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
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-momo')")
	public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody ObjectNode body)
			throws Exception {
		body.put("referenceId", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "createMomo");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-momo", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/momo/sms/{phone_number}/{sms_result}")
	public ResponseEntity<?> updateSmsResult(@PathVariable String phone_number, @PathVariable String sms_result,
			@RequestParam(required = true) String access_key) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateSmsResult");
		request.put("body", Map.of("requestId", UUID.randomUUID().toString(), "referenceId", UUID.randomUUID().toString(),
				"phone_number", phone_number, "sms_result", sms_result, "access_key", access_key));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-momo", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/momo/{momo_loan_id}/update_status/{status}")
	public ResponseEntity<?> updateStatus(@PathVariable String momo_loan_id, @PathVariable String status,
			@RequestParam(required = true) String access_key) throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateStatus");
		request.put("body", Map.of("requestId", UUID.randomUUID().toString(), "referenceId", UUID.randomUUID().toString(),
				"momo_loan_id", momo_loan_id, "status", status, "access_key", access_key));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-momo", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/momo/{momo_loan_id}/{app_id}/update_automation_result")
	public ResponseEntity<?> updateAutomationResult(@PathVariable String momo_loan_id, @PathVariable String app_id,
			@RequestParam(required = true) String automation_result, @RequestParam(required = true) String access_key)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateAutomationResult");
		request.put("body",
				Map.of("requestId", UUID.randomUUID().toString(), "referenceId", UUID.randomUUID().toString(), "momo_loan_id",
						momo_loan_id, "app_id", app_id, "automation_result", automation_result, "access_key", access_key));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-momo", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

}
