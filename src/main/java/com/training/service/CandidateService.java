package com.training.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;

import com.training.model.Candidate;
import com.training.model.CreateCandidateRequest;
import com.training.model.UpdateCandidateRequest;

public interface CandidateService {
	
	public String createCandidate(CreateCandidateRequest request);
	
	public List<?> getCandidate(String searchInput);
	
	public Candidate getCandidateById(String id);
	
	public ResponseEntity<?> updateCandidate(UpdateCandidateRequest request);
	
	public ResponseEntity<?> deleteCandidate(String id);
	
	public Query getSearchQuery(String searchInput);

}
