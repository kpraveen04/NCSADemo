package com.kpraveen.jms.producer;

import java.net.URISyntaxException;

public class JmsMessageListenerProducer {
	public static void main(String[] args) throws URISyntaxException, Exception { 
	new MessageProducerThread("One");
    //new MessageProducerThread("Two");
    //new MessageProducerThread("Three");
try {
    Thread.sleep(10000);
} catch (InterruptedException e) {
     System.out.println("Main thread Interrupted");
}
     System.out.println("Main thread exiting.");
     }
	}
