package edu.emmerson.microservice.rabbit.client;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class RabbitmqClientQueueProvider {
	
	@Autowired
	private RabbitmqClientMessageListener listener;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value( "${spring.rabbitmq.host}" )
	private String rabbitmq_host;
	
	@Value( "${spring.rabbitmq.port}" )
	private int rabbitmq_port;
	
	@Value( "${spring.rabbitmq.username}" )
	private String rabbitmq_username;
	
	@Value( "${spring.rabbitmq.password}" )
	private String rabbitmq_pwd;
	
	private SimpleMessageListenerContainer container;
	
	public RabbitmqClientQueueProvider() {
	}

	public String[] getQueues() {
		return container.getQueueNames();
	}
	
	public String addQueue(String name) {
		registerQueue(name);
		container.setQueues(new Queue(name, true));
		return name + " added!";
	}	
	
	/**
	 * Check if queue exist, if is not create the queue and exchange.
	 * @param name
	 */
	private void registerQueue(String name) {
		boolean found = false;
		for (String queue : container.getQueueNames()) {
			if(queue.equals(name)) {
				found = true;
			}
		}
		if(!found) {
			String queueName = name;
			String exchangeName = name + ".exchange";
			String routingKey = "";

			com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
			factory.setUsername(rabbitmq_username);
			factory.setPassword(rabbitmq_pwd);
			factory.setVirtualHost("/");
			factory.setHost(rabbitmq_host);
			factory.setPort(rabbitmq_port);
			
			try {
				com.rabbitmq.client.Connection conn = factory.newConnection();
				
				Channel channel = conn.createChannel();
				channel.exchangeDeclare(exchangeName, "direct", true);
				channel.queueDeclare(queueName , true, false, false, null);
				channel.queueBind(queueName, exchangeName, routingKey);
				channel.close();
				
				conn.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
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
