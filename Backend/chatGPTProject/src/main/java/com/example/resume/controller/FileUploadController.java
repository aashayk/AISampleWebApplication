package com.example.resume.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.resume.dao.EmployeeDao;
import com.example.resume.entity.EmployeeDetails;
import com.example.resume.exception.BadRequestException;
import com.example.resume.services.EmailService;
import com.example.resume.services.EmployeeService;

@RestController
@CrossOrigin("*")
public class FileUploadController {

	@Value("${spring.servlet.multipart.location}")
	private String uploadDir;
	Logger log = Logger.getLogger(FileUploadController.class);
	@Autowired
	private EmployeeService service;

	@Autowired
	private EmployeeDao dao;
	
	@Value("${gemini.question}")
	private String question;
	
	@Value("${mail.to}")
	private String to;
	
	@Autowired
	private GeminiProVisionController gemini;
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/upload/{email}")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String email)
			throws Exception {
		log.info(email);
		EmployeeDetails emp = service.getByEmail(email);
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if(!fileName.endsWith(".pdf"))
			throw new BadRequestException("Only pdf file allowed");
		String filePath = Paths.get(uploadDir, fileName).toString();
		log.info(filePath);
		try {
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
			emp.setResumeFilePath(filePath);
			dao.save(emp);
//			String summary = generateSummary(email);
			String summary = gemini.file(file,question);
//			String summary = "lorel ipsum";
			sendResume(emp, summary);
			return new ResponseEntity<>(summary, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong", HttpStatus.OK);
		}
	}
	
	private void sendResume(EmployeeDetails emp,String summary) {
		String[] passages = summary.split("\n");
		StringBuilder sb = new StringBuilder();
		sb.append("<p>");
		for (int i=0;i<passages.length;i++) {
			sb.append(passages[i]);
			if(i<passages.length-1) {
				sb.append("<br>");
			}
		}
		sb.append("</p>");
		summary=sb.toString();
		String message = "<p style='background-color: lemonchiffon;'>A candidate has submitted application. Please review it.<br/><b> Applicant Name: "
							+ emp.getFirstName()+ " "+ emp.getLastName()
							+"<br/>Age:"+ emp.getAge()
							+"<br/> Years of Experience: "+emp.getYearsOfExperience() 
							+"<br/> Email Address: "+emp.getEmail() 
							+"<br/> Mobile Number: "+emp.getMobile() 
							+"<br/> Designation: "+emp.getDesignation() 
							+"</b><br/><h3>Summary of Candidate's resume :<h3/>"
							+"<p/>"
							+summary;
		String subject = "Job Application of "+emp.getFirstName()+" "+emp.getLastName()+ ", "+LocalDate.now();
		emailService.sendEmail(subject, to, message);
	}
	

//	@GetMapping("/summary/{email}")
//	public String generateSummary(@PathVariable String email) throws Exception {
//		try {
//			EmployeeDetails emp = service.getByEmail(email);
//			if (emp == null) {
//				log.error("Employee details not found for email: " + email);
//				return "Employee details not found";
//			}
//			log.info("Employee email: " + emp.getEmail());
//			String filePath = emp.getResumeFilePath();
//			log.info("Resume file path: " + filePath);
//
//			String text = extractTextFromFile(filePath);
////	        Properties props = new Properties();
////	        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment,coref,natlog,openie");
////	        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//			StringBuilder summary = new StringBuilder();
//			for (Sentence sent : new Document(text).sentences()) {
//				summary.append(sent.text()).append("\n");
//			}
//			return summary.toString();
//		} catch (IOException e) {
//			log.error("Failed to generate summary", e);
//			return "Failed to generate summary";
//		}
//	}
//
//	private String extractTextFromFile(String filePath) throws IOException {
//		try (InputStream inputStream = new FileInputStream(new File(filePath))) {
//			if (filePath.endsWith(".docx")) {
//				XWPFDocument doc = new XWPFDocument(inputStream);
//				XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
//				return extractor.getText();
//			} else if (filePath.endsWith(".pdf")) {
//				try (PDDocument doc = PDDocument.load(new File(filePath))) {
//					PDFTextStripper stripper = new PDFTextStripper();
//					return stripper.getText(doc);
//				}
//			} else {
//				return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//			}
//		}
//	}

	@GetMapping("/download/{email}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String email) {
		EmployeeDetails emp = service.getByEmail(email);
		if (emp.getResumeFilePath() == null)
			return ResponseEntity.notFound().build();

		Path filePath = Paths.get(emp.getResumeFilePath());
		Resource resource;
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + resource.getFilename())
				.body(resource);
	}

}