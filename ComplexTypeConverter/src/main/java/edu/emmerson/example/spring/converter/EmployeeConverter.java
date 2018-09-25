package edu.emmerson.example.spring.converter;

import org.springframework.core.convert.converter.Converter;

public class EmployeeConverter  implements Converter<String, Employee> {

	public Employee convert(String source) {
		return new Employee(source);
	}

}
