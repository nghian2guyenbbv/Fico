package vn.com.tpf.microservices.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

//	@Value("${spring.url.fpt-status}")
	private String urlFptStatus;

//	@Value("${spring.url.fpt-comment}")
	private String urlFptComment;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RabbitMQService rabbitMQService;

	private JsonNode callApiFpt(String url, JsonNode data) {
		try {
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.set("payload", data);
			log.info("{}", dataLogReq);

			String dataString = mapper.writeValueAsString(data);
			JsonNode encrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
					Map.of("func", "pgpEncrypt", "body", Map.of("project", "fpt", "data", dataString)));

			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "application/pgp-encrypted");
			headers.set("Content-Type", "application/pgp-encrypted");
			headers.set("partner-code", "tpbfico");
			HttpEntity<?> entity = new HttpEntity<>(encrypt.path("data").asText(), headers);
			ResponseEntity<?> res = restTemplate.postForEntity(url, entity, Object.class);

			JsonNode decrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
					Map.of("func", "pgpDecrypt", "body", Map.of("project", "fpt", "data", res.getBody().toString())));

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

	public JsonNode sendStatusToFpt(JsonNode data) {
		return callApiFpt(urlFptStatus, data);
	}

	public JsonNode sendCommentToFpt(JsonNode data) {
		return callApiFpt(urlFptComment, data);
	}

}