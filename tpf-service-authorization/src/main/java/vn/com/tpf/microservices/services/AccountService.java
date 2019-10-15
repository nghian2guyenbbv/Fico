package vn.com.tpf.microservices.services;

import java.util.List;

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
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Account;

@Service
public class AccountService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private MongoTemplate mongoTemplate;

	private JsonNode response(int status, JsonNode data, long total) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).put("total", total).set("data", data);
		return response;
	}

	public JsonNode getListAccount(JsonNode request, JsonNode token) {
		int page = request.path("param").path("page").asInt(1);
		int limit = request.path("param").path("limit").asInt(10);
		String[] sort = request.path("param").path("sort").asText("createdAt,desc").split(",");

		Query query = new Query();
		Criteria criteria = Criteria.where("username").ne(token.path("user_name").asText());

		Account account = getInfoAccount(token.path("user_name").asText());
		if (account != null) {
			criteria.and("departments").in(account.getDepartments());
			criteria.and("branches").in(account.getBranches());
		}

		if (request.path("param").path("username").isTextual()) {
			criteria.andOperator(Criteria.where("username").regex(request.path("param").path("username").asText()));
		}

		query.addCriteria(criteria);

		long total = mongoTemplate.count(query, Account.class);

		query.with(PageRequest.of(page - 1, limit, Sort.by(Direction.fromString(sort[1]), sort[0])));
		List<Account> list = mongoTemplate.find(query, Account.class);

		return response(200, mapper.convertValue(list, JsonNode.class), total);
	}

	public JsonNode createAccount(JsonNode request) throws Exception {
		Assert.notNull(request.get("body"), "no body");

		Account body = mapper.treeToValue(request.get("body"), Account.class);

		Assert.hasText(body.getUsername(), "username is required");
		Assert.hasText(body.getEmail(), "email is required");
		Assert.hasText(request.path("body").path("password").asText(), "password is required");
		Assert.hasText(body.getAuthorities(), "authorities is required");
		Assert.notEmpty(body.getDepartments(), "departments is required");
		Assert.notEmpty(body.getProjects(), "projects is required");
		Assert.notEmpty(body.getBranches(), "branches is required");

		Account entity = new Account();
		entity.setUsername(body.getUsername());
		entity.setEmail(body.getEmail());
		entity.setAuthorities(body.getAuthorities());
		entity.setEnabled(body.isEnabled());
		entity.setProjects(body.getProjects());
		entity.setDepartments(body.getDepartments());
		entity.setBranches(body.getBranches());
		entity.setOptional(body.getOptional());

		ObjectNode bodyReq = mapper.createObjectNode();
		bodyReq.put("username", entity.getUsername());
		bodyReq.put("password", request.path("body").path("password").asText());
		bodyReq.put("authorities", entity.getAuthorities());
		bodyReq.put("enabled", entity.isEnabled());
		bodyReq.put("credentialsNonExpired", entity.isEnabled());
		bodyReq.put("accountNonLocked", entity.isEnabled());
		bodyReq.put("accountNonExpired", entity.isEnabled());

		ObjectNode req = mapper.createObjectNode();
		req.put("func", "createUser");
		req.set("token", request.path("token"));
		req.set("body", bodyReq);
		JsonNode res = rabbitMQService.sendAndReceive("tpf-service-authentication", req);

		if (res.path("status").asInt() == 201) {
			entity.setUserId(res.path("data").path("id").asText());
			mongoTemplate.save(entity);
			return response(201, mapper.convertValue(entity, JsonNode.class), 0);
		}

		return response(res.path("status").asInt(), res.path("data"), 0);
	}

	public JsonNode updateAccount(JsonNode request) throws Exception {
		String id = request.path("param").path("id").asText();
		Assert.hasText(id, "param id is required");
		Assert.notNull(request.get("body"), "no body");

		Account body = mapper.treeToValue(request.get("body"), Account.class);

		Assert.hasText(body.getUsername(), "username is required");
		Assert.hasText(body.getEmail(), "email is required");
		Assert.hasText(body.getAuthorities(), "authorities is required");
		Assert.notEmpty(body.getDepartments(), "departments is required");
		Assert.notEmpty(body.getProjects(), "projects is required");
		Assert.notEmpty(body.getBranches(), "branches is required");

		Account entity = mongoTemplate.findById(id, Account.class);

		if (entity == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"), 0);
		}

		entity.setUsername(body.getUsername());
		entity.setEmail(body.getEmail());
		entity.setAuthorities(body.getAuthorities());
		entity.setEnabled(body.isEnabled());
		entity.setProjects(body.getProjects());
		entity.setDepartments(body.getDepartments());
		entity.setBranches(body.getBranches());
		entity.setOptional(body.getOptional());

		ObjectNode bodyReq = mapper.createObjectNode();
		bodyReq.put("username", entity.getUsername());
		bodyReq.put("authorities", entity.getAuthorities());
		bodyReq.put("enabled", entity.isEnabled());
		bodyReq.put("credentialsNonExpired", entity.isEnabled());
		bodyReq.put("accountNonLocked", entity.isEnabled());
		bodyReq.put("accountNonExpired", entity.isEnabled());

		if (request.path("body").path("password").isTextual()) {
			bodyReq.put("password", request.path("body").path("password").asText());
		}

		ObjectNode req = mapper.createObjectNode();
		req.put("func", "updateUser");
		req.set("token", request.path("token"));
		req.set("param", mapper.createObjectNode().put("id", entity.getUserId()));
		req.set("body", bodyReq);
		JsonNode res = rabbitMQService.sendAndReceive("tpf-service-authentication", req);

		if (res.path("status").asInt() == 200) {
			mongoTemplate.save(entity);
			return response(200, mapper.convertValue(entity, JsonNode.class), 0);
		}

		return response(res.path("status").asInt(), res.path("data"), 0);
	}

	public JsonNode deleteAccount(JsonNode request) throws Exception {
		String id = request.path("param").path("id").asText();
		Assert.hasText(id, "param id is required");

		Account entity = mongoTemplate.findById(id, Account.class);

		if (entity == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"), 0);
		}

		ObjectNode req = mapper.createObjectNode();
		req.put("func", "deleteUser");
		req.set("token", request.path("token"));
		req.set("param", mapper.createObjectNode().put("id", entity.getUserId()));
		JsonNode res = rabbitMQService.sendAndReceive("tpf-service-authentication", req);

		if (res.path("status").asInt() == 200) {
			mongoTemplate.remove(entity);
			return response(200, mapper.convertValue(entity, JsonNode.class), 0);
		}

		return response(res.path("status").asInt(), res.path("data"), 0);
	}

	public Account getInfoAccount(String username) {
		return mongoTemplate.findOne(Query.query(Criteria.where("username").is(username)), Account.class);
	}

}