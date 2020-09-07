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
public class CrmController {

	@Autowired
	private RabbitMQService rabbitMQService;
	
	@Value("${spring.url.uploadfile}")
	private String urlUploadfile;
	
	@PostMapping("/crm/preCheck1")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> preCheck1(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "preCheck1");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/crm/preCheck2")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> preCheck2(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "preCheck2");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/crm/getAppInfo")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> getAppInfo(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getAppInfo");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/crm/addDocuments")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	ResponseEntity<?> addDocuments(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "addDocuments");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/crm/addDocumentsWithCustId")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	ResponseEntity<?> addDocumentsWithCustId(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "addDocumentsWithCustId");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/crm/addDocumentsWithCustIdAndFullApp")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	ResponseEntity<?> addDocumentsWithCustIdAndFullApp(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "addDocumentsWithCustIdAndFullApp");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/crm/returnQuery")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> returnQuery(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "returnQuery");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/crm/returnQueue")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-crm')")
	public ResponseEntity<?> returnQueue(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "returnQueue");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/crm/createField")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-crm')")
	public ResponseEntity<?> createField(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "createField");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	@PostMapping("/crm/waiveField")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-crm')")
	public ResponseEntity<?> waiveField(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "waiveField");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	
	@PostMapping("/crm/submitField")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-crm')")
	public ResponseEntity<?> submitField(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "submitField");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	@PostMapping("/crm/returnQueueAndSendBack")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-crm')")
	public ResponseEntity<?> returnQueueAndSendBack(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "returnQueueAndSendBack");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-crm", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
}
