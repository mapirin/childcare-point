package com.childcare.point.dto;

import lombok.Data;

@Data
public class StockAndUseWindowDsplDataDto {

	private String pointId;
	private String pointName;
	private int point;

	public StockAndUseWindowDsplDataDto(String pointId, String pointName, int point) {
		this.pointId = pointId;
		this.pointName = pointName;
		this.point = point;
	}
}
