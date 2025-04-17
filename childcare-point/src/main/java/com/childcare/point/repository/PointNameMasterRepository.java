package com.childcare.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.childcare.point.entity.PointNameMaster;

public interface PointNameMasterRepository extends JpaRepository<PointNameMaster, String> {

	
}
