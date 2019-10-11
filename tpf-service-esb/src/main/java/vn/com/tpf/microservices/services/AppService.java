package vn.com.tpf.microservices.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.App;

@Service
public class AppService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitMQService rabbitMQService;

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}

	public JsonNode createApp(JsonNode request) throws Exception {
		JsonNode res = rabbitMQService.sendAndReceive("tpf-service-automation",
				Map.of("func", "createApp", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"body", mapper.convertValue(request.path("body"), App.class)));
		return response(res.path("status").asInt(), res.path("data"));
	}

	public JsonNode updateApp(JsonNode request) throws Exception {
		JsonNode res = rabbitMQService.sendAndReceive("tpf-service-automation",
				Map.of("func", "updateApp", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"body", mapper.convertValue(request.path("body"), App.class)));
		return response(res.path("status").asInt(), res.path("data"));
	}

	public JsonNode updateAutomation(JsonNode request) throws Exception {
		JsonNode auto = rabbitMQService.sendAndReceive("tpf-service-" + request.path("body").path("project").asText(),
				Map.of("func", "updateAutomation", "token",
						"Bearer " + rabbitMQService.getToken().path("access_token").asText(), "body", request.path("body")));
		return response(auto.path("status").asInt(), auto.path("data"));
	}

	public JsonNode getReason(JsonNode request) throws Exception {
		JsonNode reason = rabbitMQService.sendAndReceive("tpf-service-finnone",
				Map.of("func", "getReason", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"param", Map.of("appId", request.path("param").path("appId").asText(), "status",
								request.path("param").path("status").asText())));
		return response(reason.path("status").asInt(), reason.path("data"));
	}

	public JsonNode getLoan(JsonNode request) throws Exception {
		JsonNode loan = rabbitMQService.sendAndReceive("tpf-service-finnone",
				Map.of("func", "getLoan", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"param", Map.of("appId", request.path("param").path("appId").asText())));
		return response(loan.path("status").asInt(), loan.path("data"));
	}

}