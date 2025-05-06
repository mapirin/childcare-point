package com.childcare.point.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.childcare.point.entity.LineUser;
import com.childcare.point.repository.LineUserRepository;
import com.childcare.point.service.linebot.LineBotService;

@Component
public class LineBotScheduler {

	@Autowired
	private LineBotService lineBotService;

	@Autowired
	private LineUserRepository lineUserRepository;

	/**
	 * 定期実行処理
	 * 
	 * 毎日18時に送信される
	 */
	@Scheduled(cron = "0 15 16 * * ?", zone = "Asia/Tokyo")
	public void sendDailyMessage() {
		String message = "もう入力した？\n"
				+ "🔗 https://childcare-point-2be5b80a9197.herokuapp.com/";
		for (LineUser lineUser : lineUserRepository.findAll()) {
			lineBotService.sendMessage(lineUser.getLineUserId(), message);
		}
	}
}
