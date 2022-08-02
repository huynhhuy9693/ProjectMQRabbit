package com.example.demo.events;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostUpdateEvent;

import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.cloud.stream.function.StreamBridge;


@Slf4j
public class UpdateCartEventListener implements PostUpdateEventListener {
    public UpdateCartEventListener(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    StreamBridge streamBridge;
    @Override
    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {

        {
            log.info("post update is calling");
            Object cart = postUpdateEvent.getEntity();
            streamBridge.send("dataSyncFromCart",cart);

        }

    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }

//    @Override
//    public void onSaveOrUpdate(SaveOrUpdateEvent saveOrUpdateEvent) throws HibernateException {
//        log.info("evenEntityUpdate: " + saveOrUpdateEvent.getEntity().getClass().getTypeName());
//        {
//            log.info("post update is calling");
//        }
//    }
}
