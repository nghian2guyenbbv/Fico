package vn.com.tpf.microservices.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.utils.PGPHelper;

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

	private void callApiMomo(String url, ObjectNode data) {
		try {
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.set("payload", mapper.convertValue(data, JsonNode.class));
			log.info("{}", dataLogReq);

			String dataString = mapper.writeValueAsString(data);
			dataString = dataString.replace("emi", "EMI");

			PGPHelper pgpHelper = new PGPHelper();
			ByteArrayOutputStream encStream = new ByteArrayOutputStream();
			pgpHelper.encryptAndSign(dataString.getBytes(), encStream);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "application/pgp-encrypted");
			headers.set("Content-Type", "application/pgp-encrypted");
			headers.set("partner-code", "tpbfico");
			HttpEntity<?> entity = new HttpEntity<>(encStream.toString(), headers);
			ResponseEntity<?> res = restTemplate.postForEntity(url, entity, Object.class);

			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
			dataLogRes.set("status", mapper.convertValue(res.getStatusCode(), JsonNode.class));
			dataLogRes.set("payload", mapper.convertValue(data, JsonNode.class));
			ByteArrayOutputStream desStream = new ByteArrayOutputStream();
			pgpHelper.decryptAndVerifySignature(new BufferedReader(new StringReader(res.getBody().toString())), desStream);
			dataLogRes.put("result", desStream.toString());
			log.info("{}", dataLogRes);
		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.getMessage());
			dataLogRes.set("payload", mapper.convertValue(data, JsonNode.class));
			log.info("{}", dataLogRes);
		}
	}

	public void sendStatusToMomo(ObjectNode data) {
		callApiMomo(urlMomoStatus, data);
	}

	public void sendDisburseToMomo(ObjectNode data) {
		callApiMomo(urlMomoDisburse, data);
	}

}