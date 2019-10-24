package vn.com.tpf.microservices.services;

import java.net.URI;
import java.util.Map;

import javax.annotation.PostConstruct;

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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
	private UserService userService;

	@Autowired
	private ClientService clientService;

	@PostConstruct
	private void init() {
		rabbitTemplate.setReplyTimeout(Integer.MAX_VALUE);
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

		return mapper.createObjectNode();
	}

	public Message response(Message message, byte[] payload, Object object) throws Exception {
		JsonNode obj = mapper.convertValue(object, JsonNode.class);
		ObjectNode dataLog = mapper.createObjectNode();
		dataLog.put("type", "[==RABBITMQ-LOG==]");
		dataLog.set("result", mapper.convertValue(
				Map.of("status", obj.path("status"), "message", obj.path("data").path("message")), JsonNode.class));
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
			String[] token = request.path("token").asText("Bearer ").split(" ");

			switch (request.path("func").asText()) {
			case "getListUser":
				return response(message, payload, userService.getListUser(request));
			case "createUser":
				return response(message, payload, userService.createUser(request, checkToken(token)));
			case "updateUser":
				return response(message, payload, userService.updateUser(request, checkToken(token)));
			case "deleteUser":
				return response(message, payload, userService.deleteUser(request, checkToken(token)));
			case "logout":
				return response(message, payload, userService.logoutUser(checkToken(token)));
			case "changePassword":
				return response(message, payload, userService.changePasswordUser(request, checkToken(token)));
			case "getListClient":
				return response(message, payload, clientService.getListClient(request));
			case "createClient":
				return response(message, payload, clientService.createClient(request));
			case "updateClient":
				return response(message, payload, clientService.updateClient(request));
			case "deleteClient":
				return response(message, payload, clientService.deleteClient(request));
			default:
				return response(message, payload, Map.of("status", 404, "data", Map.of("message", "Function Not Found")));
			}

		} catch (IllegalArgumentException e) {
			return response(message, payload, Map.of("status", 400, "data", Map.of("message", e.getMessage())));
		} catch (DataIntegrityViolationException e) {
			return response(message, payload, Map.of("status", 409, "data", Map.of("message", "Conflict")));
		} catch (Exception e) {
			return response(message, payload, Map.of("status", 500, "data", Map.of("message", e.toString())));
		}
	}

}