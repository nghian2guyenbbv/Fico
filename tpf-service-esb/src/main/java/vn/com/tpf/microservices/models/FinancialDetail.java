package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class FinancialDetail {

	private String incomeExpense;
	private String amount;
	private String modeOfPayment;
	private String dayOfSalaryPayment;

}
