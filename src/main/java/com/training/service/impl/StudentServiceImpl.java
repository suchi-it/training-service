package com.training.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.training.consatnts.StatusConstants;
import com.training.model.Batch;
import com.training.model.CreateStudentRequest;
import com.training.model.Status;
import com.training.model.Student;
import com.training.model.UpdateStudentRequest;
import com.training.service.StudentService;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private MongoTemplate mongoTemplate;
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

	@Override
	public ResponseEntity<?> createStudent(CreateStudentRequest request) {
		LOGGER.info("Student Implementatation create successfully");

		if (StringUtils.isEmpty(request.getStudentName())) {
			return new ResponseEntity<>("Student name cannot be empty", HttpStatus.BAD_REQUEST);
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("studentName").is(request.getStudentName()));
		Student findOne = this.mongoTemplate.findOne(query, Student.class);
		if (findOne == null) {
			Student student = new Student();
			BeanUtils.copyProperties(request, student);
			student.setStatus(Status.PENDING.getStatus());
			this.mongoTemplate.save(student);
			LOGGER.info("Student Successfully created");

			return new ResponseEntity<>("Student Created successfully", HttpStatus.OK);
		} else {
			LOGGER.info("Student Alraedy Exists");

			return new ResponseEntity<>("Student already exists", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getStudents(String searchInput) {
		LOGGER.info("Student Implementation getStudents successfully");

		if (StringUtils.isEmpty(searchInput)) {
			return new ResponseEntity<>("Search Input cannot be empty", HttpStatus.BAD_REQUEST);
		}

		Query query = new Query();
		if (StringUtils.isNotEmpty(searchInput)) {
			query = this.getSearchQuery(searchInput);
		}

		query.with(Sort.by(Sort.Direction.ASC, "studentName"));

		List<Student> students = this.mongoTemplate.find(query, Student.class);
		if (!CollectionUtils.isEmpty(students)) {
			return new ResponseEntity<>(students, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getStudentById(String studentName) {

		LOGGER.info("Student Implementation getStudentById successfully");

		if (StringUtils.isEmpty(studentName)) {
			return new ResponseEntity<>("Student name cannot be empty", HttpStatus.BAD_REQUEST);
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("studentName").is(studentName));

		Student student = this.mongoTemplate.findOne(query, Student.class);
		if (student != null) {
			return new ResponseEntity<>(student, HttpStatus.OK);
		}

		else
			return new ResponseEntity<>(new Student(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateStudent(UpdateStudentRequest request) {
		LOGGER.info("Student Implementation Update student successfully");
		if (StringUtils.isEmpty(request.getStudentName())) {
			return new ResponseEntity<>("Student Name cannot be Empty", HttpStatus.BAD_REQUEST);
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("studentName").is(request.getStudentName()));
		Student student = this.mongoTemplate.findOne(query, Student.class);

		if (student != null) {
			if (StringUtils.isNotEmpty(request.getContactNo())) {
				student.setContactNo(request.getContactNo());
			}
			if (StringUtils.isNotEmpty(request.getEmailId())) {
				student.setEmailId(request.getEmailId());
			}
			if (StringUtils.isNotEmpty(request.getCollegeName())) {
				student.setCollegeName(request.getCollegeName());
			}
			if (StringUtils.isNotEmpty(request.getEducation())) {
				student.setEducation(request.getEducation());
			}
			if (StringUtils.isNotEmpty(request.getLocation())) {
				student.setLocation(request.getLocation());
			}
			if (StringUtils.isNotEmpty(request.getState())) {
				student.setState(request.getState());
			}
			if (StringUtils.isNotEmpty(request.getStatus())) {
				student.setStatus(request.getStatus());
			}
			if (StringUtils.isNotEmpty(request.getComments())) {
				student.setComments(request.getComments());
			}

			this.mongoTemplate.save(student);
			LOGGER.info("Student " + student.getStudentName() + " is successfully updated");
			return new ResponseEntity<>("Student " + student.getStudentName() + " is successfully updated ",
					HttpStatus.OK);
		}

		else {
			LOGGER.info("No Student found with this Name " + request.getStudentName());
			return new ResponseEntity<>("No Student found with Name " + request.getStudentName(), HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> deleteStudent(String studentName) {
		LOGGER.info("Student Implementation delete student successfully");
		if (StringUtils.isEmpty(studentName)) {
			return new ResponseEntity<>("studentName Cannot be empty", HttpStatus.BAD_REQUEST);
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("studentName").is(studentName));
Student student = this.mongoTemplate.findOne(query, Student.class);
		if (student != null) {
			student.setStatus("Not Interested");
			this.mongoTemplate.save(student);
			LOGGER.info("Student" + studentName + " is sucessfully deleted");
			return new ResponseEntity<>("Student " + studentName + " is successfully deleted", HttpStatus.OK);
		} else {
			LOGGER.info("No Student found with name" + studentName);
			return new ResponseEntity<>("No Student found with name" + studentName, HttpStatus.OK);
		}
	}

	private Query getSearchQuery(String searchInput) {
		Query query = new Query();
		List<Criteria> criterias = new LinkedList<>();
		Criteria searchCriteria = new Criteria();

		searchCriteria.orOperator(
				Criteria.where("studentName")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("emailId")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("collegeName")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("education")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("state")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("location")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));

		criterias.add(searchCriteria);
		if (!CollectionUtils.isEmpty(criterias)) {
			Criteria criteria = new Criteria();
			criteria.andOperator(criterias.stream().toArray(Criteria[]::new));
			query.addCriteria(criteria);
		}

		return query;

	}
}
