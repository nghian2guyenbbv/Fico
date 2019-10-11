package vn.com.tpf.microservices.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class PGPService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitMQService rabbitMQService;

	public JsonNode decrypt(String project, String contentType, String body) throws Exception {
		JsonNode bodyNode = mapper.createObjectNode();
		try {
			bodyNode = (ObjectNode) mapper.readTree(body);
		} catch (Exception e) {
			if (contentType.equals("application/pgp-encrypted")) {
				JsonNode decrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
						Map.of("func", "pgpDecrypt", "body", Map.of("project", project, "data", body)));
				if (decrypt.path("data").isObject()) {
					bodyNode = decrypt.path("data");
				}
			}
		}
		return bodyNode;
	}

	public JsonNode encrypt(String project, String contentType, JsonNode data) throws Exception {
		if (contentType.equals("application/pgp-encrypted")) {
			JsonNode encrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
					Map.of("func", "pgpEncrypt", "body", Map.of("project", project, "data", data.asText())));
			return encrypt.path("data");
		}
		return data;
	}

}
