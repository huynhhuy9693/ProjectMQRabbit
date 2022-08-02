//package com.example.demo.events;
//
//
//import org.hibernate.HibernateException;
//import org.hibernate.event.spi.PersistEvent;
//import org.hibernate.event.spi.PersistEventListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.stream.function.StreamBridge;
//
//import java.util.Map;
//
//
//public class SaveUserEventListener implements PersistEventListener {
//
//    private Logger logger = LoggerFactory.getLogger(SaveUserEventListener.class);
//
//    public SaveUserEventListener(StreamBridge streamBridge) {
//        this.streamBridge = streamBridge;
//    }
//
//    StreamBridge streamBridge;
//
////    @Override
////    public void onPostInsert(PostInsertEvent postInsertEvent) {
////        logger.info("post insert is calling");
////        Object cart =  postInsertEvent.getEntity();
////        streamBridge.send("dataSyncFromCart",cart);
////    }
////
////    @Override
////    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
////        return false;
////    }
//
//    @Override
//    public void onPersist(PersistEvent persistEvent) throws HibernateException {
//        logger.info("post insert is calling");
//        Object cart =  persistEvent.getObject();
//        streamBridge.send("dataSyncFromCart",cart);
//    }
//
//    @Override
//    public void onPersist(PersistEvent persistEvent, Map map) throws HibernateException {
//
//    }
//
////    @Override
////    public void onPostInsertCommitFailed(PostInsertEvent postInsertEvent) {
////
////    }
//}
//
