package com.childcare.point.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/session")
public class RedisRestController {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@GetMapping("/set/{key}/{value}")
	public String setSession(@PathVariable String key, @PathVariable String value, HttpSession session) {
		session.setAttribute(key, value);
		redisTemplate.opsForValue().set("session:" + key, value);
		return "Session set: " + key + " = " + value;
	}

	@GetMapping("/get/{key}")
	public String getSession(@PathVariable String key, HttpSession session) {
		Object value = session.getAttribute(key);
		return "Session value: " + (value != null ? value : "Not found");
	}

}
