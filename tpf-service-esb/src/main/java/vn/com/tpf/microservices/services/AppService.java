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
		JsonNode res = rabbitMQService.sendAndReceive("tpf-service-automation", Map.of("func", "createApp", "reference_id",
				request.path("reference_id"), "body", mapper.convertValue(request.path("body"), App.class)));
		return response(res.path("status").asInt(), res.path("data"));
	}
	
	public JsonNode createQuickLeadApp(JsonNode request) throws Exception {
		final String project = request.path("body").path("project").asText();
		final String queue_automation = "tpf-service-automation".concat(project.isBlank()?"":"-"+project.toLowerCase().trim());
		JsonNode res = rabbitMQService.sendAndReceive(queue_automation, Map.of("func", "quickLeadApp", "reference_id",
				request.path("reference_id"), "body", mapper.convertValue(request.path("body"), Object.class)));
		return response(res.path("status").asInt(), res.path("data"));
	}
	
	public JsonNode waiveField(JsonNode request) throws Exception {
		final String queue_automation = "tpf-service-automation-mobility";
		JsonNode res = rabbitMQService.sendAndReceive(queue_automation, Map.of("func", "waiveField", "reference_id",
				request.path("reference_id"), "body", mapper.convertValue(request.path("body"), Object.class)));
		return response(res.path("status").asInt(), res.path("data"));
	}
	
	public JsonNode submitField(JsonNode request) throws Exception {
		final String queue_automation = "tpf-service-automation-mobility";
		JsonNode res = rabbitMQService.sendAndReceive(queue_automation, Map.of("func", "submitField", "reference_id",
				request.path("reference_id"), "body", mapper.convertValue(request.path("body"), Object.class)));
		return response(res.path("status").asInt(), res.path("data"));
	}

	public JsonNode updateApp(JsonNode request) throws Exception {
		JsonNode res = rabbitMQService.sendAndReceive("tpf-service-automation", Map.of("func", "updateApp", "reference_id",
				request.path("reference_id"), "body", mapper.convertValue(request.path("body"), App.class)));
		return response(res.path("status").asInt(), res.path("data"));
	}
	
	public JsonNode deResponseQuery(JsonNode request) throws Exception {
		final String project = request.path("body").path("project").asText();
		final String queue_automation = "tpf-service-automation".concat(project.isBlank()?"":"-"+project.toLowerCase().trim());
		JsonNode res = rabbitMQService.sendAndReceive(queue_automation, Map.of("func", "deResponseQuery", "reference_id",
				request.path("reference_id"), "body", request.path("body")));
		return response(res.path("status").asInt(), res.path("data"));
	}
	
	public JsonNode deSaleQueue(JsonNode request) throws Exception {
		final String project = request.path("body").path("project").asText();
		final String queue_automation = "tpf-service-automation".concat(project.isBlank()?"":"-"+project.toLowerCase().trim());
		JsonNode res = rabbitMQService.sendAndReceive(queue_automation, Map.of("func", "deSaleQueue", "reference_id",
				request.path("reference_id"), "body", request.path("body")));
		return response(res.path("status").asInt(), res.path("data"));
	}
	
	

	public JsonNode updateAutomation(JsonNode request) throws Exception {
		JsonNode auto = rabbitMQService.sendAndReceive("tpf-service-" + request.path("body").path("project").asText(),
				Map.of("func", "updateAutomation", "reference_id", request.path("reference_id"), "body", request.path("body")));
		return response(auto.path("status").asInt(), auto.path("data"));
	}
	
	
	
	public JsonNode getReason(JsonNode request) throws Exception {
		JsonNode reason = rabbitMQService.sendAndReceive("tpf-service-finnone",
				Map.of("func", "getReason", "reference_id", request.path("reference_id"), "param", Map.of("appId",
						request.path("param").path("appId").asText(), "status", request.path("param").path("status").asText())));
		return response(reason.path("status").asInt(), reason.path("data"));
	}

	public JsonNode getLoan(JsonNode request) throws Exception {
		JsonNode loan = rabbitMQService.sendAndReceive("tpf-service-finnone", Map.of("func", "getLoan", "reference_id",
				request.path("reference_id"), "param", Map.of("appId", request.path("param").path("appId").asText())));
		return response(loan.path("status").asInt(), loan.path("data"));
	}
	
	public JsonNode getAppInfo(JsonNode request) throws Exception {
		JsonNode data = rabbitMQService.sendAndReceive("tpf-service-finnone", Map.of("func", "getAppInfo", "reference_id",
				request.path("reference_id"), "body",request.path("body")));
		return response(data.path("status").asInt(), data.path("data"));
	}
	
	
	
	public JsonNode getCheckList(JsonNode request) throws Exception {
		JsonNode reason = rabbitMQService.sendAndReceive("tpf-service-finnone",
				Map.of("func", "getCheckList", "reference_id", request.path("reference_id"), "param", Map.of("bank_card_number",
						request.path("param").path("bank_card_number").asText(),"dsa_code", request.path("param").path("dsa_code").asText(),
						"area_code",request.path("param").path("area_code").asText() )));
		return response(reason.path("status").asInt(), reason.path("data"));
	}
	
	public JsonNode sendFinnOne(JsonNode request) throws Exception {
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-finnone",
				request);
		return response(response.path("status").asInt(), response.path("data"));
	}

}