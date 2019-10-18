package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class FirstCheckLoanOffer {

    private String scheme_name;
    private String interest_rate;
    private String monthly_installment_amount;
}