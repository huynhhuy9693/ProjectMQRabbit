package com.example.demo.events;

import com.example.demo.entity.CartEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.cloud.stream.function.StreamBridge;

import javax.persistence.PreRemove;


@Slf4j
public class DeleteCartEventListener implements PostDeleteEventListener {

    public DeleteCartEventListener(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    StreamBridge streamBridge;


    @PreRemove
    public void preRemove(CartEntity cart) {log.info("preRemove is calling");}




    @Override
    public void onPostDelete(PostDeleteEvent postDeleteEvent) {
        log.info("delete event");
        Object cart = postDeleteEvent.getEntity();
        streamBridge.send("dataSyncFromCart",cart);
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }
}
