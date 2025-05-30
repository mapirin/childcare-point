package com.childcare.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.childcare.point.dto.PointConfigDsplDataDto;
import com.childcare.point.dto.StockAndUseWindowDsplDataDto;
import com.childcare.point.entity.PointMaster;

public interface PointMasterRepository extends JpaRepository<PointMaster, String> {

	@Query("SELECT new com.childcare.point.dto.StockAndUseWindowDsplDataDto(pm.pointMasterId, pnm.pointName, pm.point) "
			+ "FROM PointMaster pm "
			+ "INNER JOIN PointNameMaster pnm "
			+ "ON pm.pointMasterId = pnm.pointNameMasterId "
			+ "WHERE pm.useMethod = :useMethod "
			+ "AND pm.activeFlg = '1'" 
			+ "ORDER BY pm.pointMasterId ")
	List<StockAndUseWindowDsplDataDto> findAllByUseMethod(@Param("useMethod") String useMethod);

	@Query("SELECT new com.childcare.point.dto.PointConfigDsplDataDto(pm.pointMasterId, pnm.pointName, pm.useMethod, pm.point, pm.activeFlg) "
			+ "FROM PointMaster pm "
			+ "INNER JOIN PointNameMaster pnm "
			+ "ON pm.pointMasterId = pnm.pointNameMasterId "
			+ "ORDER BY pm.pointMasterId ")
	List<PointConfigDsplDataDto> find();

}
