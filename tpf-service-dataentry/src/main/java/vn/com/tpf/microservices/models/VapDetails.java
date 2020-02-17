package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class VapDetails {
//    @NotNull
//    @NotEmpty(message = "vapProduct not null")
    private String vapProduct;

//    @NotNull
//    @NotEmpty(message = "vapTreatment not null")
    private String vapTreatment;

//    @NotNull
//    @NotEmpty(message = "insuranceCompany not null")
    private String insuranceCompany;

}
