package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Address {
//    @NotNull
//    @NotEmpty(message = "addressType not null")
    private String addressType;

//    @NotNull
//    @NotEmpty(message = "country not null")
    private String country;

//    @NotNull
//    @NotEmpty(message = "state not null")
    private String state;

//    @NotNull
//    @NotEmpty(message = "city not null")
    private String city;

//    @NotNull
//    @NotEmpty(message = "zipcode not null")
    private String zipcode;

//    @NotNull
//    @NotEmpty(message = "area not null")
    private String area;

//    @NotNull
//    @NotEmpty(message = "addressLine1 not null")
    private String addressLine1;

//    @NotNull
//    @NotEmpty(message = "addressLine2 not null")
    private String addressLine2;

//    @NotNull
//    @NotEmpty(message = "addressLine3 not null")
    private String addressLine3;

//    @NotNull
//    @NotEmpty(message = "landMark not null")
    private String landMark;

//    @NotNull
//    @NotEmpty(message = "yearsInCurrentAddress not null")
    private String yearsInCurrentAddress;

//    @NotNull
//    @NotEmpty(message = "monthsInCurrentAddress not null")
    private String monthsInCurrentAddress;

//    @Valid
    private List<PhoneNumber> phoneNumbers;

}