package vn.com.tpf.microservices.models.Automation;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO implements Serializable {
	private String occupationType;
	private String taxCode;
	private String natureOfBussiness;
	private String industry;
	private String department;
	private String level;
	private String employmentStatus;
	private String employmentType;
	private String durationYears;
	private String durationMonths;
	private String natureOfOccupation;
	public String totalYearsInOccupation;
	public String totalMonthsInOccupation;
	public String isMajorEmployment;
	private String remarks;
}
