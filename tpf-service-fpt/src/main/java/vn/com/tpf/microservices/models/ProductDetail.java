package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ProductDetail {

	private String model;
	private String goodCode;
	private String goodType;
	private long goodPrice;
	private long quantity;

}