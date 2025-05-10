package com.childcare.point.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.childcare.point.service.linebot.LineBotService;

@Component
public class LineBotScheduler {

	@Autowired
	private LineBotService lineBotService;

	/**
	 * 定期実行処理
	 * 
	 * 毎日18時に送信される
	 */
	@Scheduled(cron = "0 00 18 * * ?", zone = "Asia/Tokyo")
	public void sendDailyMessage() {
		String message = "もう入力した？\n"
				+ "🔗 https://childcare-point-2be5b80a9197.herokuapp.com/";
		lineBotService.sendMessage(message);
	}

	/**
	 * 定期実行処理
	 * 
	 * 20分ごとに送信される
	 */
	@Scheduled(fixedRate = 20 * 60 * 1000)
	public void avoidSleep() {
		System.out.println("20分ごとに処理を実行: " + System.currentTimeMillis());
	}
}
