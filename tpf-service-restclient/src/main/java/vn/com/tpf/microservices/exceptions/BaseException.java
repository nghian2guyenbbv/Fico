package vn.com.tpf.microservices.exceptions;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 769368879146577553L;
	
	
	protected BaseException() {
		super();
	}

	protected BaseException(String message) {
		super(message);
	
	}

	protected BaseException(String entity, Long id) {
	
	}
}