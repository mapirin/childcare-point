package com.childcare.point.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.childcare.point.dto.PointMaterConfigDsplDataDto;
import com.childcare.point.dto.PointMaterConfigDto;
import com.childcare.point.repository.PointMasterRepository;

@Service
public class PointConfigServiceImpl {

	@Autowired
	private PointMasterRepository pointMasterRepository;

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public PointMaterConfigDto selectPointConfigData(String userName) {
		List<PointMaterConfigDsplDataDto> pointMaterConfigDsplDataDtoList = pointMasterRepository.find();
		
		PointMaterConfigDto pointMaterConfigDto= new PointMaterConfigDto();
		pointMaterConfigDto.setUserName(userName);
		pointMaterConfigDto.setPointMaterConfigDsplDataDtoList(pointMaterConfigDsplDataDtoList);
		
		return pointMaterConfigDto;
	}
}
