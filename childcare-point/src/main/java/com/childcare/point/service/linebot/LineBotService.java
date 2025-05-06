package com.childcare.point.service.linebot;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LineBotService {

	/**
	 * LINE用APIで使用するメッセージ送信処理
	 * 
	 * @param userId
	 * @param message
	 */
	public void sendMessage(String userId, String message) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Autorization", "Bearer" + System.getenv("CHANNEL_ACCESS_TOKEN"));

		Map<String, Object> requestBody = Map.of(
				"to", userId,
				"messages", List.of(Map.of(
						"type", "text",
						"text", message)));

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, httpHeaders);
		restTemplate.postForEntity("https://api.line.me/v2/bot/message/push", request, String.class);
	}
}
