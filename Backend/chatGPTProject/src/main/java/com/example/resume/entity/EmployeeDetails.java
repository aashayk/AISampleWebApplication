package com.example.resume.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetails {
	@Id
	@GeneratedValue
	private long id;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;
	
	@NotNull
	@Min(21)
	@Max(60)
	private short age;
	
	@NotNull
	@Min(2)
	private short yearsOfExperience;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	@Pattern(regexp = "^[0-9]{10}$")
	private String mobile;
	
	@NotNull
	private String designation;
	
	private String resumeFilePath;

}
