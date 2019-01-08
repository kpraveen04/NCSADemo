package com.kpraveen.jms.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;
import java.util.Random;

public class Requestor {
    public static void main(String... args) throws Exception {

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = factory.createConnection("admin", "admin");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = new ActiveMQQueue("request-reply-demo");
        MessageProducer producer = session.createProducer(destination);

        //need to ask activemq to create a temporary queue to hold replies
        Destination replyDestination1 = session.createTemporaryQueue();

        //now let's construct the message with a simple payload
        TextMessage message = session.createTextMessage("kpraveen");
        message.setJMSCorrelationID("SayHello");
      
        //load up the message with instructions on how to get back here (JMS Header)
        message.setJMSReplyTo(replyDestination1);
        //give it a correlation ID to match (JMS Header)
        //message.setJMSCorrelationID(Long.toHexString(new Random(System.currentTimeMillis()).nextLong()));
        producer.send(message);
        
        
        Destination replyDestination2 = session.createTemporaryQueue();
        message.setJMSCorrelationID("SayBye");
        
        //load up the message with instructions on how to get back here (JMS Header)
        message.setJMSReplyTo(replyDestination2);
        //give it a correlation ID to match (JMS Header)
        //message.setJMSCorrelationID(Long.toHexString(new Random(System.currentTimeMillis()).nextLong()));
        producer.send(message);

        Thread thread = new Thread();
        //thread.sleep(10000);
       
        //now let's wait for replies
        MessageConsumer consumer = session.createConsumer(replyDestination1);
        TextMessage reply = (TextMessage)consumer.receive();
        System.out.println("RECEIVED: "+reply.getText());
        
      //now let's wait for replies
        MessageConsumer consumer1 = session.createConsumer(replyDestination2);
        TextMessage reply1 = (TextMessage)consumer.receive();
        System.out.println("RECEIVED: "+reply1.getText());

        session.close();
        connection.close();
    }
}