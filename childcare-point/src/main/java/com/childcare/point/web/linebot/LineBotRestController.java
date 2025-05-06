package com.childcare.point.web.linebot;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.childcare.point.entity.LineUser;
import com.childcare.point.repository.LineUserRepository;
import com.childcare.point.service.linebot.LineBotService;

@RestController
@RequestMapping("/api/line")
public class LineBotRestController {

	@Autowired
	private final LineUserRepository lineUserRepository;

	@Autowired
	private LineBotService lineBotService;

	public LineBotRestController(LineUserRepository lineUserRepository) {
		this.lineUserRepository = lineUserRepository;
	}

	@PostMapping("/webhook")
	public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
		List<Map<String, Object>> events = (List<Map<String, Object>>) payload.get("events");

		for (Map<String, Object> event : events) {
			String eventType = (String) event.get("type"); // イベントタイプを取得

			if (eventType.equals("message")) {
				return handleRequest(payload,"https://childcare-point-2be5b80a9197.herokuapp.com/api/line/message");
			} else if (eventType.equals("follow")) {
				return handleRequest(payload,"https://childcare-point-2be5b80a9197.herokuapp.com/api/line/init");
			}
		}
		return ResponseEntity.ok("Event processed");
	}

	private ResponseEntity<String> handleRequest(Map<String, Object> payload, String endpoint) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity(payload, httpHeaders);

		return restTemplate.postForEntity(endpoint, request, String.class);

	}

	/**
	 * LINE BOT友達登録時の処理
	 * 
	 * @param payload
	 * @return
	 */
	@PostMapping("/init")
	public ResponseEntity<String> handleInitUser(@RequestBody Map<String, Object> payload) {
		System.out.println("Webhook received: " + payload);

		//TODO Serviceクラスに移動
		List<Map<String, Object>> events = (List<Map<String, Object>>) payload.get("events");

		for (Map<String, Object> event : events) {
			String eventType = (String) event.get("type"); // イベントタイプを取得
			Map<String, Object> source = (Map<String, Object>) event.get("source");
			String userId = (String) source.get("userId");

			// リクエストpayloadのeventTypeが友達登録時(follow)の場合
			if ("follow".equals(eventType)) {
				System.out.println("Follow event received from user: " + userId);

				// はじめて友達登録をする場合
				if (lineUserRepository.findByLineUserId(userId) == null) {
					LineUser lineUser = new LineUser();
					lineUser.setLineUserId(userId);
					lineUserRepository.save(lineUser);
				}
			}
		}
		return ResponseEntity.ok("Webhook Received");
	}

	/**
	 * チャット受信時の応答処理
	 * 
	 * @param payload
	 * @return
	 */
	@PostMapping("/message")
	public ResponseEntity<String> sendResponseChat(@RequestBody Map<String, String> payload) {
//		String message = payload.get("message");
		// カスタムメッセージ設定用
				String message = "あぅ～";

		for (LineUser lineUser : lineUserRepository.findAll()) {
			lineBotService.sendMessage(lineUser.getLineUserId(), message);
		}
		return ResponseEntity.ok("ResponseChat Send");
	}

}
