package edu.emmerson.example.spring.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class Config {

	@Bean
	public ConversionService convertEmployee(){
		DefaultConversionService res = new DefaultConversionService();
		res.addConverter(new EmployeeConverter());
		return res;
	}
	
	@Bean
	public EmployeeConsumer employeeConsumer() {
		return new EmployeeConsumer();
	}

}
