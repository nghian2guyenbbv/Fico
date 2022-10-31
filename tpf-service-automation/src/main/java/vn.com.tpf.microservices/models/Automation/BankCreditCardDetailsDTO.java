package vn.com.tpf.microservices.models.Automation;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCreditCardDetailsDTO {
    private String bankName;
    private String branchName;
    private String accountNumber;
    private String typeOfAccount;
}
