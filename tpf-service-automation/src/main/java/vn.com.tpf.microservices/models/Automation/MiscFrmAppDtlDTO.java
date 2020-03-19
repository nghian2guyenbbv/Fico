package vn.com.tpf.microservices.models.Automation;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiscFrmAppDtlDTO {
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
}
