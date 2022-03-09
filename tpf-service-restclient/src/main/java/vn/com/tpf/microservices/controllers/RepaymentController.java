package vn.com.tpf.microservices.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.tpf.microservices.services.RabbitMQService;

import java.util.HashMap;
import java.util.Map;

//import vn.com.tpf.microservices.configs.RepaymentException;

@RestController
public class RepaymentController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@PostMapping("/repayment/add-payment")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root','tpf-service-momo','3p-service-momo')")
	public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "customers_pay");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@GetMapping("/repayment")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root','tpf-service-momo','3p-service-momo')")
	public ResponseEntity<?> reads(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getCustomers");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);

		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/repayment/importTrans")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root')")
	public ResponseEntity<?> importTrans(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "importTrans");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/repayment/settleTrans")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root')")
	public ResponseEntity<?> settleTrans(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "settleTrans");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/repayment/getListTrans")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root')")
	public ResponseEntity<?> getListTrans(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getListTrans");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/repayment/getReport")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root')")
	public ResponseEntity<?> getReport(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getReport");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/repayment/getTransDate")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root')")
	public ResponseEntity<?> getTransDate(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getTransDate");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/repayment/syncData")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root')")
	public ResponseEntity<?> syncData(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "syncData");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

//	@GetMapping("/repayment/customers_pay")
//	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment')")
//	public ResponseEntity<?> reads2(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
//			throws Exception {
//		Map<String, Object> request = new HashMap<>();
//		request.put("func", "getCustomers_pay");
//		request.put("token", token);
//		request.put("body", body);
//
//		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
//		return ResponseEntity.status(response.path("status").asInt(500))
//				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
//	}

	@PostMapping("/repayment/vn-post/add-payment")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root','tpf-service-momo','3p-service-momo','3p-service-vnpost')")
	public ResponseEntity<?> createVNPOST(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "customers_pay_vnPost");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@GetMapping("/repayment/vn-post")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root','tpf-service-momo','3p-service-momo','3p-service-vnpost')")
	public ResponseEntity<?> readsVNPOST(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getCustomers_vnPost");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);

		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/repayment/pay")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root','tpf-service-momo','3p-service-momo')")
	public ResponseEntity<?> repayment_pay(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "repayment_pay");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@GetMapping("/repayment/get")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment','tpf-service-root','tpf-service-momo','3p-service-momo')")
	public ResponseEntity<?> repayment_get(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "repayment_get");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-repayment", request);

		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

}