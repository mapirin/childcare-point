package com.childcare.point.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.childcare.point.entity.LineUser;
import com.childcare.point.repository.LineUserRepository;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

@Component
public class LineBotScheduler {

	@Autowired
	private LineMessagingClient lineMessagingClient;
	
	@Autowired
	private LineUserRepository lineUserRepository;
	
	/**
	 * 定期実行処理
	 * 
	 * 毎日18時に送信される
	 */
	@Scheduled(cron="0 20 18 * * ?")
	public  void sendDailyMessage() {
		String message ="もう入力した？\\n"
				+ "🔗 https://childcare-point-2be5b80a9197.herokuapp.com/";
		for(LineUser lineUser : lineUserRepository.findAll()) {
			sendMessage(lineUser.getLineUserId(),message);
		}
	}
	
	//TODO Serviceクラスに移動
	private void sendMessage(String userId, String message) {
		TextMessage textMessage =new TextMessage(message);
		PushMessage pushMessage =new PushMessage(userId,textMessage);
		lineMessagingClient.pushMessage(pushMessage);
	}
	
}
