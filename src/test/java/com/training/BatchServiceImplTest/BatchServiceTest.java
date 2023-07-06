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

		ResponseEntity<?> response = batchService.createBatch(request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Batch Already exist", response.getBody());
	}

	@Test
	public void testGetBatches_Success() {
		String searchInput = "Test";

		List<Batch> batches = new ArrayList<>();
		batches.add(new Batch());

		when(mongoTemplate.find(any(), eq(Batch.class))).thenReturn(batches);

		ResponseEntity<?> response = batchService.getBatches(searchInput);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(batches, response.getBody());
	}

	@Test
	public void testGetBatches_NoResults() {
		String searchInput = "Test";

		when(mongoTemplate.find(any(), eq(Batch.class))).thenReturn(new ArrayList<>());

		ResponseEntity<?> response = batchService.getBatches(searchInput);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(CollectionUtils.isEmpty((List<?>) response.getBody()));
	}

	@Test
	public void testGetBatchById_Success() {
		String searchInput = "123";

		List<Batch> batches = new ArrayList<>();
		batches.add(new Batch());

		when(mongoTemplate.find(any(), eq(Batch.class))).thenReturn(batches);

		ResponseEntity<?> response = batchService.getBatchById(searchInput);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(batches, response.getBody());
	}

	@Test
	public void testGetBatchById_NoResults() {
		String searchInput = "123";

		when(mongoTemplate.find(any(), eq(Batch.class))).thenReturn(new ArrayList<>());

		ResponseEntity<?> response = batchService.getBatchById(searchInput);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(CollectionUtils.isEmpty((List<?>) response.getBody()));
	}

	@Test
	public void testUpdateBatch_Success() {
		UpdateBatchRequest request = new UpdateBatchRequest();
		request.setBatchId("123");
		request.setBatchName("Updated Batch");

		Batch batch = new Batch();
		batch.setBatchId("123");
		batch.setBatchName("Test Batch");

		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(batch);

		ResponseEntity<?> response = batchService.updateBatch(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Batch 123 is successfully updated", response.getBody());
		assertEquals("Updated Batch", batch.getBatchName());
	}

	@Test
	public void testUpdateBatch_BatchNotFound() {
		UpdateBatchRequest request = new UpdateBatchRequest();
		request.setBatchId("123");
		request.setBatchName("Updated Batch");

		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(null);

		ResponseEntity<?> response = batchService.updateBatch(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("No Batch found with Id 123", response.getBody());
	}

	@Test
	public void testDeleteBatch_Success() {
		String batchId = "123";

		Batch batch = new Batch();
		batch.setBatchId("123");
		batch.setStatus("ACTIVE");

		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(batch);

		ResponseEntity<?> response = batchService.deleteBatch(batchId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Batch 123 is successfully deleted", response.getBody());
		assertEquals("DELETED", batch.getStatus());
	}

	@Test
	public void testDeleteBatch_BatchNotFound() {
		String batchId = "123";

		when(mongoTemplate.findOne(any(), eq(Batch.class))).thenReturn(null);

		ResponseEntity<?> response = batchService.deleteBatch(batchId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("No Batch found with Id 123", response.getBody());
	}
}
