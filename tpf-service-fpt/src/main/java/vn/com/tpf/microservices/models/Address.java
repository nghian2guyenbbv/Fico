package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Address {

	private String addressType;
	private String address1;
	private String address2;
	private String ward;
	private String district;
	private String province;
	private String region;

}