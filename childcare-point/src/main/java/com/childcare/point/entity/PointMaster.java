package com.childcare.point.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "point_master")
@Data
public class PointMaster {

	@Id
	private String pointMasterId;
	private String useMethod;
	private int point;
}
