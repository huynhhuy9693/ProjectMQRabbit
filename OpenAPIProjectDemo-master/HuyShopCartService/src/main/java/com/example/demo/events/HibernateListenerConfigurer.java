package com.example.demo.events;


import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Component
public class HibernateListenerConfigurer {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    StreamBridge streamBridge;

    @PostConstruct
    protected void init()
    {

        SessionFactoryImplementor sessionFactory= entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        EventListenerRegistry eventListenerRegistry=sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        eventListenerRegistry.getEventListenerGroup(EventType.PERSIST).appendListener(new SaveCartEventListener(streamBridge));

    }
}
