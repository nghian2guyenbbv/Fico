package vn.com.tpf.microservices.models.Automation;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDetailsDTO {
    private String branch;
    private String channel;
    private String applicationNumber;
    private String loanAppType;
    private String productName;
    private String scheme;
    private String loanAmount;
    private String loanTerm;
    private String saleAgentCode;

    private String loadPurpose;
}
