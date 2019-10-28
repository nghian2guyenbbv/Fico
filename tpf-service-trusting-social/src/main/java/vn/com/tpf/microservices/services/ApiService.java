package vn.com.tpf.microservices.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.url.first-check-risk}")
	private String urlFirstCheckRisk;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;

	public String firstCheckRisk(JsonNode request) {
		Map<String, Object> profile = new HashMap<>();
		profile.put("firstName", request.path("first_name").asText());
		profile.put("lastName", request.path("last_name").asText());
		profile.put("middleName", request.path("middle_name").asText());
		profile.put("birthday", request.path("dob").asText());
		profile.put("gender", request.path("gender").asText().toUpperCase());
		profile.put("id", Map.of("number", request.path("national_id").asText()));
		profile.put("phoneNumber", request.path("phone_number").asText());
		Map<?, ?> data = Map.of("profile", profile, "requestId",
				("ts-".concat(String.valueOf(System.currentTimeMillis()))).substring(0, 13), "request_fb_address",
				String.format("%s %s %s", request.path("address_no").asText(), request.path("district_code").asText(),
						request.path("province_code").asText()));

		ObjectNode dataLogReq = mapper.createObjectNode();
		dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
		dataLogReq.put("method", "POST");
		dataLogReq.put("url", urlFirstCheckRisk);
		dataLogReq.set("payload", mapper.convertValue(data, JsonNode.class));
		log.info("{}", dataLogReq);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(data), headers);
			ResponseEntity<?> res = restTemplate.postForEntity(urlFirstCheckRisk, entity, Object.class);

			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
			dataLogRes.set("status", mapper.convertValue(res.getStatusCode(), JsonNode.class));
			dataLogRes.set("result", mapper.convertValue(res.getBody(), JsonNode.class));
			dataLogRes.set("payload", mapper.convertValue(data, JsonNode.class));
			log.info("{}", dataLogRes);

			JsonNode body = mapper.convertValue(res.getBody(), JsonNode.class);
			if (body.path("first_check_result").isTextual()) {
				return body.path("first_check_result").asText().toLowerCase();
			}
		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", mapper.convertValue(data, JsonNode.class));
			log.info("{}", dataLogRes);
		}

		return "pass";
	}

}