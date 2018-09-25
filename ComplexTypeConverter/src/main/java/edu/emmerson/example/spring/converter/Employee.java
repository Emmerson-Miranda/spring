package edu.emmerson.example.spring.converter;

public class Employee {
	 private long id;
	 private double salary;
	 
	public Employee() {
		super();
	}
	
	public Employee(String from) {
		super();
		String[] data = from.split(",");
        this.id =  Long.parseLong(data[0]); 
        this.salary = Double.parseDouble(data[1]);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", salary=" + salary + "]";
	}
	   
}
