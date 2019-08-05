package vn.com.tpf.microservices.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.models.Account;

@Service
public class AccountService {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private RabbitMQService rabbitMQService;

  @Autowired
  private MongoTemplate mongoTemplate;

  public Map<String, Object> getListAccount(JsonNode request, JsonNode token) {
    int page = request.path("param").path("page").asInt(1);
    int limit = request.path("param").path("limit").asInt(10);
    String[] sort = request.path("param").path("sort").asText("createdAt,desc").split(",");

    Query query = new Query();
    Criteria criteria = Criteria.where("username").ne(token.path("user_name").asText());

    Account account = getInfoAccount(token.path("user_name").asText());
    if (account != null) {
      criteria.and("departments").in(account.getDepartments());
    }

    if (!request.path("param").path("username").isNull()) {
      criteria.andOperator(Criteria.where("username").regex(request.path("param").path("username").asText()));
    }

    query.addCriteria(criteria);

    long total = mongoTemplate.count(query, Account.class);

    query.with(PageRequest.of(page - 1, limit, Sort.by(Direction.fromString(sort[1]), sort[0])));
    List<Account> list = mongoTemplate.find(query, Account.class);

    return Map.of("status", 200, "data", list, "total", total);
  }

  public Map<String, Object> createAccount(JsonNode request) throws Exception {
    Assert.notNull(request.get("body"), "no body");

    Account body = mapper.treeToValue(request.get("body"), Account.class);

    Assert.hasText(body.getUsername(), "username is required");
    Assert.hasText(body.getEmail(), "email is required");
    Assert.hasText(request.path("body").path("password").asText(), "password is required");
    Assert.hasText(body.getAuthorities(), "authorities is required");
    Assert.notEmpty(body.getDepartments(), "departments is required");
    Assert.notEmpty(body.getProjects(), "projects is required");

    Account entity = new Account();
    entity.setUsername(body.getUsername());
    entity.setEmail(body.getEmail());
    entity.setAuthorities(body.getAuthorities());
    entity.setEnabled(body.isEnabled());
    entity.setProjects(body.getProjects());
    entity.setDepartments(body.getDepartments());

    Map<String, Object> bodyReq = new HashMap<>();
    bodyReq.put("username", entity.getUsername());
    bodyReq.put("password", request.path("body").path("password").asText());
    bodyReq.put("authorities", entity.getAuthorities());
    bodyReq.put("enabled", entity.isEnabled());
    bodyReq.put("credentialsNonExpired", entity.isEnabled());
    bodyReq.put("accountNonLocked", entity.isEnabled());
    bodyReq.put("accountNonExpired", entity.isEnabled());

    Map<String, Object> req = new HashMap<>();
    req.put("func", "createUser");
    req.put("token", request.path("token"));
    req.put("body", bodyReq);
    JsonNode res = rabbitMQService.sendAndReceive("tpf-service-authentication", req);

    if (res.path("status").asInt() == 201) {
      entity.setUserId(res.path("data").path("id").asText());
      mongoTemplate.save(entity);
      return Map.of("status", 201, "data", entity);
    }

    return Map.of("status", res.path("status").asInt(), "data", res.path("data"));
  }

  public Map<String, Object> updateAccount(JsonNode request) throws Exception {
    String id = request.path("param").path("id").asText();
    Assert.hasText(id, "param id is required");
    Assert.notNull(request.get("body"), "no body");

    Account body = mapper.treeToValue(request.get("body"), Account.class);

    Assert.hasText(body.getUsername(), "username is required");
    Assert.hasText(body.getEmail(), "email is required");
    Assert.hasText(body.getAuthorities(), "authorities is required");
    Assert.notEmpty(body.getDepartments(), "departments is required");
    Assert.notEmpty(body.getProjects(), "projects is required");

    Account entity = mongoTemplate.findById(id, Account.class);

    if (entity == null) {
      return Map.of("status", 404, "data", Map.of("message", "Not Found"));
    }

    entity.setUsername(body.getUsername());
    entity.setEmail(body.getEmail());
    entity.setAuthorities(body.getAuthorities());
    entity.setEnabled(body.isEnabled());
    entity.setProjects(body.getProjects());
    entity.setDepartments(body.getDepartments());

    Map<String, Object> bodyReq = new HashMap<>();
    bodyReq.put("username", entity.getUsername());
    bodyReq.put("authorities", entity.getAuthorities());
    bodyReq.put("enabled", entity.isEnabled());
    bodyReq.put("credentialsNonExpired", entity.isEnabled());
    bodyReq.put("accountNonLocked", entity.isEnabled());
    bodyReq.put("accountNonExpired", entity.isEnabled());

    if (!request.path("body").path("password").isNull()) {
      bodyReq.put("password", request.path("body").path("password").asText());
    }

    Map<String, Object> req = new HashMap<>();
    req.put("func", "updateUser");
    req.put("token", request.path("token"));
    req.put("param", Map.of("id", entity.getUserId()));
    req.put("body", bodyReq);
    JsonNode res = rabbitMQService.sendAndReceive("tpf-service-authentication", req);

    if (res.path("status").asInt() == 200) {
      mongoTemplate.save(entity);
      return Map.of("status", 200, "data", entity);
    }

    return Map.of("status", res.path("status").asInt(), "data", res.path("data"));
  }

  public Map<String, Object> deleteAccount(JsonNode request) throws Exception {
    String id = request.path("param").path("id").asText();
    Assert.hasText(id, "param id is required");

    Account entity = mongoTemplate.findById(id, Account.class);

    if (entity == null) {
      return Map.of("status", 404, "data", Map.of("message", "Not  Found"));
    }

    Map<String, Object> req = new HashMap<>();
    req.put("func", "deleteUser");
    req.put("token", request.path("token"));
    req.put("param", Map.of("id", entity.getUserId()));
    JsonNode res = rabbitMQService.sendAndReceive("tpf-service-authentication", req);

    if (res.path("status").asInt() == 200) {
      mongoTemplate.remove(entity);
      return Map.of("status", 200, "data", entity);
    }

    return Map.of("status", res.path("status").asInt(), "data", res.path("data"));
  }

  public Account getInfoAccount(String username) {
    return mongoTemplate.findOne(Query.query(Criteria.where("username").is(username)), Account.class);
  }

}