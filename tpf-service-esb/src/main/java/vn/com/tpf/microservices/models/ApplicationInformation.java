package vn.com.tpf.microservices.models;

import java.util.Set;

import lombok.Data;

@Data
public class ApplicationInformation {

	private PersonalInformation personalInformation;
	private EmploymentDetail employmentDetails;
	private Set<FinancialDetail> financialDetails;

}
