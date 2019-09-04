package vn.com.tpf.microservices.services;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class HttpLogService implements ClientHttpRequestInterceptor {

	private ObjectMapper mapper;

	public HttpLogService(ObjectMapper mapper) {
		super();
		this.mapper = mapper;
	}

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		ObjectNode dataLogReq = mapper.createObjectNode();
		dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
		dataLogReq.set("method", mapper.convertValue(request.getMethod(), JsonNode.class));
		dataLogReq.set("url", mapper.convertValue(request.getURI(), JsonNode.class));
		dataLogReq.set("header", mapper.convertValue(request.getHeaders(), JsonNode.class));
		try {
			dataLogReq.set("payload", mapper.readTree(new String(body, "UTF-8")));
		} catch (Exception e) {
			dataLogReq.put("payload", new String(body, "UTF-8"));
		}
		log.info("{}", dataLogReq);

		ClientHttpResponse response = execution.execute(request, body);

		ObjectNode dataLogRes = mapper.createObjectNode();
		dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
		dataLogRes.set("status", mapper.convertValue(response.getStatusCode(), JsonNode.class));
		dataLogRes.put("result", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
		try {
			dataLogRes.set("payload", mapper.readTree(new String(body, "UTF-8")));
		} catch (Exception e) {
			dataLogRes.put("payload", new String(body, "UTF-8"));
		}
		log.info("{}", dataLogRes);

		return response;
	}

}