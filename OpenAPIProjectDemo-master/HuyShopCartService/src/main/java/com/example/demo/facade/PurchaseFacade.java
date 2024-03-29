package com.example.demo.facade;


import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.demo.dto.*;
import com.example.demo.entity.CartEntity;
import com.example.demo.entity.CartItemEntity;
import com.example.demo.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
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
    UserFeignClient userFeignClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    PaymentFeignClient paymentFeignClient;
    @Autowired
    StreamBridge streamBridge;

    private final Logger logger = LoggerFactory.getLogger(PurchaseFacade.class);

    private static final String digits = "123456789";
    private static final String ALPHA_NUMERIC = digits;
    private static Random generator = new Random();

    private static final String NO_VOUCHER = "NO-VOUCHER";
    private static final String NO_PAYMENT = "ATM";

    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) throws MalformedURLException, MessagingException, UnsupportedEncodingException, JsonProcessingException {

        int numberOfCharactor = 6;

        Double totalPrice = 0D;

        CartDTO cartDTO = purchase.getCartDTO();
        if(purchase.getStatus()==null)
        {
            purchase.setStatus("SUCCESS");
        }
            purchase.setStatus(purchase.getStatus());

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
                    cartItemDTOList.get(i).setCheck(false);
                }else
                {
                    cartItemDTOList.get(i).setCheck(true);
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
        cartDTO.setStatus("ORDER-SUCCESS");
        cartDTO.setEmail(userOrder.getEmail());
        cartDTO.setIsSending(Boolean.FALSE);
        cartDTO.setTotalPrice(checkTotalPrice(purchase));

        logger.debug("cartDTO = "+"\n"
                + "status : " + purchase.getStatus()+"\n"
                + "user_name : " + cartDTO.getUserOrder().getUserName()+"\n"
                + "name : " + cartDTO.getUserOrder().getName()+"\n"
                + "email : " + cartDTO.getUserOrder().getEmail()+"\n"
                + "status : " +cartDTO.getStatus()+"\n"
                + "total Price : "+cartDTO.getTotalPrice()+"\n"
                + "oder-Number : "+cartDTO.getOderNumber()+"\n"
                + "shipping-address : " +purchase.getShippingAddress()+"\n"
                + "voucher : " +purchase.getCartDTO().getVoucher().getName()+"\n"
                + "payment : " +purchase.getCartDTO().getPayment().getName()+"\n"
        );
        for( CartItemDTO cartItem : cartItemDTOList)
        {
            logger.debug("cart-item : " + "\n [ id:  " +cartItem.getProductId() + "\n"
                    + "name : " +cartItem.getName() + "\n"
                    + "quantity : " +cartItem.getQuantity() + "\n"
                    + "price : " +cartItem.getPrice() + " ]  " + "\n"
            );
        }

        //save DB
        CartEntity cart = modelMapper.map(cartDTO, CartEntity.class);
        cart.setUserOrder(cartDTO.getUserOrder().getUserName());
        checkPromotion(purchase, cart);

        for (int i =0; i<cartItemDTOList.size();i++) {
            CartItemEntity cartItemEntity = modelMapper.map(cartItemDTOList.get(i), CartItemEntity.class);
            if (cartItemDTOList.get(i).isCheck()==false)
            {
                cartItemDTOList.remove(cartItemDTOList.get(i));
                i--;
            }else
            cart.add(cartItemEntity);
        }

        LocalDate localDate = LocalDate.now();
        cart.setDateOrder(localDate);

        if (purchase.getStatus().equalsIgnoreCase("FALSE") || cartItemDTOList.isEmpty())
        {
            purchase.setStatus("FALSE");
            return new PurchaseResponse(oderNumber);
        }
        repository.save(cart);
        purchase.setStatus("SUCCESS");
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
        VoucherDTO voucher = paymentFeignClient.getByNameVoucher(purchase.getCartDTO().getVoucher().getName());

        // if user enter voucher
        if (!purchase.getCartDTO().getVoucher().getName().isEmpty()) {
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
        PaymentDTO payment = paymentFeignClient.getByNamePayment(purchase.getCartDTO().getPayment().getName());
        //if user payment by some payment in payment DB
        if (!purchase.getCartDTO().getPayment().getName().isEmpty()) {
            if (payment != null) {
                if (payment.getId() == 1) {
                    //discount 5% -VISA
                    return totalPrice * 0.95;
                } else if (payment.getId() == 2) {
                    //discount 10% -PAYPAL
                    return totalPrice * 0.9;
                } else if (payment.getId() == 3) {
                    //discount 20% -MOMO
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
        VoucherDTO voucherDTO = paymentFeignClient.getByNameVoucher(purchase.getCartDTO().getVoucher().getName());
        PaymentDTO paymentDTO = paymentFeignClient.getByNamePayment(purchase.getCartDTO().getPayment().getName());
        //null voucher and payment
        if (voucherDTO == null && paymentDTO == null) {
            cart.setVoucher(NO_VOUCHER);
            cart.setPayment(NO_PAYMENT);
        }
        //null payment - use voucher
        else if (voucherDTO != null && paymentDTO == null) {
            cart.setVoucher(purchase.getCartDTO().getVoucher().getName().toUpperCase());
            cart.setPayment(NO_PAYMENT);
        }
        //voucher null - use payment
        else if (voucherDTO == null && paymentDTO != null) {
            cart.setVoucher(NO_VOUCHER);
            cart.setPayment(purchase.getCartDTO().getPayment().getName().toUpperCase());
        } //price of payment <= price of voucher
        else if (priceAfterVoucher(purchase) >= priceAfterPayment(purchase)) {
            cart.setVoucher(NO_VOUCHER);
            cart.setPayment(purchase.getCartDTO().getPayment().getName().toUpperCase());
        } //price of payment <= price of voucher
        else if (priceAfterVoucher(purchase) <= priceAfterPayment(purchase)) {
            cart.setVoucher(purchase.getCartDTO().getVoucher().getName().toUpperCase());
            cart.setPayment(NO_PAYMENT);
        }
        // else
        else {
            cart.setVoucher(purchase.getCartDTO().getVoucher().getName().toUpperCase());
            cart.setPayment(NO_PAYMENT);
        }
    }
}

//DOCUMENT
// ObjectWriter convert object -> String (jSon)
//        ObjectWriter obj = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        String jsonPurchase = obj.writeValueAsString(purchase);