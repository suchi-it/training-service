package com.training.service;

import org.springframework.http.ResponseEntity;

public interface TrainingService {
	
	ResponseEntity<?> loginAuthentication(String searchInput, String password);
	String accountApproval(String id,String status);

}
