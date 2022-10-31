package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_DynamicFormDTO {

    public String formName;
    public String loanPurpose;
    public String loanPurposeOther;
    public String numberOfDependents;
    public String monthlyRental;
    public String houseOwnership;
    public String newBankCardNumber;
    public String saleAgentCode;
    public String courierCode;
    public String maximumInterestedRate;
    public String zalo;
    public String companyName;
    public String contractNumber;
    public String monthlyFee;
    public String otherLoanPurposeDetail;
    public String loanAtWork;
    public String loanOfWork;
    public String internalCode;
    public String totalMonthlyPayable;
    public String remark;
    public String oldContractLoanAmount;

}
