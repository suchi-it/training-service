package com.training.service;

import org.springframework.http.ResponseEntity;

import com.training.model.CreateStudentRequest;
import com.training.model.UpdateStudentRequest;

public interface StudentService {

ResponseEntity<?> createStudent (CreateStudentRequest request);
	
	ResponseEntity<?> getStudents(String searchInput);
	
	ResponseEntity<?> getStudentById(String studentName);

	ResponseEntity<?> updateStudent(UpdateStudentRequest request);
	
	ResponseEntity<?> deleteStudent(String studentName);
}
