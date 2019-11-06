package vn.com.tpf.microservices.configs;


import vn.com.tpf.microservices.exceptions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestControllerAdvice
public class ApiExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<?> exception(Exception e, WebRequest request) {
		String reference_id = UUID.randomUUID().toString();
		Map<String, Object> requestEror = new HashMap<>();
		requestEror.put("reference_id", reference_id);
		requestEror.put("error", e.toString());

		ObjectNode dataLog = mapper.createObjectNode();
		dataLog.put("type", "[==HTTP-LOG==]");
		dataLog.set("result", mapper.convertValue(requestEror, JsonNode.class));
		log.error("{}", dataLog);

		return ResponseEntity.status(500)
				.body(Map.of("request_id", "", "reference_id", reference_id, "result_code", 500, "message", "Others error"));
	}
	
	@ExceptionHandler(value = { PGPException.class })
	public ResponseEntity<?> PGPException(Exception e, WebRequest request) {
		String reference_id = UUID.randomUUID().toString();
		Map<String, Object> requestEror = new HashMap<>();
		requestEror.put("reference_id", reference_id);
		requestEror.put("error", e.toString());

		ObjectNode dataLog = mapper.createObjectNode();
		dataLog.put("type", "[==HTTP-LOG==]");
		dataLog.set("result", mapper.convertValue(requestEror, JsonNode.class));
		log.error("{}", dataLog);

		return ResponseEntity.status(500)
				.body(Map.of("request_id", "", "reference_id", reference_id, "result_code", 500, "message", "Others error"));
	}
	
	

}