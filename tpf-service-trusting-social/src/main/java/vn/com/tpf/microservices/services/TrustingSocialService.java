package vn.com.tpf.microservices.services;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

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

	private ObjectNode error;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ApiService apiService;

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
		Map<?, ?> provinceNotExists = Map.of("code", 111, "message", "District Code Not Exits");
		error.set("provinceNotExists", mapper.convertValue(provinceNotExists, JsonNode.class));
		Map<?, ?> districtCode = Map.of("code", 112, "message", "district_code is required number");
		error.set("districtCode", mapper.convertValue(districtCode, JsonNode.class));
		Map<?, ?> districtNotExists = Map.of("code", 113, "message", "District Code Not Exits");
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
	}

	private Map<String, Object> response(int code, JsonNode body, Object data) {
		Map<String, Object> res = new HashMap<>();
		res.put("result_code", code);
		res.put("request_id", body.path("request_id").asText());
		res.put("date_time", body.path("date_time").asText());
		res.put("reference_id", body.path("reference_id").asText());
		if (code == 0) {
			res.put("data", data);
		} else {
			res.put("message", mapper.convertValue(data, JsonNode.class).get("message"));
		}
		return Map.of("status", 200, "data", res);
	}

	public Map<String, Object> firstCheckTrustingSocial(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode valid = validation(body,
				Arrays.asList("request_id", "date_time", "data.phone_number", "data.national_id", "data.middle_name",
						"data.first_name", "data.last_name", "data.address_no", "data.dob", "data.gender", "data.province_code",
						"data.district_code"));

		if (valid != null) {
			return response(valid.get("code").asInt(), body, Map.of("message", valid.get("message").asText()));
		}

		ObjectNode data = (ObjectNode) body.path("data");
		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddress", "token",
						String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()), "param",
						Map.of("areaCode", data.path("district_code").asInt())));

		if (address.path("status").asInt() != 200) {
			return response(error.get("districtNotExists").get("code").asInt(), body,
					Map.of("message", error.get("districtNotExists").get("message").asText()));
		}
		if (!address.path("data").path("postCode").asText().equals(data.path("province_code").asText())) {
			return response(error.get("provinceNotExists").get("code").asInt(), body,
					Map.of("message", error.get("provinceNotExists").get("message").asText()));
		}

		TrustingSocial ts = new TrustingSocial();
		ts.setPhone_number(data.path("phone_number").asText());
		ts.setNational_id(data.path("national_id").asText());
		ts.setMiddle_name(data.path("middle_name").asText());
		ts.setFirst_name(data.path("first_name").asText());
		ts.setLast_name(data.path("last_name").asText());
		ts.setAddress_no(data.path("address_no").asText());
		ts.setDob(data.path("dob").asText());
		ts.setGender(data.path("gender").asText());
		ts.setProvince_code(data.path("province_code").asText());
		ts.setProvince(address.path("data").path("cityName").asText());
		ts.setDistrict_code(data.path("district_code").asText());
		ts.setDistrict(address.path("data").path("areaName").asText());
		mongoTemplate.save(ts);

		data.set("district_code", address.path("data").path("areaName"));
		data.set("province_code", address.path("data").path("cityName"));
		data.set("reference_id", body.path("reference_id"));
		data.set("request_id", body.path("request_id"));

		return response(0, body, Map.of("status", apiService.firstCheckRisk(data).equals("pass") ? "passed" : "rejected"));
	}

	public Map<String, Object> createTrustingSocial(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode valid = validation(body,
				Arrays.asList("request_id", "date_time", "data.phone_number", "data.national_id", "data.middle_name",
						"data.first_name", "data.last_name", "data.address_no", "data.dob", "data.gender", "data.province_code",
						"data.district_code", "data.ts_lead_id", "data.product_code", "data.score_range", "data.dsa_code",
						"data.tsa_code", "data.ward", "data.documents"));

		if (valid != null) {
			return response(valid.get("code").asInt(), body, Map.of("message", valid.get("message").asText()));
		}

		ObjectNode data = (ObjectNode) body.path("data");
		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddress", "token",
						String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()), "param",
						Map.of("areaCode", data.path("district_code").asInt())));

		if (address.path("status").asInt() != 200) {
			return response(error.get("districtNotExists").get("code").asInt(), body,
					Map.of("message", error.get("districtNotExists").get("message").asText()));
		}
		if (!address.path("data").path("postCode").asText().equals(data.path("province_code").asText())) {
			return response(error.get("provinceNotExists").get("code").asInt(), body,
					Map.of("message", error.get("provinceNotExists").get("message").asText()));
		}

		TrustingSocial ts = new TrustingSocial();
		ts.setPhone_number(data.path("phone_number").asText());
		ts.setNational_id(data.path("national_id").asText());
		ts.setMiddle_name(data.path("middle_name").asText());
		ts.setFirst_name(data.path("first_name").asText());
		ts.setLast_name(data.path("last_name").asText());
		ts.setAddress_no(data.path("address_no").asText());
		ts.setDob(data.path("dob").asText());
		ts.setGender(data.path("gender").asText());
		ts.setProvince_code(data.path("province_code").asText());
		ts.setProvince(address.path("data").path("cityName").asText());
		ts.setDistrict_code(data.path("district_code").asText());
		ts.setDistrict(address.path("data").path("areaName").asText());
		ts.setTs_lead_id(data.path("ts_lead_id").asText());
		ts.setProduct_code(data.path("product_code").asText());
		ts.setScore_range(data.path("score_range").asText());
		ts.setDsa_code(data.path("dsa_code").asText());
		ts.setTsa_code(data.path("tsa_code").asText());
		ts.setWard(data.path("ward").asText());

		Set<Map<?, ?>> documents = new HashSet<>();
		data.path("documents").forEach(e -> {
			Map<String, Object> doc = getComment(e.path("document_type").asText(""), e.path("view_url").asText(""),
					e.path("download_url").asText(""), null);
			doc.put("updatedAt", new Date());
			documents.add(doc);
		});
		ts.setDocuments(documents);

		mongoTemplate.save(ts);

//		rabbitMQService.send("tpf-service-app",
//				Map.of("func", "createApp", "token",
//						String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()), "param",
//						Map.of("project", "trustingsocial"), "body",
//						mapper.convertValue(ts, ObjectNode.class).put("status", "PROCESSING")));

		return response(0, body, ts);
	}

	public Map<String, Object> updateTrustingSocial(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode data = body.path("data");

		Map<String, Object> comments = new HashMap<>();
		comments.put("updatedAt", new Date());
		comments.put("return", true);

		if (data.path("dc_documents").isObject()) {
			comments.put("document_type", data.path("dc_documents").path("document_type").asText());
			comments.put("code", data.path("dc_documents").path("dc_return_code").asText());
			comments.put("name", data.path("dc_documents").path("dc_return_code_desc").asText());
			comments.put("request", data.path("dc_documents").path("dc_comment").asText());

			Set<Map<?, ?>> response = new HashSet<>();
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

			Set<Map<?, ?>> response = new HashSet<>();
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

		TrustingSocial nEntity = mongoTemplate.findAndModify(
				Query.query(Criteria.where("ts_lead_id").is(data.path("ts_lead_id").asText()).and("comments.code")
						.is(comments.get("code")).and("comments.return").is(false)),
				new Update().set("comments.$", comments), new FindAndModifyOptions().returnNew(true), TrustingSocial.class);

		if (nEntity == null) {
			return response(error.get("codeWasSubmit").get("code").asInt(), body,
					Map.of("message", error.get("codeWasSubmit").get("message").asText()));
		}

		return response(0, body, nEntity);
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
			if (field.equals("data.documents")
					&& (!body.path("data").path("documents").isArray() || body.path("data").path("documents").size() == 0)) {
				return error.get("documents");
			}
		}
		return null;
	}

}