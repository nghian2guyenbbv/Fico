package vn.com.tpf.microservices.models;

import java.util.Set;

import lombok.Data;

@Data
public class PersonalInformation {

	private PersonalInfo personalInfo;
	private Set<Identification> identifications;
	private Set<Address> addresses;
	private CommunicationDetail communicationDetails;
	private Set<Family> family;

}
