package vn.com.tpf.microservices.services;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApiService {

	@Value("${tpf.first-check.uri}")
	private String firstCheckUri;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;


	public JsonNode CallApi(JsonNode request) throws JsonProcessingException, HttpClientErrorException {

		URI uri = URI.create(request.path("uri").asText());
		HttpMethod method = HttpMethod.resolve(request.path("method").asText("GET").toUpperCase());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(request.path("content-type").asText("application/json;charset=UTF-8")));
		String body = mapper.writeValueAsString(request.path("body"));
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<?> res = restTemplate.exchange(uri, method, entity, Map.class);
		return mapper.valueToTree(res);
	}
	
}
