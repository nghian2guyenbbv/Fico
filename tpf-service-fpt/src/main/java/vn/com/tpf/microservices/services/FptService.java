package vn.com.tpf.microservices.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Fpt;

@Service
public class FptService {

	private ObjectNode error;

	private ObjectNode status;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ApiService apiService;

	@Autowired
	private ConvertService convertService;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@PostConstruct
	private void init() {
		status = mapper.createObjectNode();
		status.put("APPROVED", 0);
		status.put("PROCESSING", 1);
		status.put("APPROVED_AND_WAITING_SMS", 2);
		status.put("DISBURSED", 4);
		status.put("CANCELLED", 5);
		status.put("REJECTED", 6);
		status.put("LIQUIDATION", 7);

		error = mapper.createObjectNode();
		Map<?, ?> requestId = Map.of("code", 100, "message", "request_id is required string and not empty");
		error.set("requestId", mapper.convertValue(requestId, JsonNode.class));
		Map<?, ?> dateTime = Map.of("code", 101, "message", "date_time is required string and not empty");
		error.set("dateTime", mapper.convertValue(dateTime, JsonNode.class));
		Map<?, ?> postCodeNotExists = Map.of("code", 103, "message", "city not exits");
		error.set("postCodeNotExists", mapper.convertValue(postCodeNotExists, JsonNode.class));
	}

	private JsonNode validation(JsonNode body, List<String> fields) {
		for (String field : fields) {
			if (field.equals("request_id")
					&& (!body.path("request_id").isTextual() || body.path("request_id").asText().isEmpty())) {
				return error.get("requestId");
			}
		}
		return null;
	}

	private JsonNode response(int status, JsonNode data) {
		return mapper.createObjectNode().put("status", status).set("data", data);
	}

	private JsonNode response(int code, JsonNode body, JsonNode data) {
		ObjectNode res = mapper.createObjectNode();
		res.put("result_code", code);
		res.put("request_id", body.path("request_id").asText());
		res.put("reference_id", body.path("reference_id").asText());
		res.set("date_time", mapper.convertValue(new Date(), JsonNode.class));
		if (code == 0) {
			res.set("data", data);
		} else {
			res.set("message", data.path("message"));
		}
		return mapper.createObjectNode().put("status", 200).set("data", res);
	}

	public JsonNode getDetail(JsonNode request) throws Exception {
		Fpt fpt = mongoTemplate.findOne(Query.query(Criteria.where("id").is(request.path("param").path("id").asText())),
				Fpt.class);

		if (fpt == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"));
		}

		return response(200, mapper.convertValue(fpt, JsonNode.class));
	}

	public JsonNode createFpt(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode valid = validation(body, Arrays.asList("request_id"));

		if (valid != null) {
			return response(valid.get("code").asInt(), body, mapper.createObjectNode().set("message", valid.get("message")));
		}

		JsonNode data = body.path("data");
		JsonNode postCode = rabbitMQService.sendAndReceive("tpf-service-assets", Map.of("func", "getAddress",
				"reference_id", body.path("reference_id"), "param", Map.of("postCode", data.path("issuePlace").asText())));

		if (postCode.path("status").asInt() != 200) {
			return response(error.get("postCodeNotExists").get("code").asInt(), body,
					mapper.createObjectNode().set("message", error.get("postCodeNotExists").get("message")));
		}

		Fpt fpt = mapper.convertValue(data, Fpt.class);
		fpt.getPhotos().forEach(e -> e.setUpdatedAt(new Date()));
		fpt.setIssuePlace(postCode.path("data").path("cityName").asText());
		fpt.getAddresses().forEach(address -> {
			try {
				JsonNode addr = rabbitMQService.sendAndReceive("tpf-service-assets", Map.of("func", "getAddress",
						"reference_id", body.path("reference_id"), "param", Map.of("areaCode", address.getDistrict())));
				address.setDistrict(addr.path("data").path("areaName").asText());
				address.setProvince(addr.path("data").path("cityName").asText());
				address.setRegion(addr.path("data").path("region").asText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		mongoTemplate.save(fpt);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppFinnone(fpt)));

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(fpt)));

		return response(0, body, mapper.convertValue(convertService.toAppFinnone(fpt), JsonNode.class));
	}

	public JsonNode updateAutomation(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Assert.isTrue(body.path("transaction_id").isTextual() && !body.path("transaction_id").asText().isEmpty(),
				"transaction_id is required and not empty");
		Assert.isTrue(body.path("app_id").isTextual() && !body.path("app_id").asText().isEmpty(),
				"app_id is required and not empty");
		Assert.isTrue(body.path("automation_result").isTextual() && !body.path("automation_result").asText().isEmpty(),
				"automation_result is required and not empty");

		Query query = Query.query(Criteria.where("custId").is(body.path("transaction_id").asText()));
		Update update = new Update().set("appId", body.path("app_id").asText())
				.set("automationResult", body.path("automation_result").asText()).set("status", "PROCESSING");
		Fpt fpt = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Fpt.class);

		if (fpt == null) {
			return response(404, mapper.createObjectNode().put("message", "Cust Id Not Found"));
		}

		rabbitMQService.send("tpf-service-app", Map.of("func", "updateApp", "reference_id", body.path("reference_id"),
				"param", Map.of("project", "fpt", "id", fpt.getId()), "body", convertService.toAppDisplay(fpt)));

		return response(200, null);
	}

	public JsonNode updateStatus(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Fpt fpt = mongoTemplate.findOne(Query.query(Criteria.where("appId").is(body.path("app_id").asText())), Fpt.class);

		if (fpt == null) {
			return response(404, mapper.createObjectNode().put("message", "AppId Not Found"));
		}

		if (body.path("status").asText().equals("APPROVED")) {
//			SyncACCA
		}

		fpt.setStatus(body.path("status").asText());
		fpt.setAutomationResult(fpt.getAutomationResult().equals("Pass") ? fpt.getAutomationResult() : "Fix");
		mongoTemplate.save(fpt);

		new Thread(() -> {
			apiService
					.sendStatusToFpt(mapper.createObjectNode().put("custId", fpt.getCustId()).put("status", fpt.getStatus()));
		}).start();

		rabbitMQService.send("tpf-service-app", Map.of("func", "updateApp", "reference_id", body.path("reference_id"),
				"param", Map.of("project", "fpt", "id", fpt.getId()), "body", convertService.toAppDisplay(fpt)));

		return response(200, null);
	}

}