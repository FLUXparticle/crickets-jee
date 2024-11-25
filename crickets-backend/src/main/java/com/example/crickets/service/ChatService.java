package com.example.crickets.service;

import com.example.crickets.websocket.*;
import jakarta.enterprise.context.*;
import jakarta.enterprise.event.*;
import jakarta.jms.*;
import jakarta.websocket.Session;
import org.jboss.logging.*;

import java.io.*;

@ApplicationScoped
public class ChatService {

    private static final Logger LOGGER = Logger.getLogger(ChatService.class);

    private static final String BROKER_URL = "tcp://localhost:61616"; // Beispiel: Artemis Broker URL

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session jmsSession;
    private MessageProducer producer;
    private MessageConsumer consumer;

    private Event<String> messageEvent;

/*
    public ChatService() {
        try {
            connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
            connection = connectionFactory.createConnection();
            connection.start();
            jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = jmsSession.createTopic("chat-topic");
            producer = jmsSession.createProducer(destination);
            consumer = jmsSession.createConsumer(destination);
            consumer.setMessageListener(this::onJmsMessage);
        } catch (JMSException e) {
            throw new RuntimeException("Fehler beim Initialisieren des ChatService", e);
        }
    }
*/

    // TODO Empfangene JMS-Nachrichten an alle WebSocket-Clients senden
    private void onJmsMessage(Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                String text = textMessage.getText();
                messageEvent.fire(text);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    // TODO Nachrichten von anderen Servern empfangen
    public void broadcastMessage(ChatMessage message) {
        try {
            TextMessage jmsMessage = null; // jmsSession.createTextMessage(message);
            // producer.send(jmsMessage);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    // Schließen der JMS-Verbindungen (optional)
    public void shutdown() {
        try {
            if (consumer != null) consumer.close();
            if (producer != null) producer.close();
            if (jmsSession != null) jmsSession.close();
            if (connection != null) connection.close();
        } catch (JMSException | IOException e) {
            LOGGER.error(e);
        }
    }

}
