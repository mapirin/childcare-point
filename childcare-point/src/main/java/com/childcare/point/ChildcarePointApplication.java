package com.childcare.point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.childcare.point")
@EnableScheduling
public class ChildcarePointApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChildcarePointApplication.class, args);
	}

}
