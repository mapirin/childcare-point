package com.childcare.point.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.childcare.point.dto.SelectPointListForDsplDataDto;
import com.childcare.point.dto.SelectPointMasterForDeleteTargetDto;
import com.childcare.point.entity.PointList;

public interface PointListRepository extends JpaRepository<PointList, String> {

	@Query("SELECT new com.childcare.point.dto.SelectPointListForDsplDataDto(pl.recordId, pnm.pointName, pl.point, pl.updateTimestamp) "
			+ "FROM PointList pl "
			+ "INNER JOIN PointNameMaster pnm "
			+ "ON pl.pointId = pnm.pointNameMasterId "
			+ "WHERE pl.userName = :userName "
			+ "AND TO_CHAR(pl.updateTimestamp, 'yyyy/MM/dd') = :updateDate "
			+ "ORDER BY pl.updateTimestamp DESC")
	List<SelectPointListForDsplDataDto> findSelectPointListByUserNameAndUpdateDate(@Param("userName") String userName,
			@Param("updateDate") String updateDate);

	@Query("SELECT new com.childcare.point.dto.SelectPointMasterForDeleteTargetDto(pm.useMethod, pm.point) "
			+ "FROM PointList pl "
			+ "INNER JOIN PointMaster pm "
			+ "ON pl.pointId = pm.pointMasterId "
			+ "WHERE pl.recordId = :recordId "
			+ "AND pl.userName = :userName ")
	SelectPointMasterForDeleteTargetDto findSelectPointListByRecordIdAndUserName(
			@Param("recordId") String recordId, @Param("userName") String userName);

	@Transactional
	@Modifying
	@Query("DELETE FROM PointList "
			+ "WHERE userName = :userName "
			+ "AND recordId = :recordId")
	void deleteByUserNameAndRecordId(@Param("userName") String userName, @Param("recordId") String recordId);
}
