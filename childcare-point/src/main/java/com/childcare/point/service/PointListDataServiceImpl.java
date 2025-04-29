package com.childcare.point.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.childcare.point.dto.PointListDataDto;
import com.childcare.point.repository.PointListRepository;

@Service
public class PointListDataServiceImpl {

	@Autowired
	public PointListRepository pointListRepository;

	/**
	 * 履歴データ取得処理(初期表示時)
	 * 
	 * 処理時のシステム日をyyyy/MM/dd形式に変換し、対象のデータを取得する
	 * @param userName
	 * @return
	 */
	public PointListDataDto selectPointListDataForInit(String userName) {
		PointListDataDto pointListDataDto = new PointListDataDto();
		
		LocalDate ld = LocalDate.now();
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.JAPAN);
		String updateDate = ld.format(sdf);
		
		pointListDataDto.setUserName(userName);
		pointListDataDto.setUpdateDate(updateDate);
		
		pointListDataDto.setSelectPointList(pointListRepository.findSelectpointListByUserName(userName, updateDate));

		return pointListDataDto;
	}
	
	/**
	 * 履歴データ取得処理(指定日処理)
	 * 
	 * 処理時のシステム日をyyyy/MM/dd形式に変換し、対象のデータを取得する
	 * @param userName
	 * @return
	 */
	public PointListDataDto selectPointListDataFor(String userName, String updateDate) {
		PointListDataDto pointListDataDto = new PointListDataDto();
		
		pointListDataDto.setUserName(userName);
		pointListDataDto.setUpdateDate(updateDate);
		
		pointListDataDto.setSelectPointList(pointListRepository.findSelectpointListByUserName(userName, updateDate));

		return pointListDataDto;
	}
}
