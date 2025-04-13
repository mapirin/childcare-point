package com.childcare.point.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.childcare.point.dto.UserPointCalcDto;
import com.childcare.point.dto.UserPointDto;
import com.childcare.point.dto.UserPointKeyForm;
import com.childcare.point.service.PointOperateServiceImpl;

@Controller
public class ChildcarePointController {

	@Autowired
	public PointOperateServiceImpl pointOperateServiceImpl;

	@Autowired
	public UserPointKeyForm userPointKeyForm;

	@Autowired
	public UserPointDto userPointDto;

	@RequestMapping(value = "/")
	public String showPageTitle() {
		return "childcarePointTitle";
	}

	@PostMapping(value = "/point-menu")
	public String showPageMenu(@RequestParam("userName") String userName, Model model) {
		int currentPoint = pointOperateServiceImpl.selectPoint(userName);
		System.out.println(currentPoint);

		//		UserPointKeyForm userPointKeyForm = new UserPointKeyForm();
		userPointKeyForm.setUserName(userName);
		userPointKeyForm.setCurrentPoint(currentPoint);

		model.addAttribute("userPointKeyForm", userPointKeyForm);
		return "childcarePointMenu";
	}

	@PostMapping("/stock-window")
	public String showWindowStock(@ModelAttribute("userPointKeyForm") UserPointKeyForm userPointKeyForm, Model model) {
		model.addAttribute("userPointKeyForm", userPointKeyForm);
		return "childcarePointStockWindow";
	}

	@PostMapping("/use-window")
	public String showWindowUse(@ModelAttribute("userPointKeyForm") UserPointKeyForm userPointKeyForm, Model model) {
		model.addAttribute("userPointKeyForm", userPointKeyForm);
		return "childcarePointUseWindow";
	}

	@PostMapping("/point-execute")
	public String showPageBackMenu(@ModelAttribute("userPointCalcDto") UserPointCalcDto userPointCalcDto, Model model) {

		// 処理準備
		int newPoint = userPointCalcDto.getCurrentPoint() + userPointCalcDto.getNewPoint();
		
		// エラー処理
		if (newPoint < 0) {
			String message = "ポイントが足りません。";
			userPointKeyForm.setMessage(message);
			model.addAttribute("userPointKeyForm", userPointKeyForm);
			return "childcarePointUseWindow";
		}

		// 更新処理
//		if(userPointCalcDto.getNewPoint() > 0) {
			userPointDto.setUserName(userPointCalcDto.getUserName());
			userPointDto.setPoint(newPoint);
	
			pointOperateServiceImpl.updatePoint(userPointDto);
//		}

		// 検索処理
		int currentPoint = pointOperateServiceImpl.selectPoint(userPointCalcDto.getUserName());

		// 返却処理
		userPointKeyForm.setUserName(userPointCalcDto.getUserName());
		userPointKeyForm.setCurrentPoint(currentPoint);

		model.addAttribute("userPointKeyForm", userPointKeyForm);
		return "childcarePointMenu";
	}
}
