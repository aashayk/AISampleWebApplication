package com.example.resume.configuration;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;

@Configuration(proxyBeanMethods = false)
public class GeminiConfig {
	
	@Bean
    public VertexAI vertexAI() throws IOException {
        return new VertexAI("gemini-test-421815", "asia-south1");
    }
	
	@Bean
    public GenerativeModel geminiProVisionGenerativeModel(VertexAI vertexAI) {
        return new GenerativeModel("gemini-1.0-pro-vision", vertexAI);
    }
}
