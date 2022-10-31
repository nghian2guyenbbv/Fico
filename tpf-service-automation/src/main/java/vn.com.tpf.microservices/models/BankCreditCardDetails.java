package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class BankCreditCardDetails {
    public String bankName;
    public String branchName;
    public String accountNumber;
    public String typeOfAccount;
}
