package com.childcare.point.dto;

import lombok.Data;

@Data
public class SelectPointMasterForDeleteTargetDto {

	private String useMethod;
	private int point;

	public SelectPointMasterForDeleteTargetDto(String useMethod, int point) {
		this.useMethod = useMethod;
		this.point = point;
	}
}
