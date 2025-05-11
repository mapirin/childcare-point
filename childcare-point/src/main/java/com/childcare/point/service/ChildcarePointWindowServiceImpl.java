package com.childcare.point.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.childcare.point.dto.StockAndUseWindowDsplDataDto;
import com.childcare.point.dto.StockAndUseWindowDto;
import com.childcare.point.dto.UserPointDto;
import com.childcare.point.dto.UserPointKeyForm;
import com.childcare.point.entity.PointList;
import com.childcare.point.entity.UserPoint;
import com.childcare.point.repository.PointListRepository;
import com.childcare.point.repository.PointMasterRepository;
import com.childcare.point.repository.UserPointRepository;

@Service
public class ChildcarePointWindowServiceImpl {

	private final String USE_METHOD_STOCK = "1";
	private final String USE_METHOD_USE = "2";

	@Autowired
	private UserPointRepository userPointRepository;

	@Autowired
	private PointListRepository pointListRepository;

	@Autowired
	private PointMasterRepository pointMasterRepository;

	/**
	 * 「メニュー」画面遷移時に遷移ユーザをもとにポイントを取得
	 */
	public int selectPoint(String userName) {

		int currentPoint = userPointRepository.findByUserName(userName).getPoint();
		return currentPoint;
	}

	/**
	 * 「ためる」画面の描画用データ取得用処理
	 */
	public StockAndUseWindowDto selectStockWindowData(UserPointKeyForm userPointKeyForm) {
		List<StockAndUseWindowDsplDataDto> stockAndUseWindowDtoList = pointMasterRepository
				.findAllByUseMethod(USE_METHOD_STOCK);

		StockAndUseWindowDto stockAndUseWindowDto = new StockAndUseWindowDto();
		stockAndUseWindowDto.setUserName(userPointKeyForm.getUserName());
		stockAndUseWindowDto.setPoint(userPointKeyForm.getCurrentPoint());
		stockAndUseWindowDto.setStockAndUseWindowDsplDataList(stockAndUseWindowDtoList);

		return stockAndUseWindowDto;
	}
	
	/**
	 * 「つかう」画面の描画用データ取得用処理
	 */
	public StockAndUseWindowDto selectUseWindowData(UserPointKeyForm userPointKeyForm) {
		List<StockAndUseWindowDsplDataDto> stockAndUseWindowDtoList = pointMasterRepository
				.findAllByUseMethod(USE_METHOD_USE);

		StockAndUseWindowDto stockAndUseWindowDto = new StockAndUseWindowDto();
		stockAndUseWindowDto.setUserName(userPointKeyForm.getUserName());
		stockAndUseWindowDto.setPoint(userPointKeyForm.getCurrentPoint());
		stockAndUseWindowDto.setStockAndUseWindowDsplDataList(stockAndUseWindowDtoList);

		return stockAndUseWindowDto;
	}

	/**
	 * 「ためる」「つかう」用CRUD処理
	 * ポイント表示用更新処理と履歴確認用のTBL登録処理
	 */
	@Transactional
	public void updatePoint(UserPointDto userPointDto) {
		//recordIdとupdateTimestampを生成
		String recordId = String.valueOf(System.currentTimeMillis());
		LocalDateTime updateTimestamp = LocalDateTime.now();

		//USER_POINT TBL用のBEANにデータ設定
		UserPoint userPoint = new UserPoint();
		userPoint.setUserName(userPointDto.getUserName());
		userPoint.setPoint(userPointDto.getPoint());
		userPoint.setUpdateTimestamp(updateTimestamp);
		
		System.out.println(userPointDto.getPoint());

		updateUserPoint(userPoint);

		//POINT_LIST TBL用のBEANにデータ設定
		PointList pointList = new PointList();
		pointList.setRecordId(recordId);
		pointList.setUserName(userPointDto.getUserName());
		pointList.setPointId(userPointDto.getPointId());
		pointList.setPoint(userPointDto.getPoint());
		pointList.setUpdateTimestamp(updateTimestamp);

		insertPointList(pointList);
	}

	/*
	 * 「ためる」「つかう」ボタン押下時処理後、ユーザのUSER_POINT TBLレコードを最新ポイントで更新
	 */
	public void updateUserPoint(UserPoint userPoint) {
		userPointRepository.save(userPoint);
	}

	/*
	 * 「ためる」「つかう」ボタン押下時処理後、ユーザのPOINT_LIST TBLレコードを最新ポイントで登録
	 */
	public void insertPointList(PointList pointList) {
		pointListRepository.save(pointList);
	}
}
