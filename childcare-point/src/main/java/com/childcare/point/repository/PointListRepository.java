package com.childcare.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.childcare.point.dto.SelectPointListDto;
import com.childcare.point.entity.PointList;

public interface PointListRepository extends JpaRepository<PointList, String> {

	@Query("SELECT new com.childcare.point.dto.SelectPointListDto(pnm.pointName, pl.point, pl.updateTimestamp) "
			+ "FROM PointList pl "
			+ "INNER JOIN PointNameMaster pnm "
			+ "ON pl.pointId = pnm.pointMasterId "
			+ "WHERE pl.userName = :userName "
			+ "ORDER BY pl.updateTimestamp DESC")
	List<SelectPointListDto> findSelectpointListByUserName(@Param("userName")String userName);
}
