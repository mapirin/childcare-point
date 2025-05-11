package com.childcare.point.dto;

import java.util.List;

import lombok.Data;

@Data
public class PointMaterConfigDto {

	private String userName;
	private List<PointMaterConfigDsplDataDto> pointMaterConfigDsplDataDtoList;
}
