package com.childcare.point.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="point_name_master")
@Data
public class PointNameMaster {

	@Id
	private String pointNameMasterId;
	private String pointName;
}
