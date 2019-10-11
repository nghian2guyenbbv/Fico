package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PhoneNumber {
    @NotNull
    @NotEmpty(message = "phoneType not null")
    private String phoneType;

//    @NotNull
//    @NotEmpty(message = "extension not null")
    private String extension;

    @NotNull
    @NotEmpty(message = "isdCode not null")
    private String isdCode;

    @NotNull
    @NotEmpty(message = "phoneNumber not null")
    private String phoneNumber;

    @NotNull
    @NotEmpty(message = "countryCode not null")
    private String countryCode;

//    @NotNull
//    @NotEmpty(message = "stdCode not null")
    private String stdCode;

}
