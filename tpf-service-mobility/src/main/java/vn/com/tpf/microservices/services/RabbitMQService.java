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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.utils.Utils;

@Service
public class RabbitMQService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private MobilityService mobilityService;

	@Autowired
	private Utils utils;

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
				case "preCheck1":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.getPreCheck1(request)));
				case "preCheck2":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.getPreCheck2(request)));
				case "getAppInfo":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.getAppInfo(request)));	
				case "returnQuery":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.returnQuery(request)));
				case "returnQueue":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.returnQueue(request)));
				case "addDocuments":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.addDocuments((request))));		
				case "updateAutomation":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.updateAutomation((request))));	
				case "createField":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.createField((request))));	
				case "submitField": 
					return response(message, payload, Map.of("status", 200, "data", mobilityService.submitField((request))));	
				case "waiveField": 
					return response(message, payload, Map.of("status", 200, "data", mobilityService.waiveField((request))));
				case "commentApp":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.commentApp((request))));
				case "convertToLeadF1":
					return response(message, payload, Map.of("status", 200, "data", mobilityService.convertAndCallAPIF1((request))));
				default:
					return response(message, payload, Map.of("status", 200, "data", utils.getJsonNodeResponse(500, request,
							mapper.createObjectNode().put("message", "function not found"))));
			}				

		} catch (Exception e) {
			return response(message, payload,
					Map.of("status", 200, "data",
							utils.getJsonNodeResponse(500, mapper.readTree(new String(payload, "UTF-8")),
									mapper.createObjectNode().put("message", e.toString()))));
		}

	}

}