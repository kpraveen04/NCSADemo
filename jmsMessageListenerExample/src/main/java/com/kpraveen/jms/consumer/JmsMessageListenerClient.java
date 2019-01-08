package com.kpraveen.jms.consumer;

import java.net.URISyntaxException;


public class JmsMessageListenerClient {
	public static void main(String[] args) throws URISyntaxException, Exception { 
	new ClientThread("One");
    //new ClientThread("Two");
    //new ClientThread("Three");
try {
    Thread.sleep(10000);
} catch (InterruptedException e) {
     System.out.println("Main thread Interrupted");
}
     System.out.println("Main thread exiting.");
     }
	}

