package vn.com.tpf.microservices.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class SmsService {
	
	

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ApiService apiService ;
	
	@Autowired
	RabbitMQService rabbitMQService;

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}

	public JsonNode sendSms(JsonNode request) {
		return apiService.sendSMS(request);

	}
	
	public JsonNode receiveSms(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		Assert.isTrue(body.path("access_key").isTextual() && !body.path("access_key").asText().isEmpty(),
				"access_key is required and not empty");

		if (!body.path("access_key").asText().equals("access_key_sms")) {
			return response(401, mapper.createObjectNode().put("message", "Unauthorized"));
		}
		
		JsonNode sms = rabbitMQService.sendAndReceive("tpf-service-" + body.path("service").asText().toLowerCase(), Map.of("func", "updateSms", "body", body));
		return response(sms.path("status").asInt(), sms.path("data"));
		
	}

}