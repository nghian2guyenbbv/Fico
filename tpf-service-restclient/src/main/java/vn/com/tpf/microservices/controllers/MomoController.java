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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class MomoController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@PostMapping("/momo")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-momo','3p-service-momo')")
	public ResponseEntity<?> createMomo(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "createMomo");
		request.put("body", body);

		ObjectNode response = (ObjectNode) rabbitMQService.sendAndReceive("tpf-service-momo", request);

		if (response.path("status").asInt() != 0) {
			response.with("data").put("message", "Other Error");
		}
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/momo/sms/{phone_number}/{sms_result}")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-momo')")
	public ResponseEntity<?> updateSms(@PathVariable String phone_number, @PathVariable String sms_result)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateSms");
		request.put("body",
				Map.of("reference_id", UUID.randomUUID().toString(), "phone_number", phone_number, "sms_result", sms_result));

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-momo", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

}
