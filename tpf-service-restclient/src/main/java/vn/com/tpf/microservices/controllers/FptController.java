package vn.com.tpf.microservices.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class FptController {

	@Autowired
	private RabbitMQService rabbitMQService;
	

	@PostMapping("/fpt")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-fpt','3p-service-fpt')")
	public ResponseEntity<?> createFpt(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "createFpt");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-fpt", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	
	@GetMapping("/fpt")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-fpt','3p-service-fpt')")
	public ResponseEntity<?> getAllFpt() throws Exception {
		
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getAllFpt");
	
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-fpt", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@GetMapping("/fpt/{cus_id}")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-fpt','3p-service-fpt')")
	public ResponseEntity<?> getbyFpt(@PathVariable String cus_id) throws Exception {
		
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getbyFpt");
		request.put("body",cus_id);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-fpt", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PutMapping("/fpt/{cus_id}/docPostApproved")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-fpt','3p-service-fpt')")
	public ResponseEntity<?> adddocPostApproved(@PathVariable String cus_id,@RequestBody ObjectNode body) throws Exception {
		
		Map<String, Object> request = new HashMap<>();
		request.put("func", "adddocPostApproved");
		request.put("body",Map.of("cus_id", cus_id,"data",body));
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-fpt", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	
	@PutMapping("/fpt/{cus_id}/appid")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-fpt','3p-service-fpt')")
	public ResponseEntity<?> addfieldvalue(@PathVariable String cus_id,@RequestBody ObjectNode body) throws Exception {
		
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updatedappid");
		request.put("body",Map.of("cus_id", cus_id,"data",body));
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-fpt", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	
	@PostMapping("/fpt/addCommentFromFpt")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-fpt','3p-service-fpt')")
	public ResponseEntity<?> addCommentFromFpt(@RequestBody ObjectNode body) throws Exception {
		
		Map<String, Object> request = new HashMap<>();
		request.put("func", "addCommentFromFpt");
		request.put("body",body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-fpt", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	@PostMapping("/fpt/addCommentFromTPBank")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-fpt','3p-service-fpt')")
	public ResponseEntity<?> addCommentFromTpBank(@RequestBody ObjectNode body) throws Exception {
		
		Map<String, Object> request = new HashMap<>();
		request.put("func", "addCommentFromTpBank");
		request.put("body",body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-fpt", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}
	
	
	
	
	
	

}
