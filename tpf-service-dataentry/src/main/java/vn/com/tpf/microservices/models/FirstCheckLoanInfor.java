package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class FirstCheckLoanInfor {

    private String amount;
    private String min_amount;
    private String loan_term;
    private String max_loan_term;
}