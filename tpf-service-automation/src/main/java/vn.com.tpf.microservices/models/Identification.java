package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class Identification {

    public String identificationType;
    public String identificationNumber;
    public String issuingCountry;
    public String placeOfIssue;
    public String issueDate;
    private String expiryDate;

    public void setExpiryDate(String expiryDate) {
        if (expiryDate == null || expiryDate.isEmpty())
            expiryDate = "";
        this.expiryDate = expiryDate;
    }
}
