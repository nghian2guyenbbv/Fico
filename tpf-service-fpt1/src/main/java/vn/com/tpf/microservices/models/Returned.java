package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Returned {

	private String code;
	private String commentTpf;
	private String commentFpt;
	private boolean isPending;

}