package edu.emmerson.microservice.rabbit.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * To know the amount RAM memory is used by java process run: 
 * ps aux | awk '{print $2, $4, $11}' | sort -k2rn | head -n 10 | grep java
 * ps aux | grep spring-boot | awk '{print $2, $4, $11}' | sort -k2rn | head -n 10
 * @author developer
 *
 */
@SpringBootApplication
public class RabbitmqClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqClientApplication.class, args);
	}
}
