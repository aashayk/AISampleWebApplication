package com.example.resume.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gemini")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GeminiProVisionController {

	public final GenerativeModel generativeModel;
	
	@PostMapping
	public String file(@RequestParam("file") MultipartFile file, @RequestParam String question) throws IOException {
		GenerateContentResponse generateContentResponse = this.generativeModel.generateContent(ContentMaker
				.fromMultiModalData(PartMaker.fromMimeTypeAndData(file.getContentType(), file.getBytes()), question));
		return ResponseHandler.getText(generateContentResponse);
	}

}
