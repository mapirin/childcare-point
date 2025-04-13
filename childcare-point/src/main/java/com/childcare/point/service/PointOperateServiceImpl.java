package com.childcare.point.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.childcare.point.dto.UserPointDto;
import com.childcare.point.entity.UserPoint;
import com.childcare.point.repository.UserPointRepository;

@Service
public class PointOperateServiceImpl {

	@Autowired
	private UserPointRepository userPointRepository;
	
//	@Autowired
//	public UserPoint userPoint;
	
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
//		String recordId = String.valueOf(System.currentTimeMillis());
		LocalDateTime updateTimestamp = LocalDateTime.now();

		System.out.println(userPointDto.getUserName());
		System.out.println(userPointDto.getPoint());
		System.out.println(updateTimestamp);
		
		//USER_POINT TBL用のBEANにデータ設定
		UserPoint userPoint = new UserPoint();
		userPoint.setUserName(userPointDto.getUserName());
		userPoint.setPoint(userPointDto.getPoint());
		userPoint.setUpdateTimestamp(updateTimestamp);
		
		insertUserPoint(userPoint);
		
		//POINT_LIST TBL用のBEANにデータ設定
//		pointList.setRecordId(recordId);
//		pointList.setUserName(pointForm.getUserName());
//		pointList.setPointId(pointForm.getPointId());
//		pointList.setPoint(pointForm.getPoint());
//		pointList.setUpdateTimestamp(updateTimestamp);
//		
//		pointListMapper.insertPointList(pointList);
	}
	
	/*
	 * 「ためる」「つかう」ボタン押下時処理後、ユーザのUSER_POINT TBLレコードを最新ポイントで更新
	 */
	public void insertUserPoint(UserPoint userPoint) {
		userPointRepository.save(userPoint);
	}
}
