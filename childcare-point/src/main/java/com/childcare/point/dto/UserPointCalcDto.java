package com.childcare.point.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserPointCalcDto {

	private String userName;
	private int currentPoint;
	private int newPoint;
}
