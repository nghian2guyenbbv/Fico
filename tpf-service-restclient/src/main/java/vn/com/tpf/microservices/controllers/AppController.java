package vn.com.tpf.microservices.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class AppController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@GetMapping("/v1/app")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-app')")
	public ResponseEntity<?> getListApp(@RequestHeader("Authorization") String token,
			@RequestParam Map<String, String> param) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getListApp");
		request.put("token", token);
		request.put("param", param);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-app", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/app")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-app')")
	public ResponseEntity<?> createApp(@RequestBody JsonNode body) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "createApp");
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-app", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/v1/app/update_status/{app_id}/{status}")
	public ResponseEntity<?> updateStatus(@PathVariable String app_id, @PathVariable String status,
			@RequestParam(required = true) String access_key) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateStatus");
		request.put("body", Map.of("reference_id", UUID.randomUUID().toString(), "app_id", app_id, "status", status,
				"access_key", access_key));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-app", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PutMapping("/v1/app/{id}")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-app')")
	public ResponseEntity<?> updateApp(@PathVariable String id, @RequestBody JsonNode body) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateApp");
		request.put("param", Map.of("id", id));
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-app", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@GetMapping("/v1/app/{id}")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-app')")
	public ResponseEntity<?> getApp(@PathVariable String id) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getApp");
		request.put("param", Map.of("id", id));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-app", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}



}