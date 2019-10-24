package vn.com.tpf.microservices.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class TrustingSocialController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@PostMapping("/trustingsocial/first-check")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-trustingsocial','3p-service-trustingsocial')")
	public ResponseEntity<?> firstCheck(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "firstCheckTrustingSocial");
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-trustingsocial", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/trustingsocial/lead")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-trustingsocial','3p-service-trustingsocial')")
	public ResponseEntity<?> create(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "createTrustingSocial");
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-trustingsocial", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/trustingsocial/lead/resubmit-document-info")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-trustingsocial','3p-service-trustingsocial')")
	public ResponseEntity<?> update(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateTrustingSocial");
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-trustingsocial", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

}
