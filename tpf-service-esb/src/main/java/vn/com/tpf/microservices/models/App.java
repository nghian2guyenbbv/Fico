package vn.com.tpf.microservices.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class App {

	private String requestId;
	private String referenceId;
	private String momoLoanId;
	private String phoneNumber;
	private String productCode;
	private String firstName;
	private String middleName;
	private String lastName;
	private String personalId;
	private String issuePlace;
	private String email;
	private String maritalStatus;
	private String status;
	private String gender;
	private String address1;
	private String address2;
	private String ward;
	private String district;
	private String city;
	private String region;
	private String smsResult;
	private String automationResult;
	private String dateOfBirth;
	private String issueDate;
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
	private Set<Photo> photos = new HashSet<>();
	private Set<Reference> references = new HashSet<>();

}
