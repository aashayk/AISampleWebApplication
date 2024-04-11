package com.example.resume.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resume.entity.EmployeeDetails;
import com.example.resume.services.EmployeeService;

@RestController
@RequestMapping("/emp")
@CrossOrigin("*")
public class EmployeeController {

	@Autowired
	private EmployeeService service;
	
	@GetMapping("/getAll")
	public ResponseEntity<List<EmployeeDetails>> getAllEmployees(){
		return new ResponseEntity<>(service.getAllEmployee(),HttpStatus.OK);
	}
	
}
