package com.childcare.point.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserPointDto {

	private String userName;
	private String pointId;
	private int point;
}
