package com.example.resume.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.resume.dao.EmployeeDao;
import com.example.resume.entity.EmployeeDetails;
import com.example.resume.exception.ErrorMessage;
import com.example.resume.exception.RecordNotFoundException;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao dao;
	
	public List<EmployeeDetails> getAllEmployee(){
		List<EmployeeDetails> list = dao.findAll();
		if(list.isEmpty())
			throw new RecordNotFoundException(ErrorMessage.EMPTY_RECORDS);
		return list;
	}
}
