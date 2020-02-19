package vn.com.tpf.microservices.models;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "fpt")
@JsonInclude(Include.NON_NULL)
public class Fpt {

	@Id
	private String id;
	@Indexed(unique = true, sparse = true)
	private String appId;
	@Indexed(unique = true, sparse = true)
	private String custId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;
	private String dateOfBirth;
	private String nationalId;
	private String issuePlace;
	private String issueDate;
	private String employeeCard;
	private String maritalStatus;
	private String mobilePhone;
	private String map;
	private String ownerNationalId;
	private String contactAddress;
	private String dsaCode;
	private String companyName;
	private String taxCode;
	private String status;
	private String automationResult;
	private long salary;
	private long durationYear;
	private long durationMonth;
	private LoanDetail loanDetail;
	private Set<Address> addresses;
	private Set<Photo> photos;
	private Set<ProductDetail> productDetails;
	private Set<Reference> references;
	private Set<DocPostApproved> docPostApproved;
	//private Set<Comment> comments;
	private Set<Supplement> supplement;

	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

}