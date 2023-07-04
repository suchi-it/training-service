package com.training.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.training.consatnts.CollectionConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = CollectionConstants.BATCH)

public class Batch {
	@Id
	private String id;
	@Indexed(unique = true)
	@NotNull
	private String batchId;
	private String batchName;
	private String course;
	private String trainerName;
	private String trainerEmailId;
	private String trainerContactNo;
	private Date batchStartDate;
	private Date batchEndDate;
	private String status;

}
