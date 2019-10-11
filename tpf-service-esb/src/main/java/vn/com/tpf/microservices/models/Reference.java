package vn.com.tpf.microservices.models;

import java.util.Set;

import lombok.Data;

@Data
public class Reference {

	private String name;
	private String relationship;
	private Set<PhoneNumber> phoneNumbers;

}
