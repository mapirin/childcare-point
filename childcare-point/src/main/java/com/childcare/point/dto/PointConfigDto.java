package com.childcare.point.dto;

import java.util.List;

import lombok.Data;

@Data
public class PointConfigDto {

	private String userName;
	private List<PointConfigDsplDataDto> pointConfigDsplDataDtoList;
}
