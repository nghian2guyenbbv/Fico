package vn.com.tpf.microservices.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	private MomoService momoService;

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
			case "createMomo":
				return response(message, payload, momoService.createMomo(request));
			case "getDetail":
				return response(message, payload, momoService.getDetail(request));
			case "updateAutomation":
				return response(message, payload, momoService.updateAutomation(request));
			case "updateSms":
				return response(message, payload, momoService.updateSms(request));
			case "updateStatus":
				return response(message, payload, momoService.updateStatus(request));
			default:
				return response(message, payload,Map.of("status", 500, "data",  ExceptionRespone(payload, 500, "function not found")));
			}

		} catch (IllegalArgumentException e) {
			return response(message, payload, Map.of("status", 500, "data",  ExceptionRespone(payload, 500, e.toString())));
		} catch (DataIntegrityViolationException e) {
			return response(message, payload, Map.of("status", 400, "data", ExceptionRespone(payload, 1, "lead exits")));
		} catch (Exception e) {
			return response(message, payload, Map.of("status", 500, "data",  ExceptionRespone(payload, 500, e.toString())));
		}
		
	}
	
	private Object ExceptionRespone( byte[] payload,int result_code ,String message) throws UnsupportedEncodingException, IOException {
		JsonNode request = mapper.readTree(new String(payload, "UTF-8"));
		log.info("{}",request);
		return  Map.of("result_code", result_code,"request_id", request.path("body").path("request_id").asText(), "reference_id", request.path("body").path("reference_id").asText(),"message", message);

	}

}