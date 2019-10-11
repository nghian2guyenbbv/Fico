package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class PhoneNumber {

	private String phoneType;
	private String extension;
	private String isdCode;
	private String phoneNumber;
	private String countryCode;
	private String stdCode;
}
