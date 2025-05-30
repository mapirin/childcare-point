package com.childcare.point.dto;

import lombok.Data;

@Data
public class PointConfigDsplDataDto {

	private String pointMasterId;
	private String pointName;
	private String useMethod;
	private int point;
	private String activeFlg;

	//Jackson対応用
	public PointConfigDsplDataDto() {
	}

	public PointConfigDsplDataDto(String pointMasterId, String pointName, String useMethod, int point,
			String activeFlg) {
		this.pointMasterId = pointMasterId;
		this.pointName = pointName;
		this.useMethod = useMethod;
		this.point = point;
		this.activeFlg = activeFlg;
	}
}
