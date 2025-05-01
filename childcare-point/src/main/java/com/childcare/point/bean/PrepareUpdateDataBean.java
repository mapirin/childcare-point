package com.childcare.point.bean;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class PrepareUpdateDataBean {
	String[] selectedRadioData;
	int newPoint;
}
