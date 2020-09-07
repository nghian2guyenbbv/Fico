

package vn.com.tpf.microservices.controllers;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	

	
	@Value("${spring.url.uploadfile}")
	private String urlUploadfile;
	

	@PostMapping("/smartnet/preCheck1")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> preCheck1(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "preCheck1");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/smartnet/preCheck2")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> preCheck2(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "preCheck2");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/smartnet/getAppInfo")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> getAppInfo(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getAppInfo");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/smartnet/addDocuments")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	ResponseEntity<?> addDocuments(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "addDocuments");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	
	@PostMapping("/smartnet/returnQuery")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> returnQuery(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "returnQuery");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	
	@PostMapping("/smartnet/returnQueue")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> returnQueue(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "returnQueue");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	
	


}