package com.childcare.point.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class PointListDataDto {

	private String userName;
	private int currentPoint;
	private String updateDate;
	private boolean doDeleteListFlg;
	private List<SelectPointListDto> selectPointList;
}
