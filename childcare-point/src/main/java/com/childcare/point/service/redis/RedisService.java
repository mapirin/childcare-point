package com.childcare.point.service.redis;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.childcare.point.dto.UpdateConfigOkDetailDto;

@Service
public class RedisService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void saveSessionData(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public List<UpdateConfigOkDetailDto> getData(String key) {
		Object data = redisTemplate.opsForValue().get(key);
		if (data instanceof List<?>) {
			return (List<UpdateConfigOkDetailDto>) data;
		}
		return Collections.emptyList();
	}
}
