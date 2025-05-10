package com.childcare.point.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	
	@ManyToOne
    @JoinColumn(name = "point_name_master_id", referencedColumnName = "pointNameMasterId")
    private PointNameMaster pointNameMaster;
}
