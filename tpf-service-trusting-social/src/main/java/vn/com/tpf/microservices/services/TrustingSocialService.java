package vn.com.tpf.microservices.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class TrustingSocialService {

	@Autowired
	private ApiService apiService;

	@Autowired
	private RabbitMQService rabbitMQService;

	public Map<String, Object> firstCheckTrustingSocial(JsonNode request) throws Exception {
		Assert.notNull(request.get("body"), "no body");

		ObjectNode body = (ObjectNode) request.path("body");
		Assert.isTrue(body.path("phone_number").asText().matches("^(\\+84)(?=(?:.{9}|.{10})$)[0-9]*$"),
				"phone_number is required string. Ex: +84123456789");
		Assert.isTrue(body.path("national_id").asText().matches("^(?=(?:.{9}|.{12})$)[0-9]*$"),
				"national_id is required string ^0-9 length 9 or 12");
		Assert.isTrue(body.path("middle_name").isTextual(), "middle_name is required string");
		Assert.isTrue(body.path("first_name").isTextual() && !body.path("first_name").asText().isEmpty(),
				"first_name is required string and not empty");
		Assert.isTrue(body.path("last_name").isTextual() && !body.path("last_name").asText().isEmpty(),
				"last_name is required string and not empty");
		Assert.isTrue(body.path("address_no").isTextual() && !body.path("address_no").asText().isEmpty(),
				"address_no is required string and not empty");
		Assert.isTrue(body.path("dob").asText().matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$"),
				"dob is required string format dd/MM/yyyy");
		Assert.isTrue(body.path("gender").asText().toLowerCase().matches("^(male|female)$"),
				"gender is required male or female");
		Assert.isTrue(body.path("province_code").isInt(), "province_code is required number");
		Assert.isTrue(body.path("district_code").isInt(), "district_code is required number");

		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddress", "token",
						String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()), "param",
						Map.of("areaCode", body.path("district_code").asInt())));

		if (address.path("status").asInt() != 200) {
			return Map.of("status", 404, "data", Map.of("message", "District Code Not Exits"));
		}

		if (!address.path("data").path("postCode").asText().equals(body.path("province_code").asText())) {
			return Map.of("status", 404, "data", Map.of("message", "Province Code Not Exits"));
		}

		body.set("district_code", address.path("data").path("areaName"));
		body.set("province_code", address.path("data").path("cityName"));

		return Map.of("status", 200, "data",
				Map.of("status", apiService.firstCheckRisk(body).equals("pass") ? "passed" : "rejected"));
	}

}