package com.example.resume.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.resume.dao.EmployeeDao;
import com.example.resume.entity.EmployeeDetails;
import com.example.resume.exception.AlreadyExistException;
import com.example.resume.exception.ErrorMessage;
import com.example.resume.exception.RecordNotFoundException;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao dao;

	public List<EmployeeDetails> getAllEmployee() {
		List<EmployeeDetails> list = dao.findAll();
		if (list.isEmpty())
			throw new RecordNotFoundException(ErrorMessage.EMPTY_RECORDS);
		return list;
	}

	public EmployeeDetails getByEmail(String email) {
		EmployeeDetails emp = dao.findByEmail(email);
		if (emp == null)
			throw new RecordNotFoundException(ErrorMessage.RECORD_NOT_FOUND);
		return emp;
	}

//	public List<EmployeeDetails> getByName(String firstName, String LastName) {
//		List<EmployeeDetails> listByName = dao.findByFirstNameAndLastName(firstName, LastName);
//		if (listByName.isEmpty())
//			throw new RecordNotFoundException(ErrorMessage.EMPTY_RECORDS);
//		return listByName;
//	}

	public EmployeeDetails submit(EmployeeDetails emp) {
		if (dao.findByEmail(emp.getEmail()) != null)
			throw new AlreadyExistException(ErrorMessage.ALREADY_EXIST);
		return dao.save(emp);

	}
}
