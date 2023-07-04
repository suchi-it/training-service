package com.training.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.model.CreateStudentRequest;
import com.training.model.UpdateBatchRequest;
import com.training.model.UpdateStudentRequest;
import com.training.service.StudentService;

@RestController
@RequestMapping("/training/students")
public class StudentResource {
	@Autowired
	private StudentService studentService;
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentResource.class);

	@PostMapping("/createstudent")
	public ResponseEntity<?> createStdent(@RequestBody CreateStudentRequest request) {
		LOGGER.info("Student is successfully Created");
		return this.studentService.createStudent(request);
	}

	@GetMapping("/getstudents")
	public ResponseEntity<?> getStudents(@RequestParam(required = false) String searchInput) {
		LOGGER.info("Get Students successfully");
		return this.studentService.getStudents(searchInput);
	}

	@GetMapping("/getstudent")
	public ResponseEntity<?> getStudent(@RequestParam String studentName) {
		LOGGER.info("Get Student successfully");
		return this.studentService.getStudentById(studentName);
	}

	@PutMapping("/updatestudent")
	public ResponseEntity<?> updateStudent(@RequestBody UpdateStudentRequest request) {
		LOGGER.info("Update Student successfully");
		return this.studentService.updateStudent(request);
	}
	

	@DeleteMapping("/deletestudent")
	public ResponseEntity<?> deleteStudent(@RequestParam String studentName) {
		LOGGER.info("Delete Student successfully");
		return this.studentService.deleteStudent(studentName);

	}

}

