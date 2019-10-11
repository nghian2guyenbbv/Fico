package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationInformation {

    public PersonalInfomation personalInformation;
    public EmploymentDetails employmentDetails;
    public List<FinancialDetail> financialDetails = null;

}
