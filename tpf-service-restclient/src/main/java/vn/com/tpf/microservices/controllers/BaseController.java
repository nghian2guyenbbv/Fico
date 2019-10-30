package vn.com.tpf.microservices.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class BaseController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@GetMapping("/")
	public ResponseEntity<?> hello(@RequestParam Map<String, String> param) {
		return ResponseEntity.ok().body(param);
	}

	@PostMapping("/v1/login")
	public ResponseEntity<?> login(@RequestBody JsonNode body) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "login");
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@GetMapping("/v1/logout")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-authentication')")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "logout");
		request.put("token", token);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authentication", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/v1/change-password")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-authentication')")
	public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "changePassword");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authentication", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@GetMapping("/v1/me")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-authorization')")
	public ResponseEntity<?> me(@RequestHeader("Authorization") String token) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getInfoAccount");
		request.put("token", token);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

}