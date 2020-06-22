package vn.com.tpf.microservices.models.Automation;

import lombok.Data;

@Data
public class LoanDetails {

    public SourcingDetails sourcingDetails;
    public VapDetails vapDetails;

}
