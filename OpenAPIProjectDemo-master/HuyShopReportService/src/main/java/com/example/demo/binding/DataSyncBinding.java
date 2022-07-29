package com.example.demo.binding;


import com.example.demo.model.CartDTO;
import com.example.demo.reactive.CartBinding;
import com.example.demo.service.CartService;
import com.example.demo.sync.CartDataSync;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
@EnableBinding(CartBinding.class)
public class DataSyncBinding {
    @Autowired
    CartService cartService;

    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(DataSyncBinding.class);

    @StreamListener(target = CartBinding.DATA_SYNC_FROM_CART)
    public void dataSyncFromCart(CartDTO cartDTO)
    {
        try
        {
            logger.info("data-sync-from-cart");
            CartDataSync cartDataSync = modelMapper.map(cartDTO, CartDataSync.class);
            cartService.save(cartDataSync);
            logger.info("data-sync-success");
        }catch (Exception e)
        {
            e.getCause().printStackTrace();
        }

    }
}
