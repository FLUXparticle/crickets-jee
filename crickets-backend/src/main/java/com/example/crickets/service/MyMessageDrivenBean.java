package com.example.crickets.service;

import jakarta.ejb.*;
import jakarta.jms.*;
import org.jboss.logging.*;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/queue/test"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "java:jboss/DefaultJMSConnectionFactory")
    }
)
public class MyMessageDrivenBean implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(MyMessageDrivenBean.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String text = ((TextMessage) message).getText();
                System.out.println("Empfangene Nachricht: " + text);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        } else {
            LOGGER.errorf("Nachrichtentyp nicht unterstützt: %s", message.getClass());
        }
    }

}
