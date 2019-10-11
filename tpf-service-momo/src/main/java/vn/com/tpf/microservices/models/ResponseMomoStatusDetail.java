package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseMomoStatusDetail {

	private Integer tenor;
	private Long amount;
	private Long emi;
	private Integer dueDate;
	private String applicationId;
	private String disbursementDate;

}
