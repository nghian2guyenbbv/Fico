package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class RabbitMQService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

//	@Value("${security.oauth2.client.access-token-uri}")
//	private String accessTokenUri;
//
//	@Value("${security.oauth2.resource.token-info-uri}")
//	private String tokenUri;
//
//	@Value("${security.oauth2.client.client-id}")
//	private String clientId;
//
//	@Value("${security.oauth2.client.client-secret}")
//	private String clientSecret;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AutomationService automationService;

	@PostConstruct
	private void init() {
		rabbitTemplate.setReplyTimeout(30000);
	}

//	public JsonNode getToken() {
//		try {
//			URI url = URI.create(accessTokenUri);
//			HttpMethod method = HttpMethod.POST;
//			HttpHeaders headers = new HttpHeaders();
//			headers.setBasicAuth(clientId, clientSecret);
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			String body = "grant_type=client_credentials";
//			HttpEntity<?> entity = new HttpEntity<>(body, headers);
//			ResponseEntity<?> res = restTemplate.exchange(url, method, entity, Map.class);
//			return mapper.valueToTree(res.getBody());
//		} catch (HttpClientErrorException e) {
//			System.err.println(e.getResponseBodyAsString());
//		}
//
//		return null;
//	}
//
//	public JsonNode checkToken(String[] token) {
//		try {
//			if (token.length == 2) {
//				URI url = URI.create(tokenUri + "?token=" + token[1]);
//				HttpMethod method = HttpMethod.GET;
//				HttpHeaders headers = new HttpHeaders();
//				headers.setBasicAuth(clientId, clientSecret);
//				HttpEntity<?> entity = new HttpEntity<>(headers);
//				ResponseEntity<?> res = restTemplate.exchange(url, method, entity, Map.class);
//				return mapper.valueToTree(res.getBody());
//			}
//		} catch (HttpClientErrorException e) {
//			System.err.println(e.getResponseBodyAsString());
//		}
//
//		return null;
//	}

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
			JsonNode request = mapper.readTree(new String(payload, "UTF-8"));
			String project="";



//			JsonNode token = checkToken(request.path("token").asText("Bearer ").split(" "));
//
//			if (token == null) {
//				return response(message, payload, Map.of("status", 401, "data", Map.of("message", "Unauthorized")));
//			}
//
//			String scopes = token.path("scope").toString();

			switch (request.path("func").asText()) {
			case "quickLeadApp":
				project=request.path("body").path("project").asText();
				if(project.equals("smartnet")) {
					return response(message, payload, automationService.SN_quickLeadApp(request));
				}
				else if (project.equals("mobility")){
					return response(message, payload, automationService.MOBILITY_quickLeadApp(request));
				}else if (project.equals("crm")){
					String checkCustID = request.path("body").path("neoCustID").textValue();
					String checkCifNumber = request.path("body").path("cifNumber").textValue();
					String checkIdNumber = request.path("body").path("idNumber").textValue();
					if (StringUtils.isEmpty(checkCustID) && StringUtils.isEmpty(checkCifNumber) && StringUtils.isEmpty(checkIdNumber)){
						return response(message, payload, automationService.MOBILITY_quickLeadApp(request));
					}else{
						return response(message, payload, automationService.CRM_quickLeadAppWithCustID(request));
					}
				}
				else
				{
					return response(message, payload, automationService.quickLeadApp(request));
				}
			case "fullInfoApp":
					return response(message, payload, automationService.fullInfoApp(request));
			case "updateAppError":
					return response(message, payload, automationService.updateAppError(request));
			case "createApp":
				project=request.path("body").path("project").asText();
				if(project.equals("momo")) {
					return response(message, payload, automationService.momoCreateApp(request));
				}
				else
				{
					return response(message, payload, automationService.fptCreateApp(request));
				}
			case "autoAssign":
					return response(message, payload, automationService.DE_AutoAssign(request));
			case "deResponseQuery":
				return response(message, payload, automationService.DE_ResponseQuery(request));
			case "deSaleQueue":
				return response(message, payload, automationService.DE_SaleQueue(request));
			case "waiveField":
				return response(message, payload, automationService.Waive_Field(request));
			case "submitField":
				return response(message, payload, automationService.Submit_Field(request));
			case "existingCustomer":
				return response(message, payload, automationService.Existing_Customer(request));
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