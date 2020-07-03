package vn.com.tpf.microservices.models.Automation;

import lombok.Data;

@Data
public class FinancialDetail {

    public String incomeExpense;
    public String amount;
    public String modeOfPayment;
    public String dayOfSalaryPayment;

}