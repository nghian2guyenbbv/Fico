package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class Identification {

    public String identificationType;
    public String identificationNumber;
    public String issuingCountry;
    public String placeOfBirth;
    public String issueDate;
    public String expiryDate;

}
