package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SourcingDetails {
    @NotNull
    @NotEmpty(message = "productCode not null")
    private String productCode;

    @NotNull
    @NotEmpty(message = "schemeCode not null")
    private String schemeCode;

    @NotNull
    @NotEmpty(message = "loanAmountRequested not null")
    private String loanAmountRequested;

    @NotNull
    @NotEmpty(message = "requestedTenure not null")
    private String requestedTenure;

    @NotNull
    @NotEmpty(message = "loanApplicationType not null")
    private String loanApplicationType;

    @NotNull
    @NotEmpty(message = "chassisApplicationNum not null")
    private String chassisApplicationNum;

//    @NotNull
//    @NotEmpty(message = "loanPurposeDesc not null")
    private String loanPurposeDesc;

    @NotNull
    @NotEmpty(message = "saleAgentCode not null")
    private String saleAgentCode;

}
