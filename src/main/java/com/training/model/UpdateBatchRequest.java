package com.training.model;

import java.util.Date;

public class UpdateBatchRequest {
	
	private String batchId;
	private String batchName;
	private String course;
	private String trainerName;
	private String trainerEmailId;
	private String trainerContactNo;
	private Date batchStartDate;
	private Date batchEndDate;
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getTrainerName() {
		return trainerName;
	}
	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}
	public String getTrainerEmailId() {
		return trainerEmailId;
	}
	public void setTrainerEmailId(String trainerEmailId) {
		this.trainerEmailId = trainerEmailId;
	}
	public String getTrainerContactNo() {
		return trainerContactNo;
	}
	public void setTrainerContactNo(String trainerContactNo) {
		this.trainerContactNo = trainerContactNo;
	}
	public Date getBatchStartDate() {
		return batchStartDate;
	}
	public void setBatchStartDate(Date batchStartDate) {
		this.batchStartDate = batchStartDate;
	}
	public Date getBatchEndDate() {
		return batchEndDate;
	}
	public void setBatchEndDate(Date batchEndDate) {
		this.batchEndDate = batchEndDate;
	}
	
	

}
