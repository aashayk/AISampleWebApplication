package com.example.resume.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.resume.dao.EmployeeDao;
import com.example.resume.entity.EmployeeDetails;
import com.example.resume.services.EmployeeService;

@RestController
public class FileUploadController {

	@Value("${spring.servlet.multipart.location}")
	private String uploadDir;

	@Autowired
	private EmployeeService service;

	@Autowired
	private EmployeeDao dao;

	@PostMapping("/upload/{email}")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
			@PathVariable String email) {
		EmployeeDetails emp = service.getByEmail(email);
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String filePath = Paths.get(uploadDir, fileName).toString();
		try {
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
			emp.setResumeFilePath(filePath);
			dao.save(emp);
			return new ResponseEntity<>("File Uploaded", HttpStatus.ACCEPTED);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong", HttpStatus.OK);
		}
	}
	
	@GetMapping("/download/{email}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String email){
		EmployeeDetails emp  = service.getByEmail(email);
		if(emp.getResumeFilePath()==null)
			return ResponseEntity.notFound().build();
		
		Path filePath = Paths.get(emp.getResumeFilePath());
		Resource resource;
		try {
			resource = new UrlResource(filePath.toUri()); 
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; fileName=" + resource.getFilename())
				.body(resource);
	}
	
	
}
