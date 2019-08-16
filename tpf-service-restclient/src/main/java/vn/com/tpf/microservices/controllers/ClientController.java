package vn.com.tpf.microservices.controllers;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class ClientController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@GetMapping("/v1/client")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-authentication') and hasAnyAuthority('role_root')")
	public ResponseEntity<?> reads(@RequestHeader("Authorization") String token, @RequestParam Map<String, String> param)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getListClient");
		request.put("token", token);
		request.put("param", param);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authentication", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/client")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-authentication') and hasAnyAuthority('role_root')")
	public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "createClient");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authentication", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PutMapping("/v1/client/{id}")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-authentication') and hasAnyAuthority('role_root')")
	public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @PathVariable String id,
			@RequestBody JsonNode body) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateClient");
		request.put("token", token);
		request.put("param", Map.of("id", id));
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authentication", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@DeleteMapping("/v1/client/{id}")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-authentication') and hasAnyAuthority('role_root')")
	public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable String id)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "deleteClient");
		request.put("token", token);
		request.put("param", Map.of("id", id));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authentication", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

}