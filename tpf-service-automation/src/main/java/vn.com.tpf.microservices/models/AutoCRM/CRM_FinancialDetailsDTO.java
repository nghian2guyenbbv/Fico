package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_FinancialDetailsDTO {

    public String incomeExpense;
    public String frequency;
    public String amount;
    public String modeOfPayment;
    public String dayOfSalaryPayment;


}
