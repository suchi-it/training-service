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
import com.training.model.CreateBatchRequest;
import com.training.model.UpdateBatchRequest;
import com.training.service.BatchService;
import com.training.service.impl.BatchServiceImpl;

@RestController
@RequestMapping("/training/batches")
public class BatchResource {
	@Autowired
	private BatchService batchservice;
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchResource.class);

	@PostMapping("/createbatch")
	public ResponseEntity<?> createBatch(@RequestBody CreateBatchRequest request) {
		LOGGER.info("Batch is successfully Created");
		return this.batchservice.createBatch(request);

	}

	@GetMapping("/getbatches")
	public ResponseEntity<?> getBatches(@RequestParam(required = false) String searchInput) {
		LOGGER.info("Get Batches successfully");
		return this.batchservice.getBatches(searchInput);
	}

	@GetMapping("/getbatchById")
	public ResponseEntity<?> getBatchById(@RequestParam(required = false) String searchInput1) {
		LOGGER.info("Get BatchById successfully");
		return this.batchservice.getBatchById(searchInput1);
	}

	@PutMapping("/updatebatch")
	public ResponseEntity<?> updateBatch(@RequestBody UpdateBatchRequest request) {
		LOGGER.info("Update Batch successfully");
		return this.batchservice.updateBatch(request);
	}

	@DeleteMapping("/deletebatch")
	public ResponseEntity<?> deleteBatch(@RequestParam String batchId) {
		LOGGER.info("Delete Batch successfully");
		return this.batchservice.deleteBatch(batchId);

	}
}
