package com.childcare.point.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.childcare.point.dto.PointListDataDto;
import com.childcare.point.service.PointListDataServiceImpl;
import com.childcare.point.service.PointOperateServiceImpl;

@RestController
@RequestMapping("/api")
public class ChildcareRestController {

	@Autowired
	public PointOperateServiceImpl pointOperateServiceImpl;

	@Autowired
	public PointListDataServiceImpl pointListDataServiceImpl;

	/**
	 * 履歴画面(前日)
	 * 
	 * doTomorrowMoveFlgが常にtrueのため、処理日より先の日時の遷移は可能
	 * doDeleteListFlgが常にfalseのため、削除不可
	 * 
	 * @param userName
	 * @param currentPoint
	 * @param updateDate
	 * @return ResponseEntity<PointListDataDto>
	 */
	@GetMapping("/list/yesterday")
	public ResponseEntity<PointListDataDto> selectListYesterday(@RequestParam String userName,
			@RequestParam int currentPoint,
			@RequestParam String updateDate) {

		//履歴データ取得
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataForChange(userName,
				currentPoint, updateDate,
				"1");

		return ResponseEntity.ok(pointListDataDto);
	}

	/**
	 * 履歴画面(翌日)
	 * 
	 * doTomorrowMoveFlgとdoDeleteListFlgは動的に変化する
	 * 履歴画面および履歴画面(前日)のルールが適用される
	 * 
	 * @param userName
	 * @param currentPoint
	 * @param updateDate
	 * @return ResponseEntity<PointListDataDto>
	 */
	@GetMapping(value = "/list/tomorrow")
	public ResponseEntity<PointListDataDto> selectListTomorrow(@RequestParam String userName,
			@RequestParam int currentPoint,
			@RequestParam String updateDate) {

		//履歴データ取得
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataForChange(userName,
				currentPoint, updateDate,
				"2");

		return ResponseEntity.ok(pointListDataDto);
	}
	
	/**
	 * 履歴画面(入力)
	 * 
	 * 日付入力テキストボックスで入力した日付のデータを取得する
	 * 
	 * doTomorrowMoveFlgとdoDeleteListFlgは動的に変化する
	 * 履歴画面および履歴画面(前日)のルールが適用される
	 * 
	 * @param userName
	 * @param currentPoint
	 * @param updateDate
	 * @return ResponseEntity<PointListDataDto>
	 */
	@GetMapping("/list/enter")
	public ResponseEntity<PointListDataDto> selectListEnter(@RequestParam String userName,
			@RequestParam int currentPoint,
			@RequestParam String updateDate) {

		//履歴データ取得
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataForChange(userName,
				currentPoint, updateDate,
				"3");

		return ResponseEntity.ok(pointListDataDto);
	}

	/**
	 * 履歴画面削除押下時処理
	 * 
	 * システム日付の履歴のみ削除ボタンが押下できる
	 * doTomorrowMoveFlgが常にfalseのため、処理日より先の日時の遷移は不可
	 * doDeleteListFlgが常にtrueのため、削除可能
	 * 
	 * @param recordId
	 * @param userName
	 * @param currentPoint
	 * @param updateDate
	 * @return ResponseEntity<PointListDataDto>
	 */
	@PostMapping("/delete")
	public ResponseEntity<PointListDataDto> deletePointList(@RequestParam String recordId,
			@RequestParam String userName, @RequestParam int currentPoint,
			@RequestParam String updateDate) {

		// 削除処理
		pointListDataServiceImpl.delete(userName, currentPoint,
				recordId);

		// 返却処理
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataForChange(userName,
				currentPoint, updateDate,
				"4");

		return ResponseEntity.ok(pointListDataDto);
	}
}
