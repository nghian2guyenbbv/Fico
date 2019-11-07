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
