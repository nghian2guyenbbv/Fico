package vn.com.tpf.microservices.services;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.utils.PGPHelper;
import vn.com.tpf.microservices.utils.PGPInfo;

@Service
public class PGPService {

	@Autowired
	private ObjectMapper mapper;
	
//	
//	@PostConstruct
//	public void initTest() throws JsonProcessingException {
//		
//		String data = "-----BEGIN PGP MESSAGE-----\\nVersion: GnuPG v1\\n\\nhIwD9/nZHotASLUBBACFUJwkRuEu5M5P4Gu8NyNVWQs3s/jjO5L5dqKxbwKjcFFa\\nJ9ObK5tfBZHFgCMQz1PxMPYDY0GerMdbfCfEcTrZQgmk3043Tsk4f0E9akjBt9fc\\nCx8rIL0GDbWaa5gYbr6ZscdCF8Zt3S6fu8DuqIR+XYvg3kZ8R9zdkPIMuXM7ldJa\\nAT1qbNxoADH/z1eJ3dGYL/h57RMqiLtx7u0l7RhYKwON9yUbVezoWTnYYRJm1M97\\nl/roalQYQVFxoJClU/ZqrfX5FNYJGQ86f4gD5SQ4YHzzGV566Chf7mjB\\n=cIXD\\n-----END PGP MESSAGE-----";
//	
////		JsonNode response  = pgpEncrypt(mapper.convertValue(Map.of("func", "pgpDecrypt", "body", Map.of("project", "smartnet", "data", "000111222333")),JsonNode.class));
//		JsonNode response  = pgpDecrypt(mapper.convertValue(Map.of("func", "pgpDecrypt", "body", Map.of("project", "smartnet", "data", data)),JsonNode.class));
//			
//		System.out.println(mapper.writeValueAsString(response));
////		System.out.println(PGPInfo.publicKey);
//	}

	public JsonNode pgpEncrypt(JsonNode request) {
		String preshareKey = PGPInfo.preshareKey;
		String privateKey = PGPInfo.privateKey;
		String publicKey = PGPInfo.publicKey;

		if (request.path("body").path("preshareKey").isTextual()) {
			preshareKey = request.path("body").path("preshareKey").asText();
		}
		if (request.path("body").path("privateKey").isTextual()) {
			privateKey = request.path("body").path("privateKey").asText();
		}
		if (request.path("body").path("publicKey").isTextual()) {
			publicKey = request.path("body").path("publicKey").asText();
		}
		if (request.path("body").path("project").isTextual()) {
			publicKey = PGPInfo.publicKey3P.get(request.path("body").path("project").asText());
		}

		PGPHelper pgpHelper = new PGPHelper(preshareKey, privateKey, publicKey);
		ByteArrayOutputStream encStream = new ByteArrayOutputStream();
		pgpHelper.encryptAndSign(request.path("body").path("data").asText().getBytes(), encStream);
		return mapper.createObjectNode().put("status", 200).put("data", encStream.toString());
	}

	public JsonNode pgpDecrypt(JsonNode request) {
		String preshareKey = PGPInfo.preshareKey;
		String privateKey = PGPInfo.privateKey;
		String publicKey = PGPInfo.publicKey;

		if (request.path("body").path("preshareKey").isTextual()) {
			preshareKey = request.path("body").path("preshareKey").asText();
		}
		if (request.path("body").path("privateKey").isTextual()) {
			privateKey = request.path("body").path("privateKey").asText();
		}
		if (request.path("body").path("publicKey").isTextual()) {
			publicKey = request.path("body").path("publicKey").asText();
		}
		if (request.path("body").path("project").isTextual()) {
			publicKey = PGPInfo.publicKey3P.get(request.path("body").path("project").asText());
		}

		PGPHelper pgpHelper = new PGPHelper(preshareKey, privateKey, publicKey);
		ByteArrayOutputStream desStream = new ByteArrayOutputStream();
		try {
			pgpHelper.decryptAndVerifySignature(request.path("body").path("data").asText().replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n").getBytes(), desStream);
			return mapper.createObjectNode().put("status", 200).set("data", mapper.readTree(desStream.toString()));
		} catch (Exception e) {
			return mapper.createObjectNode().put("status", 500).put("message", e.getMessage());
		}
	}

}
