package com.example.demo.facade;

import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.demo.dto.*;
import com.example.demo.entity.CartEntity;
import com.example.demo.entity.CartItemEntity;
import com.example.demo.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class PurchaseFacade {
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    CartRepository repository;

    @Autowired
    MailFeignClient mailFeignClient;

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    PaymentFeignClient paymentFeignClient;

    @Autowired
    MailService mailService;

    private static final String digits = "0123456789";
    private static final String ALPHA_NUMERIC = digits;
    private static Random generator = new Random();

    private static final String NO_VOUCHER = "NO-VOUCHER";
    private static final String NO_PAYMENT = "ATM";

    public PurchaseResponse placeOrder(Purchase purchase) throws MalformedURLException, MessagingException, UnsupportedEncodingException, JsonProcessingException {

        int numberOfCharactor = 6;
        Double totalPrice = 0D;

        CartDTO cartDTO = purchase.getCartDTO();

        String oderNumber = generateOrderNumber(numberOfCharactor);
        cartDTO.setOderNumber(oderNumber);

        List<CartItemDTO> cartItemDTOList = purchase.getCartDTO().getCartItemDTOList();


        // check quantity purchase -> quantity shop
        //if quantity purchase > quantity shop -> remove out of cartItem
        //else set total-price
        for (int i = 0; i < cartItemDTOList.size(); i++) {
            int quantity = productFeignClient.getQuantityById(cartItemDTOList.get(i).getProductId());
            {
                if (quantity < cartItemDTOList.get(i).getQuantity()) {
                    cartItemDTOList.remove(cartItemDTOList.get(i));
                    i--;
                }else
                {
                    cartItemDTOList.get(i).setPrice(productFeignClient.findById(cartItemDTOList.get(i).getProductId()).getPrice());
                    cartItemDTOList.get(i).setName(productFeignClient.findById(cartItemDTOList.get(i).getProductId()).getName());
                    totalPrice+=Double.valueOf(cartItemDTOList.get(i).getPrice()*cartItemDTOList.get(i).getQuantity());
                }
            }
        }


        cartDTO.setTotalPrice(totalPrice);

        UserOrder userOrder = userFeignClient.findById(purchase.getCartDTO().getUserOrder().getId());

        cartDTO.setUserOrder(userOrder);
        cartDTO.setShippingAddress(purchase.getShippingAddress());
        cartDTO.setStatus("DELIVERY");
        cartDTO.setEmail(purchase.getCartDTO().getUserOrder().getEmail());
        cartDTO.setIsSending(Boolean.FALSE);
        cartDTO.setTotalPrice(checkTotalPrice(purchase));


        //save DB and update quantity in tabble Product
        CartEntity cart = modelMapper.map(cartDTO, CartEntity.class);
        cart.setUserNameOrder(cartDTO.getUserOrder().getUserName());

        checkPromotion(purchase, cart);

        for (CartItemDTO cartItemDTO : cartItemDTOList) {
            CartItemEntity cartItemEntity = modelMapper.map(cartItemDTO, CartItemEntity.class);
            cart.add(cartItemEntity);
            int quantity = cartItemDTO.getQuantity();
            int quantityShop = productFeignClient.getQuantityById(cartItemDTO.getProductId());
            int result = (quantityShop - quantity);
            productFeignClient.updateProductQuantityForId(result, cartItemDTO.getProductId());
        }

        LocalDate localDate = LocalDate.now();
        cart.setDateOrder(localDate);
        repository.save(cart);
        purchase.setStatus("SUCCESS");

        // ObjectWriter convert object -> String (jSon)
//        ObjectWriter obj = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        String jsonPurchase = obj.writeValueAsString(purchase);

        try {
            mailService.sendMailPurchaseSuccsess(purchase);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PurchaseResponse(oderNumber);

    }

    private String generateOrderNumber(int numberOfCharactor) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfCharactor; i++) {
            int number = randomNumber(0, ALPHA_NUMERIC.length() - 1);
            char ch = ALPHA_NUMERIC.charAt(number);
            sb.append(ch);
        }
        return sb.toString();
    }

    private int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }

    public Double priceAfterVoucher(Purchase purchase) {
        Double totalPrice = purchase.getCartDTO().getTotalPrice();
        VoucherDTO voucher = paymentFeignClient.getByNameVoucher(purchase.getCartDTO().getVoucherDTO().getName());

        // if user enter voucher
        if (!purchase.getCartDTO().getVoucherDTO().getName().isEmpty()) {
            //if voucher equal voucher DB
            if (voucher != null) {
                if (voucher.getId() == 1) {
                    // discount 30% - HELLO VOUCHER
                    return totalPrice * 0.7;
                } else if (voucher.getId() == 2) {
                    //discount 50% - NEWUSER VOUCHER
                    return totalPrice * 0.5;
                }
            }

        }
        return totalPrice;
    }

    public Double priceAfterPayment(Purchase purchase) {
        Double totalPrice = purchase.getCartDTO().getTotalPrice();
        PaymentDTO payment = paymentFeignClient.getByNamePayment(purchase.getCartDTO().getPaymentDTO().getName());
        //if user payment by some payment in payment DB and not use voucher
        if (!purchase.getCartDTO().getPaymentDTO().getName().isEmpty()) {
            if (payment != null) {
                if (payment.getId() == 1) {
                    //discount 5% -VISA
                    return totalPrice * 0.95;
                } else if (payment.getId() == 2) {
                    //discount 10% -PAYPAL
                    return totalPrice * 0.9;
                } else if (payment.getId() == 3) {
                    //discount 80% -MOMO
                    return totalPrice * 0.2;
                }
            }
        }
        return totalPrice;
    }

    public Double checkTotalPrice(Purchase purchase) {
        if (priceAfterVoucher(purchase) >= priceAfterPayment(purchase)) {
            return priceAfterPayment(purchase);
        }
        return priceAfterVoucher(purchase);
    }

    public void checkPromotion(Purchase purchase, CartEntity cart) {
        VoucherDTO voucherDTO = paymentFeignClient.getByNameVoucher(purchase.getCartDTO().getVoucherDTO().getName());
        PaymentDTO paymentDTO = paymentFeignClient.getByNamePayment(purchase.getCartDTO().getPaymentDTO().getName());
        //null voucher and payment
        if (voucherDTO == null && paymentDTO == null) {
            cart.setVoucher(NO_VOUCHER);
            cart.setPayment(NO_PAYMENT);
        }
        //null payment - use voucher
        else if (voucherDTO != null && paymentDTO == null) {
            cart.setVoucher(purchase.getCartDTO().getVoucherDTO().getName().toUpperCase());
            cart.setPayment(NO_PAYMENT);
        }
        //voucher null - use payment
        else if (voucherDTO == null && paymentDTO != null) {
            cart.setVoucher(NO_VOUCHER);
            cart.setPayment(purchase.getCartDTO().getPaymentDTO().getName().toUpperCase());
        } //price of payment <= price of voucher
        else if (priceAfterVoucher(purchase) >= priceAfterPayment(purchase)) {
            cart.setVoucher(NO_VOUCHER);
            cart.setPayment(purchase.getCartDTO().getPaymentDTO().getName().toUpperCase());
        } //price of payment <= price of voucher
        else if (priceAfterVoucher(purchase) <= priceAfterPayment(purchase)) {
            cart.setVoucher(purchase.getCartDTO().getVoucherDTO().getName().toUpperCase());
            cart.setPayment(NO_PAYMENT);
        }
        // else
        else {
            cart.setVoucher(purchase.getCartDTO().getVoucherDTO().getName().toUpperCase());
            cart.setPayment(NO_PAYMENT);
        }
    }
}
