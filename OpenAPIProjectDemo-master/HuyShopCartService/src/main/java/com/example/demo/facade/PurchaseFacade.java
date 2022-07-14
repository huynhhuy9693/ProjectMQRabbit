package com.example.demo.facade;

import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import java.time.LocalDateTime;
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
    PromotionFeignClient promotionFeignClient;

    @Autowired
    MailService mailService;

    private static final String digits = "0123456789";
    private static final String ALPHA_NUMERIC =  digits;
    private static Random generator = new Random();
    public PurchaseResponse placeOrder(Purchase purchase) throws MalformedURLException, MessagingException, UnsupportedEncodingException, JsonProcessingException {

        int numberOfCharactor = 6;
        CartDTO cartDTO = purchase.getCartDTO();

        String oderNumber = generateOrderNumber(numberOfCharactor);
        cartDTO.setOderNumber(oderNumber);

        List<CartItemDTO> cartItemDTOList = purchase.getCartDTO().getCartItemDTOList();
        cartDTO.setCartItemDTOList(cartItemDTOList);

        // check quantity purchase -> quantity shop
        for (int i =0;i<cartItemDTOList.size();i++)
        {
            int quantity = productFeignClient.getQuantityById(cartItemDTOList.get(i).getProductId());
            {
                if (quantity < cartItemDTOList.get(i).getQuantity()) {
                    cartItemDTOList.remove(cartItemDTOList.get(i));
                    i--;
                }
            }
        }
        UserOrder userOrder = userFeignClient.findById(purchase.getCartDTO().getUserOrder().getId());

        cartDTO.setUserOrder(userOrder);
        cartDTO.setShippingAddress(purchase.getShippingAddress());
        cartDTO.setStatus("DELIVERY");
        cartDTO.setEmail(purchase.getCartDTO().getUserOrder().getEmail());
        cartDTO.setIsSending(Boolean.FALSE);
        cartDTO.setTotalPrice(priceAfterPromotion(purchase));


        //save DB and update quantity in tabble Product
        CartEntity cart = modelMapper.map(cartDTO,CartEntity.class);
        cart.setUserNameOrder(cartDTO.getUserOrder().getUserName());

        if(promotionFeignClient.getByName(purchase.getCartDTO().getVoucherDTO().getName())==null)
        {
            cart.setVoucher("NO-VOUCHER");
        }else
        {
            cart.setVoucher(purchase.getCartDTO().getVoucherDTO().getName().toUpperCase());
        }

        for (CartItemDTO cartItemDTO : cartItemDTOList)
        {
            CartItemEntity cartItemEntity = modelMapper.map(cartItemDTO,CartItemEntity.class);
            cart.add(cartItemEntity);
            int quantity = cartItemDTO.getQuantity();
            int quantityShop = productFeignClient.getQuantityById(cartItemDTO.getProductId());
            int result = (quantityShop-quantity);
            productFeignClient.updateProductQuantityForId(result, cartItemDTO.getProductId());
        }

        LocalDate localDate = LocalDate.now();
        cart.setDateOrder(localDate);
        repository.save(cart);
        purchase.setStatus("SUCCESS");

        // ObjectWriter convert object -> String (jSon)
//        ObjectWriter obj = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        String jsonPurchase = obj.writeValueAsString(purchase);

        try{
            mailService.sendMailPurchaseSuccsess(purchase);

        }catch (Exception e)
        {
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
    private  int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }

    public Double priceAfterPromotion(Purchase purchase) {
        Double totalPrice = purchase.getCartDTO().getTotalPrice();
            if(!purchase.getCartDTO().getVoucherDTO().getName().isEmpty())
                {
                VoucherDTO voucher = promotionFeignClient.getByName(purchase.getCartDTO().getVoucherDTO().getName());
                if(voucher!=null)
                {
                    if (voucher.getId()==1)
                    {
                        return totalPrice*30/100;
                    }else if(voucher.getId()==2)
                    {
                        return totalPrice*50/100;
                    }
                }
            }
            return totalPrice;
    }
}
