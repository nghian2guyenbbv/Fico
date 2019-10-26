package vn.com.tpf.microservices.controllers;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class PGPController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@PostMapping("/v1/pgp/encrypt")
	public ResponseEntity<?> pgpEncrypt(@RequestBody JsonNode body) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "pgpEncrypt");
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-assets", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/v1/pgp/decrypt")
	public ResponseEntity<?> pgpDecrypt(@RequestBody JsonNode body) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "pgpDecrypt");
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-assets", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

}