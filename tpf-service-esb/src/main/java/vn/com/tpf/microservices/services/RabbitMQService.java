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
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class RabbitMQService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private AppService appService;

	@PostConstruct
	private void init() {
		rabbitTemplate.setReplyTimeout(60000);
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

			switch (request.path("func").asText()) {
			case "createApp":
				return response(message, payload, appService.createApp(request));
			case "createQuickLeadApp":
				return response(message, payload, appService.createQuickLeadApp(request));
			case "waiveField":
				return response(message, payload, appService.waiveField(request));
			case "submitField":
				return response(message, payload, appService.submitField(request));
			case "updateApp":
				return response(message, payload, appService.updateApp(request));
			case "updateAutomation":
				return response(message, payload, appService.updateAutomation(request));
			case "deResponseQuery":
				return response(message, payload, appService.deResponseQuery(request));
			case "deSaleQueue":
				return response(message, payload, appService.deSaleQueue(request));
			case "getReason":
				return response(message, payload, appService.getReason(request));
			case "getLoan":
				return response(message, payload, appService.getLoan(request));
			case "getCheckList":
				return response(message, payload, appService.getCheckList(request));
			case "createQuickLeadAppWithCustId":
				return response(message, payload, appService.createQuickLeadAppWithCustId(request));
            case "saleQueueWithFullInfo":
            	return response(message, payload, appService.saleQueueWithFullInfo(request));
			default:
				return response(message, payload, appService.sendFinnOne(request));
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