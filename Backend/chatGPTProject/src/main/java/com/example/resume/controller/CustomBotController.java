package com.example.resume.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.resume.dto.ChatgptRequest;
import com.example.resume.dto.ChatgptResponse;

@RestController
@RequestMapping("/bot")
public class CustomBotController {
	
	@Value("${openai.model}")
	private String model;
	
	@Value("${openai.api.url}")
	private String apiURL;
	
	@Autowired
	private RestTemplate template;
	
	@GetMapping("/chat")
	public String chat(@RequestParam("prompt") String prompt) {
		ChatgptRequest request = new ChatgptRequest(model, prompt);
		ChatgptResponse response= template.postForObject(apiURL, request, ChatgptResponse.class);
		return response.getChoices().get(0).getMessage().getContent();
	}
}
