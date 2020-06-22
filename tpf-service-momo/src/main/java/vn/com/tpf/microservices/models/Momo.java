package vn.com.tpf.microservices.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;



@Data
@Document
@Builder
@JsonInclude(Include.NON_NULL)
public class Momo {

	@Id
	private String id;

	@Indexed(unique = false)
	private String appId;
	@Indexed(unique = true, sparse = true)
	private String momoLoanId;
	@Indexed(unique = false)
	private String phoneNumber;
	private String productCode;
	private String firstName;
	private String middleName;
	private String lastName;
	private String personalId;
	private String issuePlace;
	private String email;
	private String maritalStatus;
	private String gender;
	private String address1;
	private String address2;
	private String ward;
	private String district;
	private String city;
	private String region;
	private String smsResult;
	private String dateOfBirth;
	private String issueDate;
	private String status;
	private String automationResult;
	private String error;
	private long fee;
	private long salary;
	private long amount;
	private long loanTime;
	private long dueDate;
	private boolean insurrance;
	private boolean agree1;
	private boolean agree2;
	private boolean agree3;
	private boolean agree4;

	private Object preChecks;
	@Builder.Default
	private Set<Photo> photos = new HashSet<>();
	@Builder.Default
	private Set<Reference> references = new HashSet<>();

	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

}
