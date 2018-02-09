package edu.emmerson.microservice.rabbit.client;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.Timed;

@Component
public class RabbitmqClientMessageListener implements MessageListener{

	@Timed
	public void onMessage(Message message) {
		System.out.println(message.getMessageProperties().getConsumerQueue() 
				+ " ==> " + new String(message.getBody()));
	}
	
}
