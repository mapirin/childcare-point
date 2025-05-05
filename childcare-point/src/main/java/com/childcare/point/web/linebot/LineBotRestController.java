package com.childcare.point.web.linebot;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.childcare.point.entity.LineUser;
import com.childcare.point.repository.LineUserRepository;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

@RestController
@RequestMapping("/api/line")
public class LineBotRestController {

	@Autowired
	private final LineUserRepository lineUserRepository;

	@Autowired
	private LineMessagingClient lineMessagingClient;

	public LineBotRestController(LineUserRepository lineUserRepository) {
		this.lineUserRepository = lineUserRepository;
	}

	/**
	 * LINE BOT友達登録時の処理
	 * 
	 * @param payload
	 * @return
	 */
	@PostMapping("/webhook")
	public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
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
	@PostMapping("/response")
	public ResponseEntity<String> sendResponseChat(@RequestBody Map<String, String> payload) {
		String message = payload.get("message");
		// カスタムメッセージ設定用
//		String message = "🎉 フォローありがとうございます！\n\n"
//                + "早速入力しましょう。\n"
//                + "🔗 https://childcare-point-2be5b80a9197.herokuapp.com/";;
//		TextMessage textMessage = new TextMessage(message);
//		PushMessage pushMessage = new PushMessage(userId, textMessage);
//		lineMessagingClient.pushMessage(pushMessage);

		//TODO Serviceクラスに移動
		for (LineUser lineUser : lineUserRepository.findAll()) {
			TextMessage textMessage = new TextMessage(message);
			PushMessage pushMessage = new PushMessage(lineUser.getLineUserId(), textMessage);
			lineMessagingClient.pushMessage(pushMessage);
		}
		return ResponseEntity.ok("ResponseChat Send");
	}

}
