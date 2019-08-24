package vn.com.tpf.microservices.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
	}

	private Map<String, Object> response(int code, JsonNode body, Object data) {
		Map<String, Object> res = new HashMap<>();
		res.put("result_code", code);
		res.put("request_id", body.path("request_id").asText());
		res.put("date_time", body.path("date_time").asText());
		res.put("reference_id", UUID.randomUUID().toString());
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
		ts.setPhoneNumber(data.path("phone_number").asText());
		ts.setNationalId(data.path("national_id").asText());
		ts.setMiddleName(data.path("middle_name").asText());
		ts.setFirstName(data.path("first_name").asText());
		ts.setLastName(data.path("last_name").asText());
		ts.setAddressNo(data.path("address_no").asText());
		ts.setDob(data.path("dob").asText());
		ts.setGender(data.path("gender").asText());
		ts.setProvinceCode(data.path("province_code").asText());
		ts.setProvince(address.path("data").path("cityName").asText());
		ts.setDistrictCode(data.path("district_code").asText());
		ts.setDistrict(address.path("data").path("areaName").asText());
		mongoTemplate.save(ts);

		data.set("district_code", address.path("data").path("areaName"));
		data.set("province_code", address.path("data").path("cityName"));

		return response(0, body, Map.of("status", apiService.firstCheckRisk(data).equals("pass") ? "passed" : "rejected"));
	}

	@SuppressWarnings("unchecked")
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
		ts.setPhoneNumber(data.path("phone_number").asText());
		ts.setNationalId(data.path("national_id").asText());
		ts.setMiddleName(data.path("middle_name").asText());
		ts.setFirstName(data.path("first_name").asText());
		ts.setLastName(data.path("last_name").asText());
		ts.setAddressNo(data.path("address_no").asText());
		ts.setDob(data.path("dob").asText());
		ts.setGender(data.path("gender").asText());
		ts.setProvinceCode(data.path("province_code").asText());
		ts.setProvince(address.path("data").path("cityName").asText());
		ts.setDistrictCode(data.path("district_code").asText());
		ts.setDistrict(address.path("data").path("areaName").asText());
		ts.setTsLeadId(data.path("ts_lead_id").asText());
		ts.setProductCode(data.path("product_code").asText());
		ts.setScoreRange(data.path("score_range").asText());
		ts.setDsaCode(data.path("dsa_code").asText());
		ts.setTsaCode(data.path("tsa_code").asText());
		ts.setWard(data.path("ward").asText());
		ts.setDocuments(mapper.convertValue(data.path("documents"), Set.class));
		mongoTemplate.save(ts);

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "createApp", "token",
						String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()), "param",
						Map.of("project", "trustingsocial"), "body",
						mapper.convertValue(ts, ObjectNode.class).put("status", "PROCESSING")));

		return response(0, body, ts);
	}

	public Map<String, Object> updateTrustingSocial(JsonNode request) throws Exception {
		return Map.of("status", 200);
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