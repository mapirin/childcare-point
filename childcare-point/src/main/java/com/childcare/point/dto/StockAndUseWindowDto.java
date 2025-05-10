package com.childcare.point.dto;

import java.util.List;

import lombok.Data;

@Data
public class StockAndUseWindowDto {

	private String userName;
	private String pointId;
	private int point;
	private String message;
	private List<StockAndUseWindowDsplDataDto> stockAndUseWindowDsplDataList;
}
