package vn.com.tpf.microservices.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.tpf.microservices.services.RabbitMQService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AutoAllocationController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@PostMapping("/tpf-service-autoallocation/check-routing")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> check(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "checkRouting");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-autoallocation", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/tpf-service-autoallocation/set-config-routing")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> setRouting(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "setRouting");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-autoallocation1", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@GetMapping("/tpf-service-autoallocation/get-config-routing/")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> getRouting(@RequestHeader("Authorization") String token) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getRouting");
		request.put("token", token);
		//request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-autoallocation1", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/tpf-service-autoallocation/log-choice-routing/")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root') ")
	public ResponseEntity<?> logRouting(@RequestHeader("Authorization") String token,  @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "logRouting");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-autoallocation", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/tpf-service-autoallocation/get-history-config/")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> getHistoryConfig(@RequestHeader("Authorization") String token, @RequestBody JsonNode body) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "historyConfig");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-autoallocation1", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@GetMapping("/tpf-service-autoallocation/get-assign-config/")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> setAssignConfig(@RequestHeader("Authorization") String token) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "assignConfig");
		request.put("token", token);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-autoallocation", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}


	@PostMapping("/tpf-service-autoallocation/set-assign-config/")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> getAssignConfig(@RequestHeader("Authorization") String token, @RequestBody JsonNode body) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "setAssignConfig");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-autoallocation", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}


}