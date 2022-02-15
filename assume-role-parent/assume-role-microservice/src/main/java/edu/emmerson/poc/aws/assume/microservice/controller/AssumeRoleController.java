package edu.emmerson.poc.aws.assume.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.emmerson.poc.aws.assume.library.credentials.CredentialsStrategy;
import edu.emmerson.poc.aws.assume.library.pojo.CredentialsRequest;
import edu.emmerson.poc.aws.assume.library.pojo.TemporalCredential;
import edu.emmerson.poc.aws.assume.microservice.component.CredentialsDelegateComponent;

@SpringBootApplication
@ComponentScan("edu.emmerson.poc.aws.assume.microservice.component")
@RestController
public class AssumeRoleController {
	
	@Autowired
	private CredentialsDelegateComponent cd;
	
	@RequestMapping("/hello")
	public String hello() {
		return "Hello world";
	}
	
	/**
	 * curl -X POST -d '{"region":"eu-west-2","roleARN":"roleARN","roleSessionName":"roleSessionName","profileName":"profileName"}' -H "Content-Type: application/json" localhost:8080/credentials
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/credentials")
	public TemporalCredential getCredentials(@RequestBody @NonNull CredentialsRequest request) throws Exception {
		System.out.println("Request for temporal credentials: " + request);
		CredentialsStrategy cs = cd.getCredentialsStratey();
		TemporalCredential cred = cd.loadCredentialsStrategy(cs, request);
		return cred;
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(AssumeRoleController.class, args);
	}
	
}
