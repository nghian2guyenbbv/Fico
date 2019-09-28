package vn.com.tpf.microservices.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseMomoDisburse {

	private String requestId;
	private String momoLoanId;
	private String phoneNumber;
	private String description;
	private Long totalAmount;
	private Long actualAmount;
	private String password = "000000";
	private Long createDate = System.currentTimeMillis();
	private ResponseMomoDisburseDetail detail;
	private Set<ResponseMomoDisburseFeeDetail> feeDetail;

}
