package com.training.service;

import org.springframework.http.ResponseEntity;

import com.training.model.CreateBatchRequest;
import com.training.model.UpdateBatchRequest;

public interface BatchService {
	
	ResponseEntity<?> createBatch (CreateBatchRequest request);
	
	ResponseEntity<?> getBatches(String searchInput);
	
	ResponseEntity<?> getBatchById(String searchInput1);

	ResponseEntity<?> updateBatch(UpdateBatchRequest request);
	
	ResponseEntity<?> deleteBatch(String batchId);


	
}
