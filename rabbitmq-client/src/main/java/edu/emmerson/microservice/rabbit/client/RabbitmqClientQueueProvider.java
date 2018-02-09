package edu.emmerson.microservice.rabbit.client;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqClientQueueProvider {
	
	@Autowired
	private RabbitmqClientMessageListener listener;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	private SimpleMessageListenerContainer container;
	
	public RabbitmqClientQueueProvider() {
	}

	public String[] getQueues() {
		return container.getQueueNames();
	}
	
	public String addQueue(String name) {
		container.setQueues(new Queue(name, true));
		return name + " added!";
	}
	
	public String removeQueue(String name) {
		String status = " not found!";
		for (String queue : container.getQueueNames()) {
			if(queue.equals(name)) {
				container.removeQueueNames(name);
				status = " removed!";
			}
		}
		return name + status;
	}
	
	public String sendMessage(String queue, String payload) {
		rabbitTemplate.convertAndSend(queue, payload);
		return "Message (" + payload + ") sended to queue " + queue + "!";
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
		container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(listener);
		return container;
	}
}
