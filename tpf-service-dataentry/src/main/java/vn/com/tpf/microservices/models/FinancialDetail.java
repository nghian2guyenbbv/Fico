package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class FinancialDetail {
    @NotNull
    @NotEmpty(message = "incomeExpense not null")
    private String incomeExpense;

    @NotNull
    @NotEmpty(message = "amount not null")
    private String amount;

//    @NotNull
//    @NotEmpty(message = "modeOfPayment not null")
    private String modeOfPayment;

//    @NotNull
//    @NotEmpty(message = "dayOfSalaryPayment not null")
    private String dayOfSalaryPayment;

}