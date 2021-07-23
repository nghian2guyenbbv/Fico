package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class BankingDetail {
    private String bankName;
    private String branchName;
    private String accountNumber;
    private String typeOfAccount;
}
