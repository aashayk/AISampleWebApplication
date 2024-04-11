package com.example.resume.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.resume.entity.EmployeeDetails;

@Repository
public interface EmployeeDao extends JpaRepository<EmployeeDetails, Long>{

}
