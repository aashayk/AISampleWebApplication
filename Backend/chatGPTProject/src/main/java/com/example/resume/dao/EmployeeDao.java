package com.example.resume.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.resume.entity.EmployeeDetails;

@Repository
public interface EmployeeDao extends JpaRepository<EmployeeDetails, Long> {

	public EmployeeDetails findByEmail(String email);

//	public List<EmployeeDetails> findByFirstNameAndLastName(String firstName, String lastName);
}
