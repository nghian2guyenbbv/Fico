package vn.com.tpf.microservices.models;

import java.util.Set;

import lombok.Data;

@Data
public class CommunicationDetail {

	private String primaryAddress;
	private String primaryEmailId;
	private Set<PhoneNumber> phoneNumbers;

}
