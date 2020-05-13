
package vn.com.tpf.microservices.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.Authenticator;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class SmartnetController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.queue.dataentry-sgb}")
	private String queueDESGB;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Value("${spring.url.uploadfile}")
	private String urlUploadfile;

	
	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate = new RestTemplate(factory);
	}

	@PostMapping("/smartnet/preCheck1")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> preCheck1(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "preCheck1");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/smartnet/preCheck2")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> preCheck2(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "preCheck2");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/smartnet/getAppInfo")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> getAppInfo(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getAppInfo");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/smartnet/addDocuments")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	ResponseEntity<?> addDocuments(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "addDocuments");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/smartnet/returnQuery")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> returnQuery(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "returnQuery");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/smartnet/returnQueue")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-smartnet')")
	public ResponseEntity<?> returnQueue(@RequestBody ObjectNode body) throws Exception {
		body.put("reference_id", UUID.randomUUID().toString());
		Map<String, Object> request = new HashMap<>();
		request.put("func", "returnQueue");
		request.put("body", body);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-smartnet", request);
		return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
	}

	@PostMapping("/v1/smarnet/uploadfile/")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-smartnet','tpf-service-root')")
	public ResponseEntity<?> uploadfile(@RequestPart("files") MultipartFile[] files) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		ResponseEntity<?> res = new ResponseEntity<Authenticator.Success>(HttpStatus.CREATED);

		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		for (MultipartFile item : files) {
			parts.add("file", item.getResource());
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<?> entity = new HttpEntity<>(parts, headers);
		res = restTemplate.postForEntity(urlUploadfile, entity, List.class);

		ObjectNode dataLog = mapper.createObjectNode();
		dataLog.put("type", "[==HTTP-LOG==]");
		dataLog.set("result", mapper.convertValue(res, JsonNode.class));
		log.info("{}", dataLog);

		return ResponseEntity.status(res.getStatusCode()).body(res);

	}
}
