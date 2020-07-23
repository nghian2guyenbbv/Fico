package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_EmploymentDetailsDTO {

    public String occupationType;
    public String employeeNumber;
    public String employerCode;
    public String natureOfBusiness;
    public String otherCompanyTaxCode;
    public String industry;
    public String employmentType;
    public String employmentStatus;
    public String departmentName;
    public String designation;
    public String yearsInJob;
    public String monthsInJob;
    public String totalYearsInOccupation;
    public String totalMonthsInOccupation;
    public String toDate;
    public String fromDate;
    public String employmentLocation;
    public String isMajorEmployment;
    public String remarks;
    public String natureOfOccupation;

}
