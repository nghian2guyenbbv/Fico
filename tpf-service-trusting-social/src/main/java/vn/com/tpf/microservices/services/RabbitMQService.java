package vn.com.tpf.microservices.services;

import java.net.URI;
import java.util.Map;

import javax.annotation.PostConstruct;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class RabbitMQService {
	
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
  private RabbitTemplate rabbitTemplate;
  

  @Autowired
  private RestTemplate restTemplate;
  
  
  @Autowired
  private RestTemplate restTemplate1;

  @Autowired
  private TrustingSocialFirstCheckService trustingSocialFirstCheckService;

  @PostConstruct
  private void init() {
    rabbitTemplate.setReplyTimeout(Integer.MAX_VALUE);
  }

  public JsonNode getToken(String username, String password) {
    try {
      URI url = URI.create(accessTokenUri);
      HttpMethod method = HttpMethod.POST;
      HttpHeaders headers = new HttpHeaders();
      headers.setBasicAuth(clientId, clientSecret);
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      String body = "username=" + username + "&password=" + password + "&grant_type=password";
      HttpEntity<String> entity = new HttpEntity<String>(body, headers);
      ResponseEntity<?> res = restTemplate1.exchange(url, method, entity, Map.class);
      return mapper.valueToTree(res.getBody());
    } catch (HttpClientErrorException e) {
      log.error(e.getResponseBodyAsString());
    }
    return null;
  }

  public JsonNode checkToken(String[] token) {
    try {
      if (token.length == 2) {
        URI url = URI.create(tokenUri + "?token=" + token[1]);
        HttpMethod method = HttpMethod.GET;
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<?> res = restTemplate.exchange(url, method, entity, Map.class);
        return mapper.valueToTree(res.getBody());
      }
    } catch (HttpClientErrorException e) {
    	log.error(e.getResponseBodyAsString());
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
      return mapper.readTree(new String(response.getBody()));
    }
    return null;
  }

  public Message response(Message message, Object object) throws Exception {
    if (message.getMessageProperties().getReplyTo() != null) {
      return MessageBuilder.withBody(mapper.writeValueAsString(object).getBytes()).build();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @RabbitListener(queues = "${spring.rabbitmq.app-id}")
  public Message onMessage(Message message, byte[] payload) throws Exception {
	log.info(new String(payload));
    try {
      JsonNode request = mapper.readTree(new String(payload));

      JsonNode token = checkToken(request.path("token").asText("Bearer ").split(" "));

      if (token == null) {
        return response(message, Map.of("status", 401, "data", Map.of("message", "Unauthorized")));
      }

      String scopes = token.path("scope").toString();
      String authorities = token.path("authorities").toString();

      switch (request.path("func").asText()) {
      case "getFirstCheck":
        if (scopes.matches(".*(\"tpf-service-trusting-social\").*")
            && authorities.matches(".*(\"role_root\"|\"role_admin\").*")) {
        	
        	Map<?,?> data = null;
        	
          return response(message, data);
        }
        break;
      default:
        return response(message, Map.of("status", 404, "data", Map.of("message", "Function Not Found")));
      }

      return response(message, Map.of("status", 403, "data", Map.of("message", "Forbidden")));
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return response(message, Map.of("status", 400, "data", Map.of("message", e.getMessage())));
    } catch (DataIntegrityViolationException e) {
      e.printStackTrace();
      return response(message, Map.of("status", 409, "data", Map.of("message", "Conflict")));
    } catch (Exception e) {
      e.printStackTrace();
      return response(message, Map.of("status", 500, "data", Map.of("message", e.getMessage())));
    }
  }

}