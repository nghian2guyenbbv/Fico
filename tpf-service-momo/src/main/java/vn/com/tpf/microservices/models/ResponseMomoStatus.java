package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseMomoStatus {

	private String requestId;
	private String momoLoanId;
	private String phoneNumber;
	private Long createdTime = System.currentTimeMillis();
	private Integer status;
	private String description;
	private Integer reasonId;
	private ResponseMomoStatusDetail detail;

}
