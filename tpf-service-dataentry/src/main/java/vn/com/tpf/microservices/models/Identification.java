package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Identification {

    @NotNull
    @NotEmpty(message = "identificationType not null")
    private String identificationType;

    @NotNull
    @NotEmpty(message = "identificationNumber not null")
    private String identificationNumber;

//    @NotNull
//    @NotEmpty(message = "issuingCountry not null")
    private String issuingCountry;

//    @NotNull
//    @NotEmpty(message = "placeOfBirth not null")
    private String placeOfBirth;

//    @NotNull
//    @NotEmpty(message = "issueDate not null")
    private String issueDate;

//    @NotNull
//    @NotEmpty(message = "expiryDate not null")
    private String expiryDate;

}
