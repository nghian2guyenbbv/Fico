package vn.com.tpf.microservices.controllers;



import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.services.RabbitMQService;
import vn.com.tpf.microservices.utils.Utils;

@RestController
public class SmsController {

	@Autowired
	private RabbitMQService rabbitMQService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Autowired
	private Utils  utils;


	@PostMapping("/sms/add_tpfsms_customer")
	public ResponseEntity<?> reviceSms(@RequestBody ObjectNode body,
			@RequestParam(required = true) String access_key) throws Exception {
		body.put("access_key",access_key);
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "receiveSms");
		request.put("body",body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-sms", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data").textValue());
	}
	
	@SuppressWarnings("deprecation")
	@PostMapping("/sms/send")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-sms')")
	public ResponseEntity<?> sendSms(@RequestBody ObjectNode body) throws Exception {
		
		body.put("reference_id", UUID.randomUUID().toString());
		if(body.isNull() || body.path("data").isNull())
			return  ResponseEntity.status(200).body(utils.getJsonNodeResponse(1, body, objectMapper.createObjectNode().put("message", "body.data required")));	
		body.put("phone", body.path("data").path("phone"));
		body.put("content", body.path("data").path("content"));
		Map<String, Object> request = new HashMap<>();
		request.put("func", "sendSms");
		request.put("body",body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-sms", request);
		return  ResponseEntity.status(response.path("status").asInt(500)).body(utils.getJsonNodeResponse(response.path("resultCode").asInt(), body, response.path("data")));	
	}

}

