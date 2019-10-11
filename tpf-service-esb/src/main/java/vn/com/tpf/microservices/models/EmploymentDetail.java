package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class EmploymentDetail {

	private String occupationType;
	private String employeeNumber;
	private String employerCode;
	private String natureOfBusiness;
	private String otherCompanyTaxCode;
	private String industry;
	private String employmentType;
	private String employmentStatus;
	private String departmentName;
	private String designation;
	private String yearsInJob;
	private String monthsInJob;
	private String toDate;
	private String fromDate;
	private String employmentLocation;
	private String isMajorEmployment;

}
