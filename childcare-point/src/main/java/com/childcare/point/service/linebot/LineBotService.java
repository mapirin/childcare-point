package com.childcare.point.service.linebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

@Service
public class LineBotService {
	
	@Autowired
	private LineMessagingClient lineMessagingClient;
	
	/**
	 * LINE用APIで使用するメッセージ送信処理
	 * 
	 * @param userId
	 * @param message
	 */
	public void sendMessage(String userId, String message) {
		TextMessage textMessage = new TextMessage(message);
		PushMessage pushMessage = new PushMessage(userId, textMessage);
		lineMessagingClient.pushMessage(pushMessage);
	}

}
