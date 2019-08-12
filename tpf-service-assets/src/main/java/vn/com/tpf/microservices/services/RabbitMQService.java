package vn.com.tpf.microservices.services;

import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

//  private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//  @Value("${security.oauth2.client.access-token-uri}")
//  private String accessTokenUri;
//
//  @Value("${security.oauth2.resource.token-info-uri}")
//  private String tokenUri;
//
//  @Value("${security.oauth2.client.client-id}")
//  private String clientId;
//
//  @Value("${security.oauth2.client.client-secret}")
//  private String clientSecret;
//
//  @Autowired
//  private ObjectMapper mapper;
//
//  @Autowired
//  private RabbitTemplate rabbitTemplate;
//
//  @Autowired
//  private RestTemplate restTemplate;
//
//  @Autowired
//  private AppService appService;
//
//  @PostConstruct
//  private void init() {
//    rabbitTemplate.setReplyTimeout(Integer.MAX_VALUE);
//  }
//
//  public JsonNode getToken() {
//    try {
//      URI url = URI.create(accessTokenUri);
//      HttpMethod method = HttpMethod.POST;
//      HttpHeaders headers = new HttpHeaders();
//      headers.setBasicAuth(clientId, clientSecret);
//      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//      String body = "grant_type=client_credentials";
//      HttpEntity<String> entity = new HttpEntity<String>(body, headers);
//      ResponseEntity<?> res = restTemplate.exchange(url, method, entity, Map.class);
//      return mapper.valueToTree(res.getBody());
//    } catch (HttpClientErrorException e) {
//      System.err.println(e.getResponseBodyAsString());
//    }
//
//    return null;
//  }
//
//  public JsonNode checkToken(String[] token) {
//    try {
//      if (token.length == 2) {
//        URI url = URI.create(tokenUri + "?token=" + token[1]);
//        HttpMethod method = HttpMethod.GET;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBasicAuth(clientId, clientSecret);
//        HttpEntity<String> entity = new HttpEntity<String>(headers);
//        ResponseEntity<?> res = restTemplate.exchange(url, method, entity, Map.class);
//        return mapper.valueToTree(res.getBody());
//      }
//    } catch (HttpClientErrorException e) {
//      System.err.println(e.getResponseBodyAsString());
//    }
//
//    return null;
//  }
//
//  public void send(String appId, Object object) throws Exception {
//    Message request = MessageBuilder.withBody(mapper.writeValueAsString(object).getBytes()).build();
//    rabbitTemplate.send(appId, request);
//  }
//
//  public JsonNode sendAndReceive(String appId, Object object) throws Exception {
//    Message request = MessageBuilder.withBody(mapper.writeValueAsString(object).getBytes()).build();
//    Message response = rabbitTemplate.sendAndReceive(appId, request);
//
//    if (response != null) {
//      return mapper.readTree(new String(response.getBody(), "UTF-8"));
//    }
//
//    return null;
//  }
//
//  public Message response(Message message, byte[] payload, Object object) throws Exception {
//    JsonNode obj = mapper.convertValue(object, JsonNode.class);
//    log.info("[==RABBITMQ-LOG==] : {}", Map.of("payload", new String(payload, "UTF-8"), "result",
//        Map.of("status", obj.path("status"), "message", obj.path("data").path("message"))));
//
//    if (message.getMessageProperties().getReplyTo() != null) {
//      return MessageBuilder.withBody(mapper.writeValueAsString(object).getBytes()).build();
//    }
//
//    return null;
//  }
//
//  @RabbitListener(queues = "${spring.rabbitmq.app-id}")
//  public Message onMessage(Message message, byte[] payload) throws Exception {
//    try {
//      JsonNode request = mapper.readTree(new String(payload, "UTF-8"));
//      JsonNode token = checkToken(request.path("token").asText("Bearer ").split(" "));
//
//      if (token == null) {
//        return response(message, payload, Map.of("status", 401, "data", Map.of("message", "Unauthorized")));
//      }
//
//      String scopes = token.path("scope").toString();
//
//      switch (request.path("func").asText()) {
//      case "getListApp":
//        if (scopes.matches(".*(\"tpf-service-app\").*")) {
//          JsonNode info = sendAndReceive("tpf-service-authorization",
//              Map.of("func", "getInfoAccount", "token", request.path("token")));
//          return response(message, payload, appService.getListApp(request, info.path("data")));
//        }
//        break;
//      case "createApp":
//        if (scopes.matches(".*(\"tpf-service-app\").*")) {
//          return response(message, payload, appService.createApp(request));
//        }
//        break;
//      case "updateApp":
//        if (scopes.matches(".*(\"tpf-service-app\").*")) {
//          return response(message, payload, appService.updateApp(request));
//        }
//        break;
//      default:
//        return response(message, payload, Map.of("status", 404, "data", Map.of("message", "Function Not Found")));
//      }
//
//      return response(message, payload, Map.of("status", 403, "data", Map.of("message", "Forbidden")));
//    } catch (IllegalArgumentException e) {
//      return response(message, payload, Map.of("status", 400, "data", Map.of("message", e.getMessage())));
//    } catch (DataIntegrityViolationException e) {
//      return response(message, payload, Map.of("status", 409, "data", Map.of("message", "Conflict")));
//    } catch (Exception e) {
//      return response(message, payload, Map.of("status", 500, "data", Map.of("message", e.getMessage())));
//    }
//  }

}