package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DynamicForm {
    @NotNull
    @NotEmpty(message = "formName not null")
    private String formName;

    @NotNull
    @NotEmpty(message = "zalo not null")
    private String zalo;

    @NotNull
    @NotEmpty(message = "numberOfDependents not null")
    private String numberOfDependents;

    @NotNull
    @NotEmpty(message = "monthlyRental not null")
    private String monthlyRental;

    @NotNull
    @NotEmpty(message = "houseOwnership not null")
    private String houseOwnership;

    @NotNull
    @NotEmpty(message = "companyName not null")
    private String companyName;

    @NotNull
    @NotEmpty(message = "contractNumber not null")
    private String contractNumber;

    @NotNull
    @NotEmpty(message = "monthlyFee not null")
    private String monthlyFee;

    @NotNull
    @NotEmpty(message = "loanPurpose not null")
    private String loanPurpose;

//    @NotNull
//    @NotEmpty(message = "otherLoanPurposeDetail not null")
    private String otherLoanPurposeDetail;

    @NotNull
    @NotEmpty(message = "newBankCardNumber not null")
    private String newBankCardNumber;

//    @NotNull
//    @NotEmpty(message = "courierCode not null")
    private String courierCode;

    @NotNull
    @NotEmpty(message = "maximumInterestedRate not null")
    private String maximumInterestedRate;

    @NotNull
    @NotEmpty(message = "saleAgentCode not null")
    private String saleAgentCode;

//    @NotNull
//    @NotEmpty(message = "loanAtWork not null")
    private String loanAtWork;

//    @NotNull
//    @NotEmpty(message = "internalCode not null")
    private String internalCode;

//    @NotNull
//    @NotEmpty(message = "totalMonthlyPayable not null")
    private String totalMonthlyPayable;

//    @NotNull
//    @NotEmpty(message = "remark not null")
    private String remark;

}
