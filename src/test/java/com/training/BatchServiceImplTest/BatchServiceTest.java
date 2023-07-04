package com.training.BatchServiceImplTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import com.training.model.Batch;
import com.training.model.CreateBatchRequest;
import com.training.model.UpdateBatchRequest;
import com.training.service.impl.BatchServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceTest {

	@InjectMocks
	private BatchServiceImpl batchService;

	@Mock
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateBatch_Success() {

		CreateBatchRequest request = new CreateBatchRequest();
		request.setBatchId("123");
		request.setBatchName("Test Batch");
		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(null);
		ResponseEntity<?> response = batchService.createBatch(request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Batch Successfully created with batchId - 123", response.getBody());
	}

	@Test
	public void testCreateBatch_BatchExists() {
		CreateBatchRequest request = new CreateBatchRequest();
		request.setBatchId("123");
		request.setBatchName("Test Batch");

		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(new Batch());

		// Call the createBatch method
		ResponseEntity<?> response = batchService.createBatch(request);

		// Verify the response
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Batch Already exist", response.getBody());
	}

	@Test
	public void testGetBatches_Success() {
		// Create a mock search input
		String searchInput = "Test";

		// Create a mock list of batches
		List<Batch> batches = new ArrayList<>();
		batches.add(new Batch());

		// Mock the find method
		when(mongoTemplate.find(any(), eq(Batch.class))).thenReturn(batches);

		// Call the getBatches method
		ResponseEntity<?> response = batchService.getBatches(searchInput);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(batches, response.getBody());
	}

	@Test
	public void testGetBatches_NoResults() {
		// Create a mock search input
		String searchInput = "Test";

		// Mock the find method with an empty list
		when(mongoTemplate.find(any(), eq(Batch.class))).thenReturn(new ArrayList<>());

		// Call the getBatches method
		ResponseEntity<?> response = batchService.getBatches(searchInput);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(CollectionUtils.isEmpty((List<?>) response.getBody()));
	}

	@Test
	public void testGetBatchById_Success() {
		// Create a mock search input
		String searchInput = "123";

		// Create a mock list of batches
		List<Batch> batches = new ArrayList<>();
		batches.add(new Batch());

		// Mock the find method
		when(mongoTemplate.find(any(), eq(Batch.class))).thenReturn(batches);

		// Call the getBatchById method
		ResponseEntity<?> response = batchService.getBatchById(searchInput);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(batches, response.getBody());
	}

	@Test
	public void testGetBatchById_NoResults() {
		// Create a mock search input
		String searchInput = "123";

		// Mock the find method with an empty list
		when(mongoTemplate.find(any(), eq(Batch.class))).thenReturn(new ArrayList<>());

		// Call the getBatchById method
		ResponseEntity<?> response = batchService.getBatchById(searchInput);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(CollectionUtils.isEmpty((List<?>) response.getBody()));
	}

	@Test
	public void testUpdateBatch_Success() {
		// Create a mock request
		UpdateBatchRequest request = new UpdateBatchRequest();
		request.setBatchId("123");
		request.setBatchName("Updated Batch");

		// Create a mock batch
		Batch batch = new Batch();
		batch.setBatchId("123");
		batch.setBatchName("Test Batch");

		// Mock the findOne method
		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(batch);

		// Call the updateBatch method
		ResponseEntity<?> response = batchService.updateBatch(request);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Batch 123 is successfully updated", response.getBody());
		assertEquals("Updated Batch", batch.getBatchName());
	}

	@Test
	public void testUpdateBatch_BatchNotFound() {
		// Create a mock request
		UpdateBatchRequest request = new UpdateBatchRequest();
		request.setBatchId("123");
		request.setBatchName("Updated Batch");

		// Mock the findOne method with null
		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(null);

		// Call the updateBatch method
		ResponseEntity<?> response = batchService.updateBatch(request);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("No Batch found with Id 123", response.getBody());
	}

	@Test
	public void testDeleteBatch_Success() {
		// Create a mock batchId
		String batchId = "123";

		// Create a mock batch
		Batch batch = new Batch();
		batch.setBatchId("123");
		batch.setStatus("ACTIVE");

		// Mock the findOne method
		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(batch);

		// Call the deleteBatch method
		ResponseEntity<?> response = batchService.deleteBatch(batchId);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Batch 123 is successfully deleted", response.getBody());
		assertEquals("DELETED", batch.getStatus());
	}

	@Test
	public void testDeleteBatch_BatchNotFound() {
		// Create a mock batchId
		String batchId = "123";

		// Mock the findOne method with null
		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(null);

		// Call the deleteBatch method
		ResponseEntity<?> response = batchService.deleteBatch(batchId);

		// Verify the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("No Batch found with Id 123", response.getBody());
	}
}
