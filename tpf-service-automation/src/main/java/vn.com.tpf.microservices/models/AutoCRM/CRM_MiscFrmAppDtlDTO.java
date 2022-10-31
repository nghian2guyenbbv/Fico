package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_MiscFrmAppDtlDTO {

    private String loanPurpose;
    private String numOfDependents;
    private String houseOwnership;
    private String mortgagePaymentCost;
    private String newBankCardNumber;
    private String salesAgentCode;
    private String maxRequestRate;
    private String totalMonthlyPayable;
    private String remark;
    private String loanOfWork;
    private String courierCode;
    private String oldContractLoanAmount;
    private String contractNumber;
    private String zalo;
    private String companyName;
    private String monthlyFee;

}
