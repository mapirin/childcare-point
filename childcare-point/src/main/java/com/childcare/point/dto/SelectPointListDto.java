package com.childcare.point.dto;

import java.time.LocalDateTime;

import lombok.Data;

//@Component
@Data
public class SelectPointListDto {

	private String recordId;
	private String pointName;
	private int point;
	private LocalDateTime updateTimestamp;
	
	public SelectPointListDto(String recordId, String pointName, int point, LocalDateTime updateTimestamp) {
		this.recordId = recordId;
        this.pointName = pointName;
        this.point = point;
        this.updateTimestamp = updateTimestamp;
    }
}
