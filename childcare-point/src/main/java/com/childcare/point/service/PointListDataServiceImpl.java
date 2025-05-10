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
	 * 
	 * @param userName
	 * @return
	 */
	public PointListDataDto selectPointListDataForInit(String userName) {
		PointListDataDto pointListDataDto = new PointListDataDto();

		//日付変換
		LocalDate today = LocalDate.now();
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.JAPAN);
		String updateDate = today.format(sdf);

		//取得処理
		pointListDataDto.setUserName(userName);
		pointListDataDto.setUpdateDate(updateDate);

		pointListDataDto.setSelectPointList(
				pointListRepository.findSelectPointListByUserNameAndUpdateDate(userName, updateDate));

		return pointListDataDto;
	}

	/**
	 * 履歴データ取得処理(日付変更時処理)
	 * 
	 * 処理時のシステム日をyyyy/MM/dd形式に変換
	 * 日付変換用のフラグを使用して対象のデータを取得する
	 * 
	 * @param userName
	 * @param updateDate
	 * @return PointListDataDto
	 */
	public PointListDataDto selectPointListDataForChange(String userName, int currentPoint, String updateDate,
			String changeFlg) {
		LocalDate today = LocalDate.now();

		boolean doTomorrowMoveFlg = false;
		boolean doDeleteListFlg = false;

		//日付変更処理(前日・翌日)
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.JAPAN);
		LocalDate tmpUpdateDate = LocalDate.parse(updateDate, dtf);

		switch (changeFlg) {
		// 前日処理なら処理日-1
		case "1":
			tmpUpdateDate = tmpUpdateDate.minusDays(1);
			break;
		// 翌日処理なら処理日+1
		case "2":
			tmpUpdateDate = tmpUpdateDate.plusDays(1);
			break;
		
		case "3":
			if (today.isAfter(tmpUpdateDate)) {
				
			}
			break;
		default:
			break;
		}
		updateDate = tmpUpdateDate.format(dtf);

		//翌日遷移可否チェック処理
		if (!updateDate.equals(today.format(dtf))) {
			doTomorrowMoveFlg = true;
		}
		//削除可否チェック処理
		if (updateDate.equals(today.format(dtf))) {
			doDeleteListFlg = true;
		}

		//履歴データ取得
		PointListDataDto pointListDataDto = new PointListDataDto();
		pointListDataDto.setSelectPointList(
				pointListRepository.findSelectPointListByUserNameAndUpdateDate(userName, updateDate));

		//返却データ設定
		pointListDataDto.setUserName(userName);
		pointListDataDto.setCurrentPoint(currentPoint);
		pointListDataDto.setUpdateDate(updateDate);
		pointListDataDto.setDoTomorrowMoveFlg(doTomorrowMoveFlg);
		pointListDataDto.setDoDeleteListFlg(doDeleteListFlg);

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
