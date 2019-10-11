package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class SourcingDetail {

	private String productCode;
	private String schemeCode;
	private String loanAmountRequested;
	private String requestedTenure;
	private String loanApplicationType;
	private String chassisApplicationNum;
	private String loanPurposeDesc;
	private String saleAgentCode;

}
