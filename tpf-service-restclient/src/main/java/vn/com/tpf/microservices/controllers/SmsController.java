package vn.com.tpf.microservices.controllers;



import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class SmsController {

	@Autowired
	private RabbitMQService rabbitMQService;


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

}

