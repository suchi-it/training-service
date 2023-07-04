package com.training.model;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.consatnts.CollectionConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = CollectionConstants.CANDIDATES)
public class Candidate {
	@Id
	private String  id;
	private String firstName;
	private String lastName;
	
	@Indexed(unique = true)
	@NotNull
	private String email;
	private String phoneNumber;
	private Address address;
	@CreatedDate
	private Date createdAt;
	@LastModifiedBy
	private String lastModifiedDate;
	@JsonIgnore
	private String password;
	private String status;
	private Set<String> role;
	

}
