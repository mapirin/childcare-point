package com.childcare.point.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.childcare.point.dto.UserPointDto;
import com.childcare.point.entity.PointList;
import com.childcare.point.entity.UserPoint;
import com.childcare.point.repository.PointListRepository;
import com.childcare.point.repository.UserPointRepository;

@Service
public class PointOperateServiceImpl {

	@Autowired
	private UserPointRepository userPointRepository;
	
	@Autowired
	private PointListRepository pointListRepository;
	/*
	 * 
	 */
	public int selectPoint(String userName) {
		
		int currentPoint = userPointRepository.findByUserName(userName).getPoint();
		return currentPoint;
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
		
		insertUserPoint(userPoint);
		
		//POINT_LIST TBL用のBEANにデータ設定
		PointList pointList = new PointList();
		pointList.setRecordId(recordId);
		pointList.setUserName(userPointDto.getUserName());
		pointList.setPointId(userPointDto.getPointId());
		pointList.setPoint(userPointDto.getPoint());
		pointList.setUpdateTimestamp(updateTimestamp);
		
		System.out.println(pointList.getRecordId());
		System.out.println(pointList.getPointId());
		
		insertPointList(pointList);
	}
	
	/*
	 * 「ためる」「つかう」ボタン押下時処理後、ユーザのUSER_POINT TBLレコードを最新ポイントで更新
	 */
	public void insertUserPoint(UserPoint userPoint) {
		userPointRepository.save(userPoint);
	}
	
	/*
	 * 「ためる」「つかう」ボタン押下時処理後、ユーザのPOINT_LIST TBLレコードを最新ポイントで登録
	 */
	public void insertPointList(PointList pointList) {
		pointListRepository.save(pointList);
	}
}
