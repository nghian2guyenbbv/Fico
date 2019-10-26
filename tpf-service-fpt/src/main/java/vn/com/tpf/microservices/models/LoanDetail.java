package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class LoanDetail {

	private String loanId;
	private String product;
	private double emi;
	private double annualr;
	private double downPayment;
	private long tenor;
	private long dueDate;
	private long loanAmount;

}