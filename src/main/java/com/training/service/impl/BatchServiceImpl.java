package com.training.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.slf4j.Logger;
import com.training.model.Batch;
import com.training.model.CreateBatchRequest;
import com.training.model.UpdateBatchRequest;
import com.training.service.BatchService;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class BatchServiceImpl implements BatchService {
	@Autowired
	private MongoTemplate mongoTemplate;
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchServiceImpl.class);

	@Override
	public ResponseEntity<?> createBatch(CreateBatchRequest request) {
		LOGGER.info("Batch Implementatation create successfully");
		if (StringUtils.isEmpty(request.getBatchId())) {
			return new ResponseEntity<>("BatchId cannot be empty", HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(request.getBatchName())) {
			return new ResponseEntity<>("Batch name cannot be empty", HttpStatus.BAD_REQUEST);
		}
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("batchId").is(request.getBatchId()),
				Criteria.where("batchName").is(request.getBatchName()));

		Query query = new Query(criteria);
		Batch findOne = this.mongoTemplate.findOne(query, Batch.class);
		if (findOne == null) {
			Batch batch = new Batch();
			BeanUtils.copyProperties(request, batch);
			this.mongoTemplate.save(batch);
			LOGGER.info("Batch Successfully created with batchId -" + batch.getBatchId());
			return new ResponseEntity<>("Batch Successfully created with batchId -" + batch.getBatchId(),
					HttpStatus.OK);

		} else {
			LOGGER.info("Batch Already Exist");
			return new ResponseEntity<>("Batch Already exist", HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> getBatches(String searchInput) {
		LOGGER.info("Batch Implementation getBatches successfully");

		if (StringUtils.isEmpty(searchInput)) {
			return new ResponseEntity<>("Search Input cannot be empty", HttpStatus.BAD_REQUEST);
		}
		Query query = new Query();
		if (StringUtils.isNotEmpty(searchInput)) {
			query = this.getSearchQuery(searchInput);
		}

		query.with(Sort.by(Sort.Direction.DESC, "batchStartDate"));

		List<Batch> batches = this.mongoTemplate.find(query, Batch.class);
		if (!CollectionUtils.isEmpty(batches)) {
			return new ResponseEntity<>(batches, HttpStatus.OK);
		}

		else
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getBatchById(String searchInput1) {
		LOGGER.info("Batch Implementation getBatchById successfully");
		if (StringUtils.isEmpty(searchInput1)) {
			return new ResponseEntity<>("Search Input cannot be empty", HttpStatus.BAD_REQUEST);
		}
		Query query = new Query();
		if (StringUtils.isNotEmpty(searchInput1)) {
			query = this.getSearchQueryById(searchInput1);
		}

		query.with(Sort.by(Sort.Direction.DESC, "batchStartDate"));
		List<Batch> batch = this.mongoTemplate.find(query, Batch.class);
		if (!CollectionUtils.isEmpty(batch)) {
			return new ResponseEntity<>(batch, HttpStatus.OK);
		}

		else
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> updateBatch(UpdateBatchRequest request) {
		LOGGER.info("Batch Implementation Update Batch successfully");
		if (StringUtils.isEmpty(request.getBatchId())) {
			return new ResponseEntity<>("BatchId cannot be Empty", HttpStatus.BAD_REQUEST);
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("batchId").is(request.getBatchId()));
		Batch batch = this.mongoTemplate.findOne(query, Batch.class);

		if (batch != null) {
			if (StringUtils.isNotEmpty(request.getBatchName())) {
				batch.setBatchName(request.getBatchName());
			}
			if (StringUtils.isNotEmpty(request.getCourse())) {
				batch.setCourse(request.getCourse());
			}
			if (StringUtils.isNotEmpty(request.getTrainerName())) {
				batch.setTrainerName(request.getTrainerName());
			}
			if (StringUtils.isNotEmpty(request.getTrainerEmailId())) {
				batch.setTrainerEmailId(request.getTrainerEmailId());
			}
			if (StringUtils.isNotEmpty(request.getTrainerContactNo())) {
				batch.setTrainerContactNo(request.getTrainerContactNo());
			}
			if (request.getBatchStartDate() != null) {
				batch.setBatchStartDate(request.getBatchStartDate());
			}
			if (request.getBatchEndDate() != null) {
				batch.setBatchEndDate(request.getBatchEndDate());
			}

			this.mongoTemplate.save(batch);
			LOGGER.info("Batch " + batch.getBatchId() + " is successfully updated");
			return new ResponseEntity<>("Batch " + batch.getBatchId() + " is successfully updated ", HttpStatus.OK);
		}

		else {
			LOGGER.info("No Batch found with Id " + request.getBatchId());
			return new ResponseEntity<>("No Batch found with Id " + request.getBatchId(), HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> deleteBatch(String batchId) {
		LOGGER.info("batch Implementation delete batch successfully");
		if (StringUtils.isEmpty(batchId)) {
			return new ResponseEntity<>("BatchId Cannot be empty", HttpStatus.BAD_REQUEST);
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("batchId").is(batchId));
		Batch batch = this.mongoTemplate.findOne(query, Batch.class);

		if (batch != null) {
			batch.setStatus("DELETED");
			this.mongoTemplate.save(batch);
			LOGGER.info("Batch" + batchId + " is sucessfully deleted");
			return new ResponseEntity<>("Batch " + batchId + " is successfully deleted", HttpStatus.OK);
		} else {
			LOGGER.info("No Batch found with Id" + batchId);
			return new ResponseEntity<>("No Batch found with Id" + batchId, HttpStatus.OK);
		}
	}

	private Query getSearchQuery(String searchInput) {
		Query query = new Query();
		List<Criteria> criterias = new LinkedList<>();
		Criteria searchCriteria = new Criteria();

		searchCriteria.orOperator(
				Criteria.where("batchId")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("batchName")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("course")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("trainerName")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("trainerEmailId")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("trainerContactNo")
						.regex(Pattern.compile(searchInput, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));

		criterias.add(searchCriteria);
		if (!CollectionUtils.isEmpty(criterias)) {
			Criteria criteria = new Criteria();
			criteria.andOperator(criterias.stream().toArray(Criteria[]::new));
			query.addCriteria(criteria);
		}

		return query;

	}

	private Query getSearchQueryById(String searchInput1) {
		Query query = new Query();
		List<Criteria> criterias = new LinkedList<>();
		Criteria searchCriteria = new Criteria();

		searchCriteria.orOperator(
				Criteria.where("batchId")
						.regex(Pattern.compile(searchInput1, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
				Criteria.where("batchName")
						.regex(Pattern.compile(searchInput1, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));

		criterias.add(searchCriteria);
		if (!CollectionUtils.isEmpty(criterias)) {
			Criteria criteria = new Criteria();
			criteria.andOperator(criterias.stream().toArray(Criteria[]::new));
			query.addCriteria(criteria);
		}

		return query;

	}

}
