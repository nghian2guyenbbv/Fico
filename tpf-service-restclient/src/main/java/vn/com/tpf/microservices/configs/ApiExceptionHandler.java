package vn.com.tpf.microservices.configs;

import java.util.*;

import javax.validation.ValidationException;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;
import vn.com.tpf.microservices.services.RabbitMQService;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler{

	@Autowired
	private RabbitMQService rabbitMQService;

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<?> exception(Exception ex, WebRequest request) {
		log.error("handling valid Exception ");
		String reference_id = UUID.randomUUID().toString();
		Map<String, Object> requestEror = new HashMap<>();
		requestEror.put("func", "CALLERROR");
		requestEror.put("reference_id", reference_id);

		try {

			rabbitMQService.send("tpf-service-repayment", requestEror);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("request_id", "","reference_id", reference_id, "result_code", 500, "message", "Others error"));
	}

}