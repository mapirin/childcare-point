package com.childcare.point.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.childcare.point.dto.PointListDataDto;
import com.childcare.point.dto.UpdateOkDetailDto;
import com.childcare.point.dto.UpdateOkDto;
import com.childcare.point.dto.UserPointDto;
import com.childcare.point.dto.UserPointKeyForm;
import com.childcare.point.service.ChildcarePointWindowServiceImpl;
import com.childcare.point.service.PointListDataServiceImpl;

@RestController
@RequestMapping("/api")
public class ChildcareRestController {

	@Autowired
	public ChildcarePointWindowServiceImpl childcarePointWindowServiceImpl;

	@Autowired
	public PointListDataServiceImpl pointListDataServiceImpl;

	@Autowired
	public UserPointKeyForm userPointKeyForm;

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
	@PostMapping("/list/delete")
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

	/**
	 * ためる・つかう画面OK押下時処理
	 * @param userPointCalcDto
	 * @param model
	 * @return
	 */
	@PostMapping("/update")
	public ResponseEntity<Integer> updateUseOk(@RequestBody UpdateOkDto updateOkDto) {
		System.out.println("チェック");
		
		String userName = updateOkDto.getUserName();
		List<UpdateOkDetailDto> pointDataList = updateOkDto.getPointData();

		/**
		 * 更新処理
		 * 
		 * pointDataの1要素ごとに更新処理を呼び出している
		 */
		for (UpdateOkDetailDto updateOkDetailDto : pointDataList) {
			UserPointDto userPointDto = new UserPointDto();
			userPointDto.setUserName(userName);
			userPointDto.setPointId(updateOkDetailDto.getPointId());
			userPointDto.setPoint(updateOkDetailDto.getPoint());
			
			System.out.println(updateOkDetailDto.getPointId());
			System.out.println(updateOkDetailDto.getPoint());

			childcarePointWindowServiceImpl.updatePoint(userPointDto);
		}

		// 検索処理
		int currentPoint = childcarePointWindowServiceImpl.selectPoint(userName);

		return ResponseEntity.ok(currentPoint);
	}
}
