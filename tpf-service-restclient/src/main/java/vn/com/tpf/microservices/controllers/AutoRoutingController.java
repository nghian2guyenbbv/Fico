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
public class AutoRoutingController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@GetMapping("/tpf-service-autorouting/check_routing")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-autorouting','tpf-service-root','tpf-service-authorization') and hasAnyAuthority('role_root','role_admin')")
	public ResponseEntity<?> check(@RequestHeader("Authorization") String token, @RequestParam Map<String, String> param)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "checkRouting");
		request.put("token", token);
		request.put("param", param);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/tpf-service-autorouting/set_config_routing")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-autorouting','tpf-service-root','tpf-service-authorization') and hasAnyAuthority('role_root','role_admin')")
	public ResponseEntity<?> setRouting(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "setRouting");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/tpf-service-autorouting/get_config_routing/")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-autorouting','tpf-service-root','tpf-service-authorization') and hasAnyAuthority('role_root','role_admin')")
	public ResponseEntity<?> getRouting(@RequestHeader("Authorization") String token, @PathVariable String id,
			@RequestBody JsonNode body) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getRouting");
		request.put("token", token);
		request.put("param", Map.of("id", id));
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@DeleteMapping("/tpf-service-autorouting/log_choice_routing/")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-autorouting','tpf-service-root','tpf-service-authorization') and hasAnyAuthority('role_root')")
	public ResponseEntity<?> logRouting(@RequestHeader("Authorization") String token, @PathVariable String id)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "logRouting");
		request.put("token", token);
		request.put("param", Map.of("id", id));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

}