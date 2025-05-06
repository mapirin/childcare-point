package com.childcare.point.service.linebot;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.linecorp.bot.client.LineMessagingClient;

@Service
public class LineBotService {

	@Autowired
	private LineMessagingClient lineMessagingClient;

	/**
	 * LINE用APIで使用するメッセージ送信処理（定期実行）
	 * 
	 * @param userId
	 * @param message
	 */
	public void sendMessage( String message) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Authorization", "Bearer " + System.getenv("CHANNEL_ACCESS_TOKEN"));
		
		System.out.println(System.getenv("CHANNEL_ACCESS_TOKEN"));

		Map<String, Object> requestBody = Map.of(
				"messages", List.of(Map.of(
						"type", "text",
						"text", message)));

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, httpHeaders);
		restTemplate.postForEntity("https://api.line.me/v2/bot/message/broadcast", request, String.class);

	}

	/**
	 * LINE用APIで使用するメッセージ送信処理（チャット受信時）
	 * 
	 * @param replyToken
	 * @param message
	 */
	public void replyMessage(String replyToken, String message) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Authorization", "Bearer " + System.getenv("CHANNEL_ACCESS_TOKEN"));

		// 指定JSON構造に沿ったリクエストボディ
		Map<String, Object> requestBody = Map.of(
				"replyToken", replyToken,
				"messages", List.of(
						Map.of("type", "text", "text", message)));

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, httpHeaders);
		
		// メッセージ受信時の返却用エンドポイントに送信
		restTemplate.postForEntity("https://api.line.me/v2/bot/message/reply", request, String.class);
	}
}
