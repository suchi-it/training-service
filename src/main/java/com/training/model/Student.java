package com.training.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.training.consatnts.CollectionConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = CollectionConstants.STUDENT)
public class Student {
@Id	
private String id;	
private String studentName;
private String contactNo;
private String emailId;
private String collegeName;
private String education;
private String state;
private String location;
private String status;
private String comments;

}
