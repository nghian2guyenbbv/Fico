package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Map;

@Service
public class RabbitMQService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${security.oauth2.resource.token-info-uri}")
	private String tokenUri;

	@Value("${security.oauth2.client.client-id}")
	private String clientId;

	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DataEntryService dataEntryService;

	@PostConstruct
	private void init() {
		rabbitTemplate.setReplyTimeout(Integer.MAX_VALUE);
	}

	public JsonNode checkToken(String[] token) {
		try {
			if (token.length == 2) {
				URI url = URI.create(tokenUri + "?token=" + token[1]);
				HttpMethod method = HttpMethod.GET;
				HttpHeaders headers = new HttpHeaders();
				headers.setBasicAuth(clientId, clientSecret);
				HttpEntity<?> entity = new HttpEntity<>(headers);
				ResponseEntity<?> res = restTemplate.exchange(url, method, entity, Map.class);
				return mapper.valueToTree(res.getBody());
			}
		} catch (HttpClientErrorException e) {
			System.err.println(e.getResponseBodyAsString());
		}

		return null;
	}

	public void send(String appId, Object object) throws Exception {
		Message request = MessageBuilder.withBody(mapper.writeValueAsString(object).getBytes()).build();
		rabbitTemplate.send(appId, request);
	}

	public JsonNode sendAndReceive(String appId, Object object) throws Exception {
		Message request = MessageBuilder.withBody(mapper.writeValueAsString(object).getBytes()).build();
		Message response = rabbitTemplate.sendAndReceive(appId, request);

		if (response != null) {
			return mapper.readTree(new String(response.getBody(), "UTF-8"));
		}

		return null;
	}

	public Message response(Message message, byte[] payload, Object object) throws Exception {
		ObjectNode dataLog = mapper.createObjectNode();
		dataLog.put("type", "[==RABBITMQ-LOG==]");
		dataLog.set("result", mapper.convertValue(object, JsonNode.class));
		try {
			dataLog.set("payload", mapper.readTree(new String(payload, "UTF-8")));
		} catch (Exception e) {
			dataLog.put("payload", new String(payload, "UTF-8"));
		}
		log.info("{}", dataLog);

		if (message.getMessageProperties().getReplyTo() != null) {
			return MessageBuilder.withBody(mapper.writeValueAsString(object).getBytes()).build();
		}

		return null;
	}

	@RabbitListener(queues = "${spring.rabbitmq.app-id}")
	public Message onMessage(Message message, byte[] payload) throws Exception {
		try {
//			mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			JsonNode request = mapper.readTree(new String(payload, "UTF-8"));
			JsonNode token = checkToken(request.path("token").asText("Bearer ").split(" "));

//			if (token == null) {
//				return response(message, payload, Map.of("status", 401, "data", Map.of("message", "Unauthorized")));
//			}
//
//			String scopes = token.path("scope").toString();

			switch (request.path("func").asText()) {
				case "addProduct":
					return response(message, payload, dataEntryService.addProduct(request));
				case "getProductByName":
					return response(message, payload, dataEntryService.getProductByName(request));
				case "getAll":
					return response(message, payload, dataEntryService.getAll(request));
				case "getByAppId":
					return response(message, payload, dataEntryService.getByAppId(request));
				case "getDetail":
					return response(message, payload, dataEntryService.getDetail(request, token));
				case "getAddress":
					return response(message, payload, dataEntryService.getAddress(request));
				case "getBranch":
					return response(message, payload, dataEntryService.getBranch(request));
				case "getBranchByUser":
					JsonNode info = sendAndReceive("tpf-service-authorization",
							Map.of("func", "getInfoAccount", "token", request.path("token")));
					return response(message, payload, dataEntryService.getBranchByUser(request, info.path("data")));
				case "firstCheck":
					return response(message, payload, dataEntryService.firstCheck(request, token));
				case "quickLead":
					return response(message, payload, dataEntryService.quickLead(request, token));
				case "sendApp":
					return response(message, payload, dataEntryService.sendApp(request, token));
				case "updateApp":
					return response(message, payload, dataEntryService.updateApp(request, token));
				case "commentApp":
					return response(message, payload, dataEntryService.commentApp(request, token));
				case "cancelApp":
					return response(message, payload, dataEntryService.updateStatus(request, token));
				case "uploadFile":
					return response(message, payload, dataEntryService.uploadFile(request, token));
				case "updateAutomation":
					return response(message, payload, dataEntryService.updateAutomation(request, token));
				case "updateFullApp":
					return response(message, payload, dataEntryService.updateFullApp(request, token));
				case "updateAppError":
					return response(message, payload, dataEntryService.updateAppError(request, token));
				case "uploadDigiTex":
					return response(message, payload, dataEntryService.uploadDigiTex(request, token));
				case "getTATReport":
					return response(message, payload, dataEntryService.getTATReport(request, token));
				case "getStatusReport":
					return response(message, payload, dataEntryService.getStatusReport(request, token));
				case "getDocumentId":
					return response(message, payload, dataEntryService.getDocumentId(request, token));
				default:
					return response(message, payload, Map.of("status", 404, "data", Map.of("message", "Function Not Found")));
			}
		} catch (IllegalArgumentException e) {
			return response(message, payload, Map.of("status", 400, "data", Map.of("message", e.getMessage())));
		} catch (DataIntegrityViolationException e) {
			return response(message, payload, Map.of("status", 409, "data", Map.of("message", "Conflict")));
		} catch (Exception e) {
			return response(message, payload, Map.of("status", 500, "data", Map.of("message", e.getMessage())));
		}
	}

}