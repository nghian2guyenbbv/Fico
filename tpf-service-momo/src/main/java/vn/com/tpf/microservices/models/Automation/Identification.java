package vn.com.tpf.microservices.models.Automation;

import lombok.Data;

@Data
public class Identification {

    public String identificationType;
    public String identificationNumber;
    public String issuingCountry;
    public String placeOfIssue;
    public String issueDate;
    public String expiryDate;

}
