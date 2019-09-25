package vn.com.tpf.microservices.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
//import vn.com.tpf.microservices.configs.RepaymentException;
import vn.com.tpf.microservices.services.RabbitMQService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RepaymentController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@PostMapping("/repayment/add-payment")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment')")
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
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-repayment')")
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

}