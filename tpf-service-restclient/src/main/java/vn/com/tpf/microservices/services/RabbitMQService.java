package vn.com.tpf.microservices.services;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RabbitMQService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitTemplate rabbitTemplate;

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
		JsonNode obj = mapper.convertValue(object, JsonNode.class);
		log.info("[==RABBITMQ-LOG==] : {}", Map.of("payload", new String(payload, "UTF-8"), "result",
				Map.of("status", obj.path("status"), "message", obj.path("data").path("message"))));

		if (message.getMessageProperties().getReplyTo() != null) {
			return MessageBuilder.withBody(mapper.writeValueAsString(object).getBytes()).build();
		}

		return null;
	}

	@RabbitListener(queues = "${spring.rabbitmq.app-id}")
	public Message onMessage(Message message, byte[] payload) throws Exception {
		try {
			JsonNode request = mapper.readTree(new String(payload, "UTF-8"));

			switch (request.path("func").asText()) {
			case "func":
				break;
			default:
				return response(message, payload, Map.of("status", 404, "data", Map.of("message", "Function Not Found")));
			}

			return response(message, payload, Map.of("status", 403, "data", Map.of("message", "Forbidden")));
		} catch (IllegalArgumentException e) {
			return response(message, payload, Map.of("status", 400, "data", Map.of("message", e.getMessage())));
		} catch (DataIntegrityViolationException e) {
			return response(message, payload, Map.of("status", 409, "data", Map.of("message", "Conflict")));
		} catch (Exception e) {
			return response(message, payload, Map.of("status", 500, "data", Map.of("message", e.getMessage())));
		}
	}
}