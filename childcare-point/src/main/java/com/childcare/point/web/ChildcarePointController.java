package com.childcare.point.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.childcare.point.bean.PrepareUpdateDataBean;
import com.childcare.point.dto.PointListDataDto;
import com.childcare.point.dto.UserPointCalcDto;
import com.childcare.point.dto.UserPointDto;
import com.childcare.point.dto.UserPointKeyForm;
import com.childcare.point.service.PointListDataServiceImpl;
import com.childcare.point.service.PointOperateServiceImpl;

@Controller
public class ChildcarePointController {

	@Autowired
	public PointOperateServiceImpl pointOperateServiceImpl;

	@Autowired
	public PointListDataServiceImpl pointListDataServiceImpl;

	@Autowired
	private PrepareUpdateDataBean prepareUpdateDataBean;

	@Autowired
	public UserPointKeyForm userPointKeyForm;

	/**
	 * タイトル画面
	 * @return
	 */
	@GetMapping(value = "/")
	public String showPageTitle() {
		return "childcarePointTitle";
	}

	/**
	 * メニュー画面
	 * @param userName
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/menu")
	public String showPageMenu(@RequestParam("userName") String userName, Model model) {
		System.out.println(userName);

		int currentPoint = pointOperateServiceImpl.selectPoint(userName);

		userPointKeyForm.setUserName(userName);
		userPointKeyForm.setCurrentPoint(currentPoint);

		model.addAttribute("userPointKeyForm", userPointKeyForm);
		return "childcarePointMenu";
	}

	/**
	 * 履歴画面
	 * 
	 * doTomorrowMoveFlgが常にfalseのため、処理日より先の日時の遷移は不可
	 * doDeleteListFlgが常にtrueのため、削除可能
	 * 
	 * @param userName
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/list/today")
	public String showPageList(@RequestParam("userName") String userName,
			@RequestParam("currentPoint") int currentPoint, Model model) {
		
		//履歴データ取得
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataForInit(userName);

		pointListDataDto.setCurrentPoint(currentPoint);
		pointListDataDto.setDoTomorrowMoveFlg(false);
		pointListDataDto.setDoDeleteListFlg(true);
		model.addAttribute("pointListDataDto", pointListDataDto);
		return "childcarePointList";
	}

	/**
	 * 履歴画面(前日)
	 * 
	 * doTomorrowMoveFlgが常にtrueのため、処理日より先の日時の遷移は可能
	 * doDeleteListFlgが常にfalseのため、削除不可
	 * 
	 * @param userName
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/list/yesterday")
	public String showPageListYesterday(@RequestParam("userName") String userName,
			@RequestParam("updateDate") String updateDate, Model model) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate tmpUpdateDate = LocalDate.parse(updateDate, dtf).minusDays(1);
		updateDate = tmpUpdateDate.format(dtf);

		//履歴データ取得
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataFor(userName, updateDate);
		
		pointListDataDto.setDoTomorrowMoveFlg(true);
		pointListDataDto.setDoDeleteListFlg(false);
		model.addAttribute("pointListDataDto", pointListDataDto);
		return "childcarePointList";
	}

	/**
	 * 履歴画面(翌日)
	 * 
	 * doTomorrowMoveFlgとdoDeleteListFlgは動的に変化する
	 * 履歴画面および履歴画面(前日)のルールが適用される
	 * 
	 * @param userName
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/list/tomorrow")
	public String showPageListTomorrow(@RequestParam("userName") String userName,
			@RequestParam("updateDate") String updateDate, Model model) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate tmpUpdateDate = LocalDate.parse(updateDate, dtf).plusDays(1);
		updateDate = tmpUpdateDate.format(dtf);

		boolean doTomorrowMoveFlg = false;
		boolean doDeleteListFlg = false;
		LocalDate ld = LocalDate.now();

		// 翌日遷移可否チェック処理
		if (!updateDate.equals(ld.format(dtf))) {
			doTomorrowMoveFlg = true;
		}
		// 削除可否チェック処理
		if (updateDate.equals(ld.format(dtf))) {
			doDeleteListFlg = true;
		}
		
		//履歴データ取得
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataFor(userName, updateDate);
		
		pointListDataDto.setDoTomorrowMoveFlg(doTomorrowMoveFlg);
		pointListDataDto.setDoDeleteListFlg(doDeleteListFlg);
		model.addAttribute("pointListDataDto", pointListDataDto);
		return "childcarePointList";
	}

	/**
	 * 履歴画面削除押下時処理
	 * 
	 * システム日付の履歴のみ削除ボタンが押下できる
	 * doTomorrowMoveFlgが常にfalseのため、処理日より先の日時の遷移は不可
	 * doDeleteListFlgが常にtrueのため、削除可能
	 * 
	 * @param userPointCalcDto
	 * @param model
	 * @return
	 */
	@PostMapping("/delete")
	public String deletePointList(@RequestParam("selectedRecordId") String recordId,
			@RequestParam("userName") String userName, @RequestParam("currentPoint") int currentPoint,
			@RequestParam("updateDate") String updateDate, Model model) {
		
		// 削除処理
		pointListDataServiceImpl.delete(userName, currentPoint,
				recordId);

		// 返却処理
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataFor(userName, updateDate);
		pointListDataDto.setDoTomorrowMoveFlg(false);
		pointListDataDto.setDoDeleteListFlg(true);
		model.addAttribute("pointListDataDto", pointListDataDto);
		return "childcarePointList";
	}

	/**
	 * ためる画面
	 * @param userPointKeyForm
	 * @param model
	 * @return
	 */
	@PostMapping("/stock")
	public String showWindowStock(@ModelAttribute("userPointKeyForm") UserPointKeyForm userPointKeyForm, Model model) {
		model.addAttribute("userPointKeyForm", userPointKeyForm);
		return "childcarePointStockWindow";
	}

	/**
	 * つかう画面
	 * @param userPointKeyForm
	 * @param model
	 * @return
	 */
	@PostMapping("/use")
	public String showWindowUse(@ModelAttribute("userPointKeyForm") UserPointKeyForm userPointKeyForm, Model model) {
		model.addAttribute("userPointKeyForm", userPointKeyForm);
		return "childcarePointUseWindow";
	}

	/**
	 * ためる・つかう画面OK押下時処理
	 * @param userPointCalcDto
	 * @param model
	 * @return
	 */
	@PostMapping("/update")
	public String updateOk(@ModelAttribute("userPointCalcDto") UserPointCalcDto userPointCalcDto, Model model) {

		// 処理準備
		prepareUpdateDataBean = pointOperateServiceImpl.prepareUpdateData(userPointCalcDto);

		// エラー処理
		if (prepareUpdateDataBean.getNewPoint() < 0) {
			String message = "ポイントが足りません。";
			userPointKeyForm.setMessage(message);
			model.addAttribute("userPointKeyForm", userPointKeyForm);
			return "childcarePointUseWindow";
		}

		// 更新処理
		UserPointDto userPointDto = new UserPointDto();
		userPointDto.setUserName(userPointCalcDto.getUserName());
		userPointDto.setPointId(prepareUpdateDataBean.getSelectedRadioData()[1]);
		userPointDto.setPoint(prepareUpdateDataBean.getNewPoint());

		pointOperateServiceImpl.updatePoint(userPointDto);

		// 検索処理
		int currentPoint = pointOperateServiceImpl.selectPoint(userPointCalcDto.getUserName());

		// 返却処理
		userPointKeyForm.setUserName(userPointCalcDto.getUserName());
		userPointKeyForm.setCurrentPoint(currentPoint);

		model.addAttribute("userPointKeyForm", userPointKeyForm);
		return "childcarePointMenu";
	}
}
