package vn.com.tpf.microservices.models;

import java.util.Set;

import lombok.Data;

@Data
public class Address {

	private String addressType;
	private String country;
	private String state;
	private String city;
	private String zipcode;
	private String area;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String landMark;
	private String yearsInCurrentAddress;
	private String monthsInCurrentAddress;
	private Set<PhoneNumber> phoneNumbers;

}
