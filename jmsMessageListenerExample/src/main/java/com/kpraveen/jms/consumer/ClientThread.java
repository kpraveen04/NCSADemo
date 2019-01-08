package com.kpraveen.jms.consumer;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import com.kpraveen.jms.producer.ConsumerMessageListener;

public class ClientThread implements Runnable {
	String name;
	Thread t;
	ClientThread (String thread){
	    name = "ClientThread"; 
	    t = new Thread(this, name);
	System.out.println("New thread: " + t);
	t.start();
	}

	public void run() {
	 try {
		 
		 BrokerService broker = BrokerFactory.createBroker(new URI(
					"broker:(tcp://localhost:61620)"));
			broker.start();
			Connection connection = null;
			try {
				// Producer
				ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
						"tcp://localhost:61620");
				connection = connectionFactory.createConnection();
				Session session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				Queue queue = session.createQueue("customerQueue");
				
				// Consumer
				MessageConsumer consumer = session.createConsumer(queue);
				consumer.setMessageListener(new ConsumerMessageListener("Consumer"));
				connection.start();
				Thread.sleep(1000);
				session.close();
			} finally {
				if (connection != null) {
					connection.close();
				}
				broker.stop();
			}
		 
		 
	    /* for(int i = 5; i > 0; i--) {
	     System.out.println(name + ": " + i);
	      Thread.sleep(1000);
	}*/
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
