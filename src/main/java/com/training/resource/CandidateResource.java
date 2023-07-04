package com.training.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.model.Candidate;
import com.training.model.CreateCandidateRequest;
import com.training.model.UpdateCandidateRequest;
import com.training.service.CandidateService;

@RestController
@RequestMapping("/training/candidates")
public class CandidateResource {
	@Autowired
	private CandidateService candidateservice;
	
	@PostMapping("/createcandidate")
	public ResponseEntity<String>createCandidate(@RequestBody CreateCandidateRequest request)
	{
		return new ResponseEntity<String>(this.candidateservice.createCandidate(request),HttpStatus.OK);
		
	}
	@GetMapping("/getcandidates")
	public ResponseEntity<?>getAllCandidates(@RequestParam(required=false) String searchInput)
	{
		return new ResponseEntity<>(this.candidateservice.getCandidate(searchInput),HttpStatus.OK);
		
	}
	
	@GetMapping("/getcandidatebyid")
	public ResponseEntity<?>getCandidateById(@RequestParam  String id)
	{
		return new ResponseEntity<Candidate>(this.candidateservice.getCandidateById(id),HttpStatus.OK);
		
	}
	
	@PutMapping("/updatecandidate")
	public ResponseEntity<?>updateCandidate(@RequestBody  UpdateCandidateRequest request)
	{
		return this.candidateservice.updateCandidate(request);
		
	}
	
	@DeleteMapping("/deletecandidate")
	public ResponseEntity<?>deleteCandidate(@RequestParam  String id)
	{
		
		return this.candidateservice.deleteCandidate(id);
		
	}

}
