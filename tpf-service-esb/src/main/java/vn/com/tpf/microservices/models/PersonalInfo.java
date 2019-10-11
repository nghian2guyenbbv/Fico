package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class PersonalInfo {

	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	private String gender;
	private String dateOfBirth;
	private String nationality;
	private String maritalStatus;
	private String customerCategoryCode;

}
