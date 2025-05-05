package com.childcare.point.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.LineMessagingClient;

@Configuration
public class LineBotConfig {

	@Bean
	public LineMessagingClient lineMessagingClient() {
		return LineMessagingClient.builder("n3etbOnMsvcJXRnrAhva3wYfjRwK/3WVUdbivYnXOEqFIIyOLXEUNnahDiKSftKhZAHKa9C5nwYI5E5VAklUoNox2Dl0ZL/F1ry4LTfXza8Wi7L3nNzyvrDx7KXPiQ1+QUypToZym2tgKIjKi9E3qAdB04t89/1O/w1cDnyilFU=").build();
	}
}