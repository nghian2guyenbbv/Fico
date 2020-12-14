package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Map;

@Service
public class RabbitMQService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AutoAllocationService autoAllocationService;

	@Value("${security.oauth2.resource.token-info-uri}")
	private String tokenUri;

	@Value("${security.oauth2.client.client-id}")
	private String clientId;

	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;

	@PostConstruct
	private void init() {
		rabbitTemplate.setReplyTimeout(Integer.MAX_VALUE);
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
//		JsonNode obj = mapper.convertValue(object, JsonNode.class);
//		log.info("[==RABBITMQ-LOG==] : {}", Map.of("payload", new String(payload, "UTF-8"), "result", obj));

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
			JsonNode request = mapper.readTree(new String(payload, "UTF-8"));
			JsonNode token = checkToken(request.path("token").asText("Bearer ").split(" "));
			//String userName = token.path("user_name").asText("");

			switch (request.path("func").asText()) {
				case "historyAllocation":
					return response(message, payload, autoAllocationService.getHistoryApp(request) );
				case "assignConfig":
					return response(message, payload, autoAllocationService.getAssignConfig());
				case "setAssignConfig":
					return response(message, payload, autoAllocationService.setAssignConfig(request));
				case "ETLPushData":
					return response(message, payload, autoAllocationService.sendAppFromF1(request));
				case "uploadUser":
					return response(message, payload, autoAllocationService.uploadUser(request, token));
				case "addUser":
					return response(message, payload, autoAllocationService.addUser(request, token));
				case "getAllUser":
					return response(message, payload, autoAllocationService.getAllUser(request, token));
				case "getInfoUserLogin":
					return response(message, payload, autoAllocationService.getInfoUserLogin(request));
				case "removeUser":
					return response(message, payload, autoAllocationService.removeUser(request, token));
				case "changeActiveUser":
					return response(message, payload, autoAllocationService.changeActiveUser(request, token));
				case "getDashboard":
					return response(message, payload, autoAllocationService.getDashboard(request));
				case "getPending":
					return response(message, payload, autoAllocationService.getPending(request));
				case "updatePending":
					return response(message, payload, autoAllocationService.updatePending(request, token));
				case "updateStatusApp":
					return response(message, payload, autoAllocationService.updateStatusApp(request));
				case "getAllPendingCode":
					return response(message, payload, autoAllocationService.getAllPendingCode(request));
				case "holdApplication":
					return response(message, payload, autoAllocationService.holdApplication(request, token));
				case "updateVendor":
					return response(message, payload, autoAllocationService.updateVendor(request));
				case "updateRaiseQuery":
					return response(message, payload, autoAllocationService.updateRaiseQuery(request));
				case "reassign":
					return response(message, payload, autoAllocationService.reassign(request, token));
				case "updateLoginStatus":
					return response(message, payload, autoAllocationService.updateLoginStatus(request));
				case "updateLeader":
					return response(message, payload, autoAllocationService.updateLeader(request, token));
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

	public JsonNode checkToken(String[] token) {
		try {
			if (token.length == 2) {
				URI url = URI.create(tokenUri + "?token=" + token[1]);
				HttpMethod method = HttpMethod.GET;
				org.springframework.http.HttpHeaders headers = new HttpHeaders();
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

}