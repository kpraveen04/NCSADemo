package com.kpraveen.jms.producer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;



public class MessageProducerThread implements Runnable {
	String name;
	Thread t;
	MessageProducerThread (String thread){
	    name = "MessageProducerThread"; 
	    t = new Thread(this, name);
	System.out.println("New thread: " + t);
	t.start();
	}

	public void run() {
	 try {
			BrokerService broker = BrokerFactory.createBroker(new URI(
					"broker:(tcp://localhost:61618)"));
			broker.start();
			Connection connection = null;
			try {
				// Producer
				ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
						"tcp://localhost:61618");
				connection = connectionFactory.createConnection();
				Session session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				Queue queue = session.createQueue("customerQueue");
				String payload = "Important Task from Producer thread";
				Message msg;
				MessageProducer producer;
				
				
				//for(int i=0;i<3;i++) {
					
					//payload+=i;
					producer = session.createProducer(queue);
					 //need to ask activemq to create a temporary queue to hold replies
					Destination replyDestination = session.createTemporaryQueue();
					msg = session.createTextMessage(payload);
					 //load up the message with instructions on how to get back here (JMS Header)
					msg.setJMSReplyTo(replyDestination);
			        //give it a correlation ID to match (JMS Header)
					msg.setJMSCorrelationID(Long.toHexString(new Random(System.currentTimeMillis()).nextLong()));
					
					System.out.println("***************************Sending text '" + payload + "'************************************");
					producer.send(msg);
			//	}
				
				 //now let's wait for replies
		        MessageConsumer consumer = session.createConsumer(replyDestination);
		        TextMessage reply = (TextMessage)consumer.receive();
		        System.out.println("RECEIVED: "+reply.getText());
		        
				session.close();
			} finally {
				if (connection != null) {
					connection.close();
				}
				broker.stop();
			}
		}catch (InterruptedException e) {
	     System.out.println(name + "Interrupted");
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	     System.out.println(name + " exiting.");
	}
	}
