//package com.example.demo.events;
//
//import org.hibernate.boot.Metadata;
//import org.hibernate.engine.spi.SessionFactoryImplementor;
//import org.hibernate.event.service.spi.EventListenerRegistry;
//import org.hibernate.event.spi.EventType;
//import org.hibernate.integrator.spi.Integrator;
//import org.hibernate.service.spi.SessionFactoryServiceRegistry;
//
//public class EventListenerIntegrator implements Integrator {
//    @Override
//    public void integrate(Metadata metadata,
//                          SessionFactoryImplementor sessionFactoryImplementor,
//                          SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
//        EventListenerRegistry eventListenerRegistry =
//                sessionFactoryServiceRegistry.getService(EventListenerRegistry.class);
//        eventListenerRegistry.getEventListenerGroup(EventType.POST_COMMIT_INSERT)
//                .appendListener(new SaveEventListener());
//    }
//
//    @Override
//    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
//
//    }
//}
