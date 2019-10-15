package vn.com.tpf.microservices.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class MomoController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@PostMapping("/momo")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-momo','3p-service-momo')")
	public ResponseEntity<?> createMomo(@RequestHeader("Authorization") String token,
			@RequestHeader("Content-Type") String contentType, @RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "createMomo");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-momo", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/momo/sms/{phone_number}/{sms_result}")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-momo','tpf-service-sms')")
	public ResponseEntity<?> updateSms(@RequestHeader("Authorization") String token, @PathVariable String phone_number,
			@PathVariable String sms_result) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateSms");
		request.put("token", token);
		request.put("body",
				Map.of("reference_id", UUID.randomUUID().toString(), "phone_number", phone_number, "sms_result", sms_result));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-momo", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/momo/{transaction_id}/update_automation/{app_id}")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-momo')")
	public ResponseEntity<?> updateAutomation(@RequestHeader("Authorization") String token,
			@PathVariable String transaction_id, @PathVariable String app_id,
			@RequestParam(required = true) String automation_result) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateAutomation");
		request.put("token", token);
		request.put("body", Map.of("reference_id", UUID.randomUUID().toString(), "transaction_id", transaction_id, "app_id",
				app_id, "automation_result", automation_result));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-momo", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/momo/{transaction_id}/update_status/{status}")
	public ResponseEntity<?> updateStatus(@PathVariable String transaction_id, @PathVariable String status,
			@RequestParam(required = true) String access_key) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateStatus");
		request.put("body", Map.of("reference_id", UUID.randomUUID().toString(), "transaction_id", transaction_id, "status",
				status, "access_key", access_key));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-momo", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

}
