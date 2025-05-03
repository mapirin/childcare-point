package com.childcare.point.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/line")
public class LineBotRestController {
	
	@PostMapping("/callback")
	public ResponseEntity<String> handleMessage(@RequestBody String requestBody){
		
		return ResponseEntity.ok("Message Received");
	}
	
}
