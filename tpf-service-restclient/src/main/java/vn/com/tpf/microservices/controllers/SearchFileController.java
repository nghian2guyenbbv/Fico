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
public class SearchFileController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@PostMapping("/search-file-by-keyword")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-search-folder','tpf-service-root','tpf-service-momo','3p-service-momo')")
	public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getListPathFileByKeyword");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-search-folder", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}
}