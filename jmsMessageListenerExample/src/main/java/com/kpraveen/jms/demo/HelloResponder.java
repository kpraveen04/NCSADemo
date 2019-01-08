package com.kpraveen.jms.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

public class HelloResponder {
	public static void main(String... args) throws Exception {

		ActiveMQConnectionFactory factory =null;
		Session session=null;
		Connection connection=null;
		try {

			factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			connection = factory.createConnection("admin", "admin");
			connection.start();
			
			do {
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				Destination destination = new ActiveMQQueue("request-reply-demo");
				MessageConsumer consumer = session.createConsumer(destination);

				TextMessage requestMessage = (TextMessage) consumer.receive();

				// now that we have the request message, let's construct a reply
				String replyPayload = String.format("Hello, %s", requestMessage.getText());
				String activity = requestMessage.getJMSCorrelationID();

				if (activity.equals("SayHello")) {

					replyPayload = String.format("Hello, %s", requestMessage.getText());
				}

				TextMessage replyMessage = session.createTextMessage(replyPayload);
				replyMessage.setJMSDestination(requestMessage.getJMSReplyTo());
				replyMessage.setJMSCorrelationID(requestMessage.getJMSCorrelationID());

				MessageProducer producer = session.createProducer(requestMessage.getJMSReplyTo());
				producer.send(replyMessage);

			} while (true);
		}

		finally {

			if(session!=null)
			session.close();
			if(connection!=null)
			connection.close();
		}

	}
}