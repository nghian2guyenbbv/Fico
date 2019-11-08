package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.url.momo-status}")
	private String urlMomoStatus;

	@Value("${spring.url.momo-disburse}")
	private String urlMomoDisburse;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RabbitMQService rabbitMQService;

	private JsonNode callApiMomo(String url, JsonNode data) {
		try {
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.set("payload", data);
			log.info("{}", dataLogReq);

			String dataString = mapper.writeValueAsString(data);
			dataString = dataString.replace("emi", "EMI");

			JsonNode encrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
					Map.of("func", "pgpEncrypt", "body", Map.of("project", "momo", "data", dataString)));

			log.info("{}","HIEPLN1 - " +  encrypt.path("data").asText());

			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "application/pgp-encrypted");
			headers.set("Content-Type", "application/pgp-encrypted");
			headers.set("partner-code", "tpbfico");
			HttpEntity<String> entity = new HttpEntity<String>(encrypt.path("data").asText().replace("emi", "EMI"), headers);
			ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);

			log.info("{}","HIEPLN2 - " +  res.getBody().toString());

			JsonNode decrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
					Map.of("func", "pgpDecrypt", "body", Map.of("project", "momo", "data", res.getBody().toString())));
			
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
			dataLogRes.set("status", mapper.convertValue(res.getStatusCode(), JsonNode.class));
			dataLogRes.set("payload", data);
			dataLogRes.put("result", decrypt.path("data").asText());
			
			log.info("{}", dataLogRes);

			return decrypt.path("data");
		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", data);
			log.info("{}", dataLogRes);

			return mapper.createObjectNode().put("resultCode", 500).put("message", e.getMessage());
		}
	}

	public JsonNode sendStatusToMomo(JsonNode data) {
		return callApiMomo(urlMomoStatus, data);
	}

	public JsonNode sendDisburseToMomo(JsonNode data) {
		return callApiMomo(urlMomoDisburse, data);
	}

}