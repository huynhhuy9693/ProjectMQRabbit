package com.example.demo.events;







import com.example.demo.entity.CartEntity;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;

import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

public class SaveEventListener implements PostInsertEventListener {

    private Logger logger = LoggerFactory.getLogger(SaveEventListener.class);
    @Autowired
    StreamBridge streamBridge;



    @PostPersist
    public void onPostInsert(CartEntity cart) {
        logger.info("post insert event is calling");
        streamBridge.send("dataSyncFromCart",cart);
    }
    @PreUpdate
    public void onPreUpdate(CartEntity cart) {
        logger.info("update cart-orderNumber : " + cart.getOderNumber());
    }

    @PostUpdate
    public void onPostUpdate(CartEntity cart) {
        logger.info("post update event is calling");
        streamBridge.send("dataSyncFromCart",cart);
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }

    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {
        Object cart = postInsertEvent.getEntity();
        logger.info("cart-info"+cart);
    }



}

