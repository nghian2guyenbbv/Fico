package vn.com.tpf.microservices.models.Automation;

import lombok.Data;

@Data
public class SourcingDetails {

    public String productCode;
    public String schemeCode;
    public String loanAmountRequested;
    public String requestedTenure;
    public String loanApplicationType;
    public String chassisApplicationNum;
    public String loanPurposeDesc;
    public String saleAgentCode;

}
