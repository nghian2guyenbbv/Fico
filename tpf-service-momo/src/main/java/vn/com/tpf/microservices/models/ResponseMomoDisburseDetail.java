package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseMomoDisburseDetail {

	private String loanId;
	private Integer tenor;
	private Long rate;
	private String disbursementDate;
	private String maturityDate;
	private Integer dueDate;
	private Long emi;
	private String paymentBankAccount;
	private String firstInstallmentDate;
	private Long firstInstallmentAmount;

}
