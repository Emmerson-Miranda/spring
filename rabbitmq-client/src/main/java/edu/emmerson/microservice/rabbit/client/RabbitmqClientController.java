package edu.emmerson.microservice.rabbit.client;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rabbitmq/queue")
@Api(value="rabbitmq", description="Operations related to listen RabbitMQ queues!")
public class RabbitmqClientController {
	
	@Autowired
	RabbitmqClientQueueProvider queueProvider;
    
    @Timed
    @ApiOperation(value = "Add a message to a queue.")
    @RequestMapping(method = RequestMethod.PUT, value = "/{queueName}", produces = "application/json")
    public ResponseEntity<String> sendMessage(@PathVariable String queueName, @RequestBody String payload) {
		String msg = queueProvider.sendMessage(queueName, payload + ". Timestamp: " + System.currentTimeMillis());
    	return new ResponseEntity<String>("{\"result\" : \"" + msg + "\"}", HttpStatus.OK);
    }
	
    @Timed
    @ApiOperation(value = "Add a listener to a specific queue.")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> addQueue(@RequestBody String name) {
		String msg = queueProvider.addQueue(name);
    	return new ResponseEntity<String>("{\"result\" : \"" + msg + "\"}", HttpStatus.OK);
    }
    
    @Timed
    @ApiOperation(value = "Remove a listener to a specific queue.")
    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> removeQueue(@RequestBody String name) {
		String msg = queueProvider.removeQueue(name);
    	return new ResponseEntity<String>("{\"result\" : \"" + msg + "\"}", HttpStatus.OK);
    }
    
    @Timed
    @ApiOperation(value = "Return a list of queues listened")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> listqueues() {
    	final StringBuilder res = new StringBuilder();
    	Arrays.stream(queueProvider.getQueues()).forEachOrdered(i -> res.append(i).append(" " ));
    	return ResponseEntity.ok().body("{\"result\" : \"" + res.toString() + "\"}");
    }

}
