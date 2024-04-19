package com.example.resume.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.resume.dao.EmployeeDao;
import com.example.resume.entity.EmployeeDetails;
import com.example.resume.exception.AlreadyExistException;
import com.example.resume.exception.RecordNotFoundException;
@SpringBootTest
class EmployeeServiceTest {
	
	@MockBean
	private EmployeeDao dao;
	
	@Autowired
	private EmployeeService service;
	
	Logger log = Logger.getLogger(EmployeeServiceTest.class);
	
	int id = 100;
	String firstName = "Unit";
	String lastName = "Tester";
	Short age = 25;
	String email = "tester@gmail.com";
	String mobile = "9988998899";
	String designation = "Software Engineer";
	EmployeeDetails emp;
	List<EmployeeDetails> listEmp;
	
	@BeforeEach
	void setup() {
		emp = new EmployeeDetails(id, firstName, lastName, age, email, mobile, designation);
		listEmp = new ArrayList<>();
		listEmp.add(emp);
	}

	@Test
	void testGetAllEmployee() {
		when(dao.findAll()).thenReturn(listEmp);
		assertEquals(service.getAllEmployee(), listEmp);
	}
	
	@Test
	void testGetAllEmployeeEmptyRecords() {
		List<EmployeeDetails> emptyList = new ArrayList<>();
		when(dao.findAll()).thenReturn(emptyList);
		assertThrows(RecordNotFoundException.class,()->service.getAllEmployee());
	}

	@Test
	void testGetByEmail() {
		when(dao.findByEmail(email)).thenReturn(emp);
		assertEquals(service.getByEmail(email), emp);
	}
	
	@Test
	void testGetByEmailRecordNotFound() {
		when(dao.findByEmail("abc@gmail.com")).thenReturn(null);
		assertThrows(RecordNotFoundException.class, ()->service.getByEmail("abc@gmail.com"));
	}

	@Test
	void testSubmit() {
		when(dao.findByEmail(email)).thenReturn(null);
		when(dao.save(emp)).thenReturn(emp);
		assertEquals(service.submit(emp), emp);
	}
	
	@Test
	void testSubmitAlreadyExist() {
		when(dao.findByEmail(email)).thenReturn(emp);
		assertThrows(AlreadyExistException.class, ()->service.submit(emp));
	}

}
