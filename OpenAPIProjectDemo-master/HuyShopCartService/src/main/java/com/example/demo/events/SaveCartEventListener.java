package com.example.demo.events;







import com.example.demo.entity.CartEntity;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;


import java.util.Map;


public class SaveCartEventListener implements PersistEventListener {

    private Logger logger = LoggerFactory.getLogger(SaveCartEventListener.class);

    public SaveCartEventListener(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    StreamBridge streamBridge;

//    @PostPersist
//    public void onPostInsert(CartEntity cart) {
//        logger.info("post insert event is calling");
//        streamBridge.send("dataSyncFromCart",cart);
//    }


//    @Override
//    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
//        return false;
//    }
//
//
//    @Override
//    public void onPostInsert(PostInsertEvent postInsertEvent) {
//
//        logger.info("post insert is calling");
//    }
//
//
//    @Override
//    public void onPostInsertCommitFailed(PostInsertEvent postInsertEvent) {
//
//    }



    @Override
    public void onPersist(PersistEvent persistEvent) throws HibernateException {
        logger.info("post insert is calling");
        CartEntity cart= (CartEntity) persistEvent.getObject();
        streamBridge.send("dataSyncFromCart",cart);

    }

    @Override
    public void onPersist(PersistEvent persistEvent, Map map) throws HibernateException {
    }
}

