package com.training.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.training.consatnts.StatusConstants;
import com.training.model.Candidate;
import com.training.model.CreateCandidateRequest;
import com.training.model.UpdateCandidateRequest;
import com.training.service.CandidateService;

import io.micrometer.core.instrument.util.StringUtils;
@Service
public class CandidateServiceImpl implements CandidateService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public String createCandidate(CreateCandidateRequest request) {
		
		Random rand=new Random();
		
		Criteria criteria =new Criteria();
		criteria.orOperator(
		Criteria.where("email").regex(Pattern.compile(request.getEmail(),Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),		
		Criteria.where("phoneNumber").regex(Pattern.compile(request.getPhoneNumber(),Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))		
				);
		
		Query query =new Query(criteria);
		Candidate candidate = this.mongoTemplate.findOne(query, Candidate.class);
		if(candidate==null)
		{
			Candidate newcandidate=new Candidate();
			BeanUtils.copyProperties(request, newcandidate);
			newcandidate.setId(newcandidate.getFirstName().substring(0, 3)+newcandidate.getLastName().substring(0, 3)+rand.nextInt(99));
	newcandidate.setStatus(StatusConstants.PENDING);
	newcandidate.setCreatedAt(new Date(System.currentTimeMillis()));
	this.mongoTemplate.insert(newcandidate);
	return "Candidate Successfully Created with candidate id :"+newcandidate.getId();
		
		}
		else
		return "Candidate Already Exists";
	}
	
	


	@Override
	public List<?> getCandidate(String searchInput) {
		Query query=new Query();
		if(StringUtils.isNotEmpty(searchInput))
		{
			 query = this.getSearchQuery(searchInput);
		}
		List<Candidate> candidates = this.mongoTemplate.find(query, Candidate.class);
		if(!CollectionUtils.isEmpty(candidates))
		{
			return candidates;
		}
		else
		return new ArrayList();
	}

	@Override
	public Candidate getCandidateById(String id) {
Query query=new Query();
if(StringUtils.isNotEmpty(id))
{
	query.addCriteria(Criteria.where("_id").is(id));
	Candidate candidate=this.mongoTemplate.findOne(query, Candidate.class);
	if(candidate!=null)
		return candidate;
	else
		return new Candidate();
}
else
		
		return new Candidate();
	}
	
	@Override
	public ResponseEntity<?> updateCandidate(UpdateCandidateRequest request) {
Query query=new Query();
query.addCriteria(Criteria.where("candidateId").is(request.getCandidateId()));
Candidate candidate=this.mongoTemplate.findOne(query, Candidate.class);
if(candidate!=null)
{
	if(request.getFirstName()!=null)
	{
		candidate.setFirstName(request.getFirstName());
	}
	if(request.getLastName()!=null)
	{
		candidate.setLastName(request.getLastName());
	}
	if(request.getEmail()!=null)
	{
		candidate.setEmail(request.getEmail());
	}
	if(request.getPhoneNumber()!=null)
	{
		candidate.setPhoneNumber(request.getPhoneNumber());
	}
	
	if(request.getAddress()!=null)
	{
		candidate.setAddress(request.getAddress());
	}
	
	if(request.getRole()!=null)
	{
		candidate.setRole(request.getRole());
	}
	
	this.mongoTemplate.save(candidate);
	return new ResponseEntity<>("Candidate"+candidate.getId()+" : is Successfully Updated",HttpStatus.OK);

	}
else

		return new ResponseEntity<>("No Candidate found with Id-"+ request.getCandidateId(),HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> deleteCandidate(String id) {
		
		Query query=new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Candidate candidate = this.mongoTemplate.findOne(query, Candidate.class);
		if(candidate!=null)
		{
			candidate.setStatus(StatusConstants.INACTIVE);
			this.mongoTemplate.save(candidate);
			return new ResponseEntity<>("Candidate "+ id+ " : is successfully deleted",HttpStatus.OK);
		}
		else
		return new ResponseEntity<>("No Candidate found with Id-"+id,HttpStatus.NOT_FOUND);
	}

	@Override
	public Query getSearchQuery(String searchInput) {
Query query=new Query();
List<Criteria> criterias=new LinkedList<>();
Criteria searchCriteria=new Criteria();

searchCriteria.orOperator(
	Criteria.where("_id").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("firstName").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("lastName").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("email").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("address.lane").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("address.street").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("address.city").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("address.state").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("address.country").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("address.zipcode").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("phoneNumber").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)),
	Criteria.where("role").regex(Pattern.compile(searchInput,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));

		criterias.add(searchCriteria);
		if(!CollectionUtils.isEmpty(criterias))
		{
			Criteria criteria=new Criteria();
			criteria.andOperator(criterias.stream().toArray(Criteria[]::new));
			query.addCriteria(criteria);
		}
		return query;
	}

	
	
}
