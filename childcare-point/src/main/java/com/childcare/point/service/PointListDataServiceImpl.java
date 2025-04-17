package com.childcare.point.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.childcare.point.dto.PointListDataDto;
import com.childcare.point.repository.PointListRepository;

@Service
public class PointListDataServiceImpl {
	
	@Autowired
	public PointListRepository pointListRepository;

	public PointListDataDto selectPointListData(String userName) {
		PointListDataDto pointListDataDto = new PointListDataDto();
		pointListDataDto.setUserName(userName);
		pointListDataDto.setSelectPointList(pointListRepository.findSelectpointListByUserName(userName));
		
		return pointListDataDto;
	}
}
