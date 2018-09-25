package edu.emmerson.example.spring.converter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EmployeeTest {

	public static void main(String[] args) {
		System.setProperty("myparam", "66,77");
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
		EmployeeConsumer consumer = ctx.getBean(EmployeeConsumer.class);
		System.out.println(consumer.getEmployee().toString());
		ctx.close();
	}

}
