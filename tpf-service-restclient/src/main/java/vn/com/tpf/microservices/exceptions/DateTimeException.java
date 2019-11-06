package vn.com.tpf.microservices.exceptions;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;

@Getter
public class DateTimeException  extends BaseException {

	private static final long serialVersionUID = 2715363440500672381L;
	private final JsonNode data;
	public DateTimeException(JsonNode data) {
		this.data = data;
	}
} 
