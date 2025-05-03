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
	 * 
	 * @param userName
	 * @param currentPoint
	 * @param updateDate
	 * @return
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
	 * 
	 * @param userName
	 * @param currentPoint
	 * @param updateDate
	 * @return
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
	 * 
	 * @param recordId
	 * @param userName
	 * @param currentPoint
	 * @param updateDate
	 * @return
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
