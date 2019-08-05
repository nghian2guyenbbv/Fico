package vn.com.tpf.microservices.services;

import java.net.URI;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

@Service
public class RabbitMQService {

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

  @PostConstruct
  public void init() {
    rabbitTemplate.setReplyTimeout(Integer.MAX_VALUE);
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

  @RabbitListener(queues = "${spring.rabbitmq.app-id}")
  public Message onMessage(Message message, byte[] payload) throws Exception {
    System.out.println(new String(payload));
    try {
      JsonNode request = mapper.readTree(new String(payload));

      switch (request.path("func").asText()) {
      case "func":
        break;
      default:
        return response(message, Map.of("status", 404, "data", Map.of("message", "Function Not Found")));
      }

      return response(message, Map.of("status", 403, "data", Map.of("message", "Forbidden")));
    } catch (IllegalArgumentException e) {
      System.err.println(e);
      return response(message, Map.of("status", 400, "data", Map.of("message", e.getMessage())));
    } catch (DataIntegrityViolationException e) {
      System.err.println(e);
      return response(message, Map.of("status", 409, "data", Map.of("message", "Conflict")));
    } catch (Exception e) {
      System.err.println(e);
      return response(message, Map.of("status", 500, "data", Map.of("message", e.getMessage())));
    }
  }

}