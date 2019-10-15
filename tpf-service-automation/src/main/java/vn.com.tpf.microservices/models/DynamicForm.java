package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class DynamicForm {

    public String formName;
    public String zalo;
    public String numberOfDependents;
    public String monthlyRental;
    public String houseOwnership;
    public String companyName;
    public String contractNumber;
    public String monthlyFee;
    public String loanPurpose;
    public String otherLoanPurposeDetail;
    public String newBankCardNumber;
    public String courierCode;
    public String maximumInterestedRate;
    //public OldContractNumber oldContractNumber;
    public String saleAgentCode;
    public String loanAtWork;
    public String internalCode;
    public String totalMonthlyPayable;
    public String remark;

}
