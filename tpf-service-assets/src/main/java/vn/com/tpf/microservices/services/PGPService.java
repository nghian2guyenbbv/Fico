package vn.com.tpf.microservices.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.utils.PGPHelper;

@Service
public class PGPService {

	@Autowired
	private ObjectMapper mapper;

	public JsonNode pgpEncrypt(JsonNode request) {
		String project = request.path("body").path("project").asText();
		String data = request.path("body").path("data").asText();
		PGPHelper pgpHelper = new PGPHelper(project);
		ByteArrayOutputStream encStream = new ByteArrayOutputStream();
		pgpHelper.encryptAndSign(data.getBytes(), encStream);
		return mapper.createObjectNode().put("status", 200).put("data", encStream.toString());
	}

	public JsonNode pgpDecrypt(JsonNode request) {
		String project = request.path("body").path("project").asText();
		String data = request.path("body").path("data").asText();
		PGPHelper pgpHelper = new PGPHelper(project);
		ByteArrayOutputStream desStream = new ByteArrayOutputStream();
		pgpHelper.decryptAndVerifySignature(new BufferedReader(new StringReader(data)), desStream);
		return mapper.createObjectNode().put("status", 200).put("data", desStream.toString());
	}

}
