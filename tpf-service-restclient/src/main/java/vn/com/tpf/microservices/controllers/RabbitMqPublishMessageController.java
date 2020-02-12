package vn.com.tpf.microservices.controllers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController()
public class RabbitMqPublishMessageController {
	

	@Autowired
	private RabbitMQService rabbitMQService;
	
	
	
	@PostMapping("/v1/rabbitmq/publish-message")
	public ResponseEntity<?> submit(@RequestBody ObjectNode body) throws Exception { 
		
		Assert.isTrue(body.path("func").isTextual() && !body.path("func").asText().isEmpty(),
				"func is required and not empty");	
		Assert.isTrue(body.path("service").isTextual() && !body.path("service").asText().isEmpty(),
				"service is required and not empty");	
		Map<String, Object> request = new HashMap<>();
		request.put("func", body.get("func").asText().trim());
		request.put("body", body.get("body"));
		request.put("param", body.get("param"));
		JsonNode receive  = (ObjectNode) rabbitMQService.sendAndReceive(body.path("service").asText(), request);

		Map<String, Object> response = new HashMap<>(); 
		response.put("request_id", body.path("request_id").asText());
		response.put("reference_id", UUID.randomUUID().toString());
		response.put("date_time",ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		
		if(receive.path("status").asInt() == 200) {
			response.put("result_code", 0);
			response.put("data", receive.get("data"));
		}
		else {
			response.put("result_code", 500);
			response.put("message", receive.get("message").asText());	
		}

		return ResponseEntity.status(receive.path("status").asInt(500)).body(response);
	}
}
