package com.childcare.point.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.childcare.point.dto.PointListDataDto;
import com.childcare.point.dto.SelectPointMasterForDeleteTargetDto;
import com.childcare.point.entity.UserPoint;
import com.childcare.point.repository.PointListRepository;
import com.childcare.point.repository.UserPointRepository;

@Service
public class PointListDataServiceImpl {

	@Autowired
	private UserPointRepository userPointRepository;
	
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

		pointListDataDto.setSelectPointList(
				pointListRepository.findSelectPointListByUserNameAndUpdateDate(userName, updateDate));

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

		pointListDataDto.setSelectPointList(
				pointListRepository.findSelectPointListByUserNameAndUpdateDate(userName, updateDate));

		return pointListDataDto;
	}

	/**
	 * 履歴データ削除処理
	 */
	@Transactional
	public void delete(String userName, int currentPoint, String recordId) {
		//TODO カンマをそもそも付与しない方法は？
		recordId = recordId.replace(",", "");

		//パラメータを使用して、新規TBL POINT_MASTERから削除対処のポイントを取得
		SelectPointMasterForDeleteTargetDto selectPointMasterForDeleteTargetDto = pointListRepository
				.findSelectPointListByRecordIdAndUserName(recordId, userName);
		
		//削除するレコードのポイントを現在ポイントと計算
		if (selectPointMasterForDeleteTargetDto.getUseMethod().equals("1")) {
			selectPointMasterForDeleteTargetDto.setPoint(currentPoint - selectPointMasterForDeleteTargetDto.getPoint());
		} else {
			selectPointMasterForDeleteTargetDto.setPoint(currentPoint + selectPointMasterForDeleteTargetDto.getPoint());
		}
		
		//計算した値でUSER_POINTを更新
		LocalDateTime updateTimestamp = LocalDateTime.now();
		UserPoint userPoint = new UserPoint();
		userPoint.setUserName(userName);
		userPoint.setPoint(selectPointMasterForDeleteTargetDto.getPoint());
		userPoint.setUpdateTimestamp(updateTimestamp);
		
		userPointRepository.save(userPoint);
		
		//削除
		pointListRepository.deleteByUserNameAndRecordId(userName, recordId);
	}
}
