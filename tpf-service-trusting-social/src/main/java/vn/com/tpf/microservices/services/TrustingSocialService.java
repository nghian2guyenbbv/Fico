package vn.com.tpf.microservices.services;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.TrustingSocial;

@Service
public class TrustingSocialService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private ObjectNode error;

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
		error = mapper.createObjectNode();
		Map<?, ?> requestId = Map.of("code", 100, "message", "request_id is required string and not empty");
		error.set("requestId", mapper.convertValue(requestId, JsonNode.class));
		Map<?, ?> dateTime = Map.of("code", 101, "message", "date_time is required string and not empty");
		error.set("dateTime", mapper.convertValue(dateTime, JsonNode.class));
		Map<?, ?> phoneNumber = Map.of("code", 102, "message", "phone_number is required string. Ex: +84123456789");
		error.set("phoneNumber", mapper.convertValue(phoneNumber, JsonNode.class));
		Map<?, ?> nationalId = Map.of("code", 103, "message", "national_id is required string ^0-9 length 9 or 12");
		error.set("nationalId", mapper.convertValue(nationalId, JsonNode.class));
		Map<?, ?> middleName = Map.of("code", 104, "message", "middle_name is required string");
		error.set("middleName", mapper.convertValue(middleName, JsonNode.class));
		Map<?, ?> firstName = Map.of("code", 105, "message", "first_name is required string and not empty");
		error.set("firstName", mapper.convertValue(firstName, JsonNode.class));
		Map<?, ?> lastName = Map.of("code", 106, "message", "last_name is required string and not empty");
		error.set("lastName", mapper.convertValue(lastName, JsonNode.class));
		Map<?, ?> addressNo = Map.of("code", 107, "message", "address_no is required string and not empty");
		error.set("addressNo", mapper.convertValue(addressNo, JsonNode.class));
		Map<?, ?> dob = Map.of("code", 108, "message", "dob is required string format dd/MM/yyyy");
		error.set("dob", mapper.convertValue(dob, JsonNode.class));
		Map<?, ?> gender = Map.of("code", 109, "message", "gender is required male or female");
		error.set("gender", mapper.convertValue(gender, JsonNode.class));
		Map<?, ?> provinceCode = Map.of("code", 110, "message", "province_code is required number");
		error.set("provinceCode", mapper.convertValue(provinceCode, JsonNode.class));
		Map<?, ?> provinceNotExists = Map.of("code", 111, "message", "province_code not exits");
		error.set("provinceNotExists", mapper.convertValue(provinceNotExists, JsonNode.class));
		Map<?, ?> districtCode = Map.of("code", 112, "message", "district_code is required number");
		error.set("districtCode", mapper.convertValue(districtCode, JsonNode.class));
		Map<?, ?> districtNotExists = Map.of("code", 113, "message", "district_code not exits");
		error.set("districtNotExists", mapper.convertValue(districtNotExists, JsonNode.class));
		Map<?, ?> tsLeadId = Map.of("code", 114, "message", "ts_lead_id is required string and not empty");
		error.set("tsLeadId", mapper.convertValue(tsLeadId, JsonNode.class));
		Map<?, ?> productCode = Map.of("code", 115, "message", "product_code is required string and not empty");
		error.set("productCode", mapper.convertValue(productCode, JsonNode.class));
		Map<?, ?> scoreRange = Map.of("code", 116, "message", "score_range is required string and not empty");
		error.set("scoreRange", mapper.convertValue(scoreRange, JsonNode.class));
		Map<?, ?> dsaCode = Map.of("code", 117, "message", "dsa_code is required string and not empty");
		error.set("dsaCode", mapper.convertValue(dsaCode, JsonNode.class));
		Map<?, ?> tsaCode = Map.of("code", 118, "message", "tsa_code is required string and not empty");
		error.set("tsaCode", mapper.convertValue(tsaCode, JsonNode.class));
		Map<?, ?> ward = Map.of("code", 119, "message", "ward is required string and not empty");
		error.set("ward", mapper.convertValue(ward, JsonNode.class));
		Map<?, ?> documents = Map.of("code", 120, "message", "documents is required array and not empty");
		error.set("documents", mapper.convertValue(documents, JsonNode.class));
		Map<?, ?> codeWasSubmit = Map.of("code", 121, "message", "dc_return_code or info_query_code was submit");
		error.set("codeWasSubmit", mapper.convertValue(codeWasSubmit, JsonNode.class));
		Map<?, ?> stage = Map.of("code", 122, "message", "stage is required string and not empty");
		error.set("stage", mapper.convertValue(stage, JsonNode.class));
	}

	private JsonNode validation(JsonNode body, List<String> fields) {
		for (String field : fields) {
			if (field.equals("request_id")
					&& (!body.path("request_id").isTextual() || body.path("request_id").asText().isEmpty())) {
				return error.get("requestId");
			}
			if (field.equals("date_time")
					&& (!body.path("date_time").isTextual() || body.path("date_time").asText().isEmpty())) {
				return error.get("dateTime");
			}
			if (field.equals("data.phone_number")
					&& !body.path("data").path("phone_number").asText().matches("^(\\+84)(?=(?:.{9}|.{10})$)[0-9]*$")) {
				return error.get("phoneNumber");
			}
			if (field.equals("data.national_id")
					&& !body.path("data").path("national_id").asText().matches("^(?=(?:.{9}|.{12})$)[0-9]*$")) {
				return error.get("nationalId");
			}
			if (field.equals("data.middle_name") && !body.path("data").path("middle_name").isTextual()) {
				return error.get("middleName");
			}
			if (field.equals("data.first_name") && (!body.path("data").path("first_name").isTextual()
					|| body.path("data").path("first_name").asText().isEmpty())) {
				return error.get("firstName");
			}
			if (field.equals("data.last_name") && (!body.path("data").path("last_name").isTextual()
					|| body.path("data").path("last_name").asText().isEmpty())) {
				return error.get("lastName");
			}
			if (field.equals("data.address_no") && (!body.path("data").path("address_no").isTextual()
					|| body.path("data").path("address_no").asText().isEmpty())) {
				return error.get("addressNo");
			}
			if (field.equals("data.dob") && !body.path("data").path("dob").asText()
					.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$")) {
				return error.get("dob");
			}
			if (field.equals("data.gender")
					&& !body.path("data").path("gender").asText().toLowerCase().matches("^(male|female)$")) {
				return error.get("gender");
			}
			if (field.equals("data.province_code") && !body.path("data").path("province_code").isInt()) {
				return error.get("provinceCode");
			}
			if (field.equals("data.district_code") && !body.path("data").path("district_code").isInt()) {
				return error.get("districtCode");
			}
			if (field.equals("data.ts_lead_id") && (!body.path("data").path("ts_lead_id").isTextual()
					|| body.path("data").path("ts_lead_id").asText().isEmpty())) {
				return error.get("tsLeadId");
			}
			if (field.equals("data.product_code") && (!body.path("data").path("product_code").isTextual()
					|| body.path("data").path("product_code").asText().isEmpty())) {
				return error.get("productCode");
			}
			if (field.equals("data.score_range") && (!body.path("data").path("score_range").isTextual()
					|| body.path("data").path("score_range").asText().isEmpty())) {
				return error.get("scoreRange");
			}
			if (field.equals("data.dsa_code") && (!body.path("data").path("dsa_code").isTextual()
					|| body.path("data").path("dsa_code").asText().isEmpty())) {
				return error.get("dsaCode");
			}
			if (field.equals("data.tsa_code") && (!body.path("data").path("tsa_code").isTextual()
					|| body.path("data").path("tsa_code").asText().isEmpty())) {
				return error.get("tsaCode");
			}
			if (field.equals("data.ward")
					&& (!body.path("data").path("ward").isTextual() || body.path("data").path("ward").asText().isEmpty())) {
				return error.get("ward");
			}
			if (field.equals("data.stage")
					&& (!body.path("data").path("stage").isTextual() || body.path("data").path("stage").asText().isEmpty())) {
				return error.get("stage");
			}
			if (field.equals("data.documents")
					&& (!body.path("data").path("documents").isArray() || body.path("data").path("documents").size() == 0)) {
				return error.get("documents");
			}
		}
		return null;
	}

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}

	private JsonNode response(int code, JsonNode body, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
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
		response.put("status", 200).set("data", res);
		return response;
	}

	private void rabbitLog(JsonNode body, JsonNode data) {
		ObjectNode dataLog = mapper.createObjectNode();
		dataLog.put("type", "[==RABBITMQ-LOG==]");
		dataLog.set("result", data);
		dataLog.set("payload", body);
		log.info("{}", dataLog);
	}

	private Map<String, Object> getComment(String document_type, String view_url, String download_url, String comment) {
		Map<String, Object> c = new HashMap<>();
		if (document_type != null && !document_type.isEmpty()) {
			c.put("document_type", document_type);
		}
		if (view_url != null && !view_url.isEmpty()) {
			c.put("view_url", view_url);
		}
		if (download_url != null && !download_url.isEmpty()) {
			c.put("download_url", download_url);
		}
		if (comment != null && !comment.isEmpty()) {
			c.put("comment", comment);
		}
		return c;
	}

	public JsonNode firstCheckTrustingSocial(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode valid = validation(body,
				Arrays.asList("request_id", "date_time", "data.phone_number", "data.national_id", "data.middle_name",
						"data.first_name", "data.last_name", "data.address_no", "data.dob", "data.gender", "data.province_code",
						"data.district_code"));

		if (valid != null) {
			return response(valid.get("code").asInt(), body, mapper.createObjectNode().set("message", valid.get("message")));
		}

		ObjectNode data = (ObjectNode) body.path("data");
		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets", Map.of("func", "getAddress", "reference_id",
				body.path("reference_id"), "param", Map.of("areaCode", data.path("district_code").asInt())));

		if (address.path("status").asInt() != 200) {
			rabbitLog(body, address);
			return response(error.get("districtNotExists").get("code").asInt(), body,
					mapper.createObjectNode().set("message", error.get("districtNotExists").get("message")));
		}
		if (!address.path("data").path("postCode").asText().equals(data.path("city").asText())) {
			return response(error.get("cityNotExists").get("code").asInt(), body,
					mapper.createObjectNode().set("message", error.get("cityNotExists").get("message")));
		}

		TrustingSocial ts = mapper.convertValue(data, TrustingSocial.class);
		ts.setProvince(address.path("data").path("cityName").asText());
		ts.setDistrict(address.path("data").path("areaName").asText());
		mongoTemplate.save(ts);

		data.set("district_code", address.path("data").path("areaName"));
		data.set("province_code", address.path("data").path("cityName"));

		return response(0, body, mapper.createObjectNode().put("status",
				apiService.firstCheckRisk(data).equals("pass") ? "passed" : "rejected"));
	}

	public JsonNode createTrustingSocial(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode valid = validation(body,
				Arrays.asList("request_id", "date_time", "data.phone_number", "data.national_id", "data.middle_name",
						"data.first_name", "data.last_name", "data.address_no", "data.dob", "data.gender", "data.province_code",
						"data.district_code", "data.ts_lead_id", "data.product_code", "data.score_range", "data.dsa_code",
						"data.tsa_code", "data.ward", "data.documents"));

		if (valid != null) {
			return response(valid.get("code").asInt(), body, mapper.createObjectNode().set("message", valid.get("message")));
		}

		ObjectNode data = (ObjectNode) body.path("data");
		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets", Map.of("func", "getAddress", "reference_id",
				body.path("reference_id"), "param", Map.of("areaCode", data.path("district_code").asInt())));

		if (address.path("status").asInt() != 200) {
			rabbitLog(body, address);
			return response(error.get("districtNotExists").get("code").asInt(), body,
					mapper.createObjectNode().set("message", error.get("districtNotExists").get("message")));
		}
		if (!address.path("data").path("postCode").asText().equals(data.path("city").asText())) {
			return response(error.get("cityNotExists").get("code").asInt(), body,
					mapper.createObjectNode().set("message", error.get("cityNotExists").get("message")));
		}

		TrustingSocial ts = mapper.convertValue(data, TrustingSocial.class);
		ts.setProvince(address.path("data").path("cityName").asText());
		ts.setDistrict(address.path("data").path("areaName").asText());
		ts.getDocuments().forEach(e -> e.setUpdatedAt(new Date()));
		mongoTemplate.save(ts);

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"param", Map.of("project", "trustingsocial"), "body", convertService.toAppDisplay(ts)));

		return response(0, body, mapper.convertValue(ts, JsonNode.class));
	}

	public JsonNode updateTrustingSocial(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode valid = validation(body, Arrays.asList("request_id", "date_time", "data.ts_lead_id", "data.stage"));

		if (valid != null) {
			return response(valid.get("code").asInt(), body, mapper.createObjectNode().set("message", valid.get("message")));
		}

		JsonNode data = body.path("data");

		Query query = Query.query(Criteria.where("ts_lead_id").is(data.path("ts_lead_id").asText()));
		TrustingSocial oEntity = mongoTemplate.findOne(query, TrustingSocial.class);

		if (oEntity == null) {
			return response(404, mapper.createObjectNode().put("message", "TsLeadId Not Found"));
		}

		Set<Map<?, ?>> response = new HashSet<>();
		Map<String, Object> comments = new HashMap<>();
		comments.put("updatedAt", new Date());

		if (data.path("dc_documents").isObject()) {
			comments.put("document_type", data.path("dc_documents").path("document_type").asText());
			comments.put("code", data.path("dc_documents").path("dc_return_code").asText());
			comments.put("name", data.path("dc_documents").path("dc_return_code_desc").asText());
			comments.put("request", data.path("dc_documents").path("dc_comment").asText());

			if (data.path("dc_documents").path("dc_ts_comment").isTextual()) {
				response.add(getComment(null, null, null, data.path("dc_documents").path("dc_ts_comment").asText()));
			}
			if (data.path("dc_documents").path("dc_ts_documents").isArray()) {
				data.path("dc_documents").path("dc_ts_documents").forEach(c -> {
					response.add(getComment(c.path("document_type").asText(""), c.path("view_url").asText(""),
							c.path("download_url").asText(""), c.path("comment").asText()));
				});
			}
			comments.put("response", response);
		} else if (data.path("information").isObject()) {
			comments.put("code", data.path("information").path("info_query_code").asText());
			comments.put("name", data.path("information").path("info_query_name").asText());
			comments.put("request", data.path("information").path("info_query").asText());

			if (data.path("information").path("info_ts_comment").isTextual()) {
				response.add(getComment(null, null, null, data.path("information").path("info_ts_comment").asText()));
			}
			if (data.path("information").path("info_ts_documents").isArray()) {
				data.path("information").path("info_ts_documents").forEach(c -> {
					response.add(getComment(c.path("document_type").asText(""), c.path("view_url").asText(""),
							c.path("download_url").asText(""), c.path("comment").asText()));
				});
			}
			comments.put("response", response);
		}

		boolean isCodeWasSubmit = true;
		for (Map<String, Object> c : oEntity.getComments()) {
			if (c.get("response") == null && c.get("code") != null && c.get("code").equals(comments.get("code"))) {
				c.put("response", comments.get("response"));
				isCodeWasSubmit = false;
				break;
			}
		}

		if (isCodeWasSubmit) {
			return response(error.get("codeWasSubmit").get("code").asInt(), body,
					mapper.createObjectNode().set("message", error.get("codeWasSubmit").get("message")));
		}

		oEntity.getDocuments().forEach(c -> {
			response.forEach(r -> {
				if (c.getDocument_type() != null && r.get("document_type") != null
						&& c.getDocument_type().equals(r.get("document_type"))) {
					c.setDownload_url((String) r.get("download_url"));
					c.setView_url((String) r.get("view_url"));
					c.setUpdatedAt(new Date());
					return;
				}
			});
		});

		Update update = new Update().set("comments", oEntity.getComments())
				.set("stage", data.path("stage").asText().toUpperCase()).set("documents", oEntity.getDocuments());

		TrustingSocial nEntity = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				TrustingSocial.class);

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", body.path("reference_id"), "param",
						Map.of("project", "trustingsocial", "id", nEntity.getId()), "body", convertService.toAppDisplay(nEntity)));

		return response(0, body, mapper.convertValue(nEntity, JsonNode.class));
	}

}