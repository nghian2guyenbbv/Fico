package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Data;

@Data
public class CRM_IdentificationsDTO {

    public String identificationType;
    public String identificationNumber;
    public String issuingCountry;
    public String placeOfIssue;
    public String issueDate;
    public String expiryDate;

}
