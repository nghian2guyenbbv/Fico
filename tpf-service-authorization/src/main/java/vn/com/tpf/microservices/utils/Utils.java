package vn.com.tpf.microservices.utils;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public  class  Utils {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${security.oauth2.client.access-token-uri}")
	private String accessTokenUri;

	@Value("${security.oauth2.resource.token-info-uri}")
	private String tokenUri;

	@Value("${security.oauth2.client.client-id}")
	private String clientId;

	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;


	
	public  JsonNode getToken(String username, String password) {
		URI url = URI.create(accessTokenUri);
		HttpMethod method = HttpMethod.POST;
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(clientId, clientSecret);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String body = "username=" + username + "&password=" + password + "&grant_type=password";
		HttpEntity<?> entity = new HttpEntity<>(body, headers);
		try {
			ResponseEntity<?> res = restTemplate.exchange(url, method, entity, Map.class);
			return mapper.valueToTree(res.getBody());
		} catch (HttpClientErrorException e) {
			log.error("{} {} {}", username, password, e.getResponseBodyAsString());
			return null;
		}
	}
	
	public JsonNode checkToken(String[] token) {
		if (token.length != 2) {
			return mapper.createObjectNode();
		}
		URI url = URI.create(tokenUri + "?token=" + token[1]);
		HttpMethod method = HttpMethod.GET;
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(clientId, clientSecret);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<?> res = restTemplate.exchange(url, method, entity, Map.class);
		return mapper.valueToTree(res.getBody());
	}
	
	public  JsonNode getJsonNodeResponse(int resultCode,JsonNode body, JsonNode data) {
		ObjectNode res = mapper.createObjectNode();
		res.put("result_code", resultCode);
		res.put("request_id", body.path("request_id").asText());
		res.put("reference_id", body.path("reference_id").asText());
		res.put("date_time",ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		if (!Objects.isNull(data))
			if(resultCode == 0)
				res.set("data", data);
			else 
				res.put("message", data.path("message").asText());
		return  res;
	}
}
