//package com.example.demo.events;
//
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.event.spi.PostUpdateEvent;
//import org.hibernate.event.spi.PostUpdateEventListener;
//import org.hibernate.persister.entity.EntityPersister;
//
//
//@Slf4j
//public class UpdateCartEventListener implements PostUpdateEventListener {
//    @Override
//    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
//        log.info("post update is calling");
//    }
//
//    @Override
//    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
//        return false;
//    }
//}
