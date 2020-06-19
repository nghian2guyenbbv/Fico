package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Data;

@Data
public class CRM_SourcingDetailsDTO {

    public String sourcingChannel;
    public String sourcingBranch;
    public String applicationNumber;
    public String loanApplicationType;
    public String productCode;
    public String schemeCode;
    public String loanAmountRequested;
    public String requestedTenure;
    public String chassisApplicationNum;
    public String loanPurposeDesc;
    public String saleAgentCode;

}
