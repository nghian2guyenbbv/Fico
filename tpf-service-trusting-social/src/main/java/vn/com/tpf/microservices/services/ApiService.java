package vn.com.tpf.microservices.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	// @Value("${spring.url.first-check-risk}")
	private String urlFirstCheckRisk;

	@Autowired
	private ObjectMapper mapper;

	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Arrays.asList(new HttpLogService()));
	}

	public String firstCheckRisk(JsonNode request) {
		urlFirstCheckRisk = "http://10.131.21.137:52233/sale_page/api_management/firstcheck/";

		Map<String, Object> profile = new HashMap<>();
		profile.put("firstName", request.path("first_name").asText());
		profile.put("lastName", request.path("last_name").asText());
		profile.put("middleName", request.path("middle_name").asText());
		profile.put("birthday", request.path("dob").asText());
		profile.put("gender", request.path("gender").asText().toUpperCase());
		profile.put("id", Map.of("number", request.path("national_id").asText()));
		profile.put("phoneNumber", request.path("phone_number").asText());
		Map<?, ?> data = Map.of("profile", profile, "requestId", "ts-" + System.currentTimeMillis() / 1000,
				"request_fb_address", String.format("%s %s %s", request.path("address_no").asText(),
						request.path("district_code").asText(), request.path("province_code").asText()));

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(data), headers);
			ResponseEntity<?> res = restTemplate.postForEntity(urlFirstCheckRisk, entity, Object.class);

			JsonNode body = mapper.valueToTree(res.getBody());
			if (body.path("first_check_result").isTextual()) {
				return body.path("first_check_result").asText().toLowerCase();
			}
		} catch (HttpClientErrorException e) {
			log.info("[==HTTP-LOG-RESPONSE==] : {}",
					Map.of("payload", data, "status", e.getStatusCode(), "result", e.getResponseBodyAsString()));
		} catch (Exception e) {
			log.info("[==HTTP-LOG-RESPONSE==] : {}", Map.of("payload", data, "status", 500, "result", e.getMessage()));
		}

		return "pass";
	}

}