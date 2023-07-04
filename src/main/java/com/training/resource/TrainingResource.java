package com.training.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.model.LoginRequest;
import com.training.service.TrainingService;

@RestController
@RequestMapping("/training/candidates")
public class TrainingResource {
	@Autowired
	private TrainingService trainingService;
	
@PostMapping("/login")	
public ResponseEntity<?> login(@RequestBody LoginRequest request)
{
	System.out.println(request.getSearchInput());
	
	return this.trainingService.loginAuthentication(request.getSearchInput(), request.getPassword());
	
}
@GetMapping("/statusapproval/{id}/{status}")
public String accountApproval(@PathVariable String id,@PathVariable String status)
{
	return this.trainingService.accountApproval(id, status);
	
}
}
