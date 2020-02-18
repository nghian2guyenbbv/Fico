

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
public class SmartnetController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@PostMapping("/smartnet")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-fpt','3p-service-fpt')")
	public ResponseEntity<?> createFpt(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "createFpt");
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-fpt", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

}
