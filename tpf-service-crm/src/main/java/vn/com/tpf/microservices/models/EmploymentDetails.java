package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class EmploymentDetails {
    @NotNull
    @NotEmpty(message = "occupationType not null")
    private String occupationType;

    @NotNull
    @NotEmpty(message = "employeeNumber not null")
    private String employeeNumber;

    @NotNull
    @NotEmpty(message = "employerCode not null")
    private String employerCode;

    @NotNull
    @NotEmpty(message = "natureOfBusiness not null")
    private String natureOfBusiness;

    @NotNull
//    @NotEmpty(message = "otherCompanyTaxCode not null")
    private String otherCompanyTaxCode;

//    @NotNull
//    @NotEmpty(message = "industry not null")
    private String industry;

    @NotNull
//    @NotEmpty(message = "employmentType not null")
    private String employmentType;

    @NotNull
    @NotEmpty(message = "employmentStatus not null")
    private String employmentStatus;

//    @NotNull
//    @NotEmpty(message = "departmentName not null")
    private String departmentName;

//    @NotNull
//    @NotEmpty(message = "designation not null")
    private String designation;

//    @NotNull
//    @NotEmpty(message = "yearsInJob not null")
    private String yearsInJob;

//    @NotNull
//    @NotEmpty(message = "monthsInJob not null")
    private String monthsInJob;

    //    @NotNull
//    @NotEmpty(message = "toDate not null")
    private String toDate;

    //    @NotNull
//    @NotEmpty(message = "fromDate not null")
    private String fromDate;

    @NotNull
    @NotEmpty(message = "employmentLocation not null")
    private String employmentLocation;

    @NotNull
//    @NotEmpty(message = "isMajorEmployment not null")
    private String isMajorEmployment;

    @NotNull
//    @NotEmpty(message = "remarks not null")
    private String remarks;

    @NotNull
//    @NotEmpty(message = "totalYearsInOccupation not null")
    private String totalYearsInOccupation;

    @NotNull
//    @NotEmpty(message = "totalMonthsInOccupation not null")
    private String totalMonthsInOccupation;

    private String natureOfOccupation;

    public EmploymentDetails() {
        if (natureOfOccupation == null || natureOfOccupation.equals("")){
            this.natureOfOccupation = "";
        }
    }

}
