package vn.com.tpf.microservices.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.services.AutoAssignService;
import vn.com.tpf.microservices.services.RabbitMQService;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AutoAssignController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RabbitMQService rabbitMQService;

	private RestTemplate restTemplate;

	@Autowired
	private AutoAssignService autoAssignService;

	@PostConstruct
	private void init() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate = new RestTemplate(factory);
	}

	@PostMapping("/getallvendor")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> getAllVendor(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
//	public ResponseEntity<?> getAllVendor(@RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> response = autoAssignService.getVendor();
		return ResponseEntity.status(200)
				.header("x-pagination-total", "0").body(response.get("data"));
	}

	@PostMapping("/getConfigInDay")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
//	public ResponseEntity<?> addProduct(@RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> response = autoAssignService.getConfigInDay(body);
		return ResponseEntity.status(200)
				.header("x-pagination-total", "0").body(response.get("data"));
	}

	@PostMapping("/createdConfigure")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> createdConfigure(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
//	public ResponseEntity<?> createdConfigure(@RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> response = autoAssignService.createdConfigure(body);

		return ResponseEntity.status(200)
				.header("x-pagination-total", "0").body(response.get("data"));
	}

	@PostMapping("/updatedConfigure")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> updatedConfigure(@RequestHeader("Authorization") String token, @RequestBody ObjectNode body)
//	public ResponseEntity<?> updatedConfigure(@RequestBody JsonNode body)
			throws Exception {

		body.put("token", token);
		Map<String, Object> response = autoAssignService.updatedConfigure(body);
		return ResponseEntity.status(200)
				.header("x-pagination-total", "0").body(response.get("data"));
	}

	@PostMapping("/configureApplication")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root', '3p-service-mobility')")
	public ResponseEntity<?> configureApplication(@RequestHeader("Authorization") String token, @RequestBody ObjectNode body)
//	public ResponseEntity<?> configureApplication(@RequestBody ObjectNode body)
			throws Exception {
		String reqId = body.get("request_id").textValue();
		String refId = UUID.randomUUID().toString();
		body.put("reference_id", refId);
		Map<String, Object> responseConfig = autoAssignService.configureApplication(body);

		body.put("reference_id", refId);
		Map<String, Object> request = new HashMap<>();
		request.put("func", "addDocuments");
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive(responseConfig.get("vendorQueue").toString(), request);
		return ResponseEntity.status(response.path("status").asInt(200)).body(response.path("data"));
	}

	@PostMapping("/configureVendor")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root', '3p-service-mobility')")
	public ResponseEntity<?> configureVendor(@RequestHeader("Authorization") String token, @RequestBody ObjectNode body)
//	public ResponseEntity<?> configureVendor(@RequestBody ObjectNode body)
			throws Exception {
		String reqId = body.get("request_id").textValue();
		String refId = UUID.randomUUID().toString();
		body.put("reference_id", refId);
		Map<String, Object> responseConfig = autoAssignService.configureApplication(body);

		return ResponseEntity.status(200).body(Map.of("result_code", 0, "request_id", reqId,"reference_id", refId, "date_time", new Timestamp(new Date().getTime()),
				"data", Map.of("vendorName", responseConfig.get("vendorName"), "vendorId", responseConfig.get("vendorId"), "routingId", responseConfig.get("routingId"))));
	}

	@GetMapping("/gethistory")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> getHistory(@RequestHeader("Authorization") String token,@RequestParam Map<String, String> param)
			throws Exception {
		Map<String, Object> response = autoAssignService.getHistory(param);
		return ResponseEntity.status(200)
				.header("x-pagination-total", "0").body(response.get("data"));
	}

	@GetMapping("/getquickreport")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root')")
	public ResponseEntity<?> getQuickReport(@RequestHeader("Authorization") String token,@RequestParam Map<String, String> param)
			throws Exception {
		Map<String, Object> response = autoAssignService.getQuickReport();
		return ResponseEntity.status(200)
				.header("x-pagination-total", "0").body(response.get("data"));
	}

}