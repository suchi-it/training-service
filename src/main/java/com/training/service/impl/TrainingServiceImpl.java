package com.training.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.UpdateResult;
import com.training.consatnts.StatusConstants;
import com.training.model.Candidate;
import com.training.service.TrainingService;

import io.micrometer.core.instrument.util.StringUtils;
@Service
public class TrainingServiceImpl implements TrainingService {
	@Autowired
	private MongoTemplate mongoTemplet;

	@Override
	public ResponseEntity<?> loginAuthentication(String searchInput, String password) {
		if (StringUtils.isEmpty(searchInput) || (StringUtils.isEmpty(password))) {
			return new ResponseEntity<>("Email or Password should not be blank", HttpStatus.BAD_REQUEST);
		}
		Criteria criteria = new Criteria();
		criteria.orOperator(Criteria.where("email").is(searchInput), Criteria.where("phoneNumber").is(searchInput),
				Criteria.where("_id").is(searchInput));
		Query query = new Query();

		query.addCriteria(criteria);
		query.addCriteria(Criteria.where("status").is(StatusConstants.APPROVED));
		if (this.mongoTemplet.count(query, Candidate.class) == 0) {
			return new ResponseEntity<>("Email does not Exist", HttpStatus.BAD_REQUEST);
		}
		query.addCriteria(Criteria.where("password").is(password));
		Candidate candidate = this.mongoTemplet.findOne(query, Candidate.class);
		if (candidate != null) {
			return new ResponseEntity<>("Login Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid Credentials", HttpStatus.FORBIDDEN);
		}
	}

	@Override
	public String accountApproval(String id, String status) {

		System.out.println(status);
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update updateDefi = new Update().set("status", status);
		UpdateResult updateFirst = mongoTemplet.updateFirst(query, updateDefi, Candidate.class);
		System.out.println(updateFirst.getMatchedCount());
		String msg = "";
		if (updateFirst != null) {
			msg = "Candidate Status Approved" + id;
		} else {
			msg = "Candidate Status is Rejected" + id;
		}
		return msg;
	}

}
