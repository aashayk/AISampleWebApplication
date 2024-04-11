package com.example.resume.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	Logger log = Logger.getLogger(EmailService.class);
	
	@Value("${spring.mail.username}")
	private String from;
	
	public void sendEmail(String subject,String to,String emailMessage) {
		
		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg,"utf-8");
		try {
			helper.setText(emailMessage,true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setFrom(from);
			log.info(subject);
			log.info(to);
			javaMailSender.send(msg);
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
}
