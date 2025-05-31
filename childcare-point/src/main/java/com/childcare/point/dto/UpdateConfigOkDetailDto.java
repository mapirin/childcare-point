package com.childcare.point.dto;

import lombok.Data;

@Data
public class UpdateConfigOkDetailDto {
	private String pointMasterId;
	private String pointName;
	private String useMethod;
	private int point;
	private String activeFlg;
	private String userName;
	private String isInsertable;
}
