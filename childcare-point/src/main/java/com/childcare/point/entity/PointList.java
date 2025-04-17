package com.childcare.point.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "point_list")
@Data
public class PointList {

	@Id
	private String recordId;
	private String userName;
	private String pointId;
//	@Column(name = "point_master_id", insertable = false, updatable = false)
//	private String pointMasterId;
	@ManyToOne
    @JoinColumn(name = "point_master_id", referencedColumnName = "pointMasterId")
    private PointNameMaster pointNameMaster;
	private int point;
	private LocalDateTime updateTimestamp;
}
