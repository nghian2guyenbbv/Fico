package vn.com.tpf.microservices.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class MobilityFinnOneFiled {

	@Id
	private String id;

	private Object noOfAttempts;
	
	private Object verificationAgent;
	
	private Object resonDecisionFic;
	
	private Object resultDecisionFiv;
	
	private Object result2ndHomeVisit;
	
	private Object resultOfficeVisit;
	
	private Object resultHomeVisit; 

	private Object phoneConfirmed;
	
	private Object decisionFic;
	
	private Object filesUpload;
	
	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;
}