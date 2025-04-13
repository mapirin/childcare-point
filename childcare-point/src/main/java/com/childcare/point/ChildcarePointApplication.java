package com.childcare.point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.childcare.point")
public class ChildcarePointApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChildcarePointApplication.class, args);
	}

}
