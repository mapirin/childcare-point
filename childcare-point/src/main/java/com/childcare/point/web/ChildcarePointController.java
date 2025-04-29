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
	public UserPointKeyForm userPointKeyForm;

	@Autowired
	public UserPointDto userPointDto;

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
	 * @param userName
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/list")
	public String showPageList(@RequestParam("userName") String userName, Model model) {
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataForInit(userName);
		model.addAttribute("pointListDataDto", pointListDataDto);
		return "childcarePointList";
	}

	/**
	 * 履歴画面(前日)
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
		
		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataFor(userName, updateDate);
		model.addAttribute("pointListDataDto", pointListDataDto);
		return "childcarePointList";
	}

	/**
	 * 履歴画面(翌日)
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

		PointListDataDto pointListDataDto = pointListDataServiceImpl.selectPointListDataFor(userName, updateDate);
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
	public String showPageBackMenu(@ModelAttribute("userPointCalcDto") UserPointCalcDto userPointCalcDto, Model model) {

		// 処理準備
		String[] selectedRadioData = userPointCalcDto.getSelectedRadioData().split(",");
		int newPoint = userPointCalcDto.getCurrentPoint() + Integer.parseInt(selectedRadioData[0]);

		// エラー処理
		if (newPoint < 0) {
			String message = "ポイントが足りません。";
			userPointKeyForm.setMessage(message);
			model.addAttribute("userPointKeyForm", userPointKeyForm);
			return "childcarePointUseWindow";
		}

		// 更新処理
		userPointDto.setUserName(userPointCalcDto.getUserName());
		userPointDto.setPointId(selectedRadioData[1]);
		userPointDto.setPoint(newPoint);
		System.out.println(userPointDto.getPoint());

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
