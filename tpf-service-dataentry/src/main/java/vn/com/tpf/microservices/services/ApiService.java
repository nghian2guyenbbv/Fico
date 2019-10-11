package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.models.FirstCheck;
import vn.com.tpf.microservices.models.FirstCheckRequest;
import vn.com.tpf.microservices.models.FirstCheckResponse;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String urlFirstCheck;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Arrays.asList(new HttpLogService()));
	}

	public String firstCheck(JsonNode request) {
		urlFirstCheck = "http://10.131.21.137:52233/sale_page/api_management/early_check/";
		Map<?, ?> data = Map.of("file", request.path("body"));
		try {
			Assert.notNull(request.get("body"), "no body");
//			FirstCheckRequest requestFirstCheck = mapper.treeToValue(request.path("body").path("data"), FirstCheckRequest.class);
			FirstCheckRequest requestFirstCheck = new FirstCheckRequest();
			requestFirstCheck.setProject_name("data-entry");

			requestFirstCheck.setRequest_id("de-"+ UUID.randomUUID().toString().substring(0,11));
			requestFirstCheck.setFull_name(request.path("body").path("data").path("customerName").textValue());
			requestFirstCheck.setCustomer_id(request.path("body").path("data").path("customerId").textValue());
			requestFirstCheck.setDsa_code(request.path("body").path("data").path("dsaCode").textValue());
			requestFirstCheck.setBank_card_number(request.path("body").path("data").path("bankCardNumber").textValue());
			requestFirstCheck.setCurrent_address(request.path("body").path("data").path("currentAddress").textValue());
			requestFirstCheck.setArea_id(request.path("body").path("data").path("areaId").textValue());


			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<?> entity = new HttpEntity<>(request.path("body").path("data"), headers);
			ResponseEntity<?> res = restTemplate.postForEntity(urlFirstCheck, entity, Object.class);
			JsonNode body = mapper.valueToTree(res.getBody());

			FirstCheckResponse firstCheckResponse = mapper.treeToValue(body, FirstCheckResponse.class);

			FirstCheck firstCheck = new FirstCheck();
			firstCheck.setRequest(requestFirstCheck);
			firstCheck.setResponse(firstCheckResponse);
			mongoTemplate.save(firstCheck);

			if (firstCheckResponse.isAll()){
				return "pass";
			}
			return "fail";

		} catch (HttpClientErrorException e) {
			log.info("[==HTTP-LOG-RESPONSE==] : {}",
					Map.of("payload", data, "status", e.getStatusCode(), "result", e.getResponseBodyAsString()));
			return e.getMessage();

		} catch (Exception e) {
			log.info("[==HTTP-LOG-RESPONSE==] : {}", Map.of("payload", data, "status", 500, "result", e.getMessage()));
			return e.getCause().toString();
		}
	}

}