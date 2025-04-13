package com.childcare.point.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserPointKeyForm {

	private String userName;
	private int currentPoint;
	private String message;
}
