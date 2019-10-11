package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class Identification {

	private String identificationType;
	private String identificationNumber;
	private String issuingCountry;
	private String placeOfBirth;
	private String issueDate;
	private String expiryDate;

}
