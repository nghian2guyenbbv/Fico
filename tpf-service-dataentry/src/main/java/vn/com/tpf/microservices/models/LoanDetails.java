package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.Valid;

@Data
public class LoanDetails {
    @Valid
    private SourcingDetails sourcingDetails;

//    @Valid
    private VapDetails vapDetails;

}
