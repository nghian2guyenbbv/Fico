package vn.com.tpf.microservices.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.models.Template;

@Service
public class TemplateService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	public Map<String, Object> getListTemplate(JsonNode request) {
		int page = request.path("param").path("page").asInt(1);
		int limit = request.path("param").path("limit").asInt(10);
		String[] sort = request.path("param").path("sort").asText("createdAt,desc").split(",");

		Query query = new Query();
		long total = mongoTemplate.count(query, Template.class);

		query.with(PageRequest.of(page - 1, limit, Sort.by(Direction.fromString(sort[1]), sort[0])));
		List<Template> list = mongoTemplate.find(query, Template.class);

		return Map.of("status", 200, "data", list, "total", total);
	}

	public Map<String, Object> createTemplate(JsonNode request) throws Exception {
		Assert.notNull(request.get("body"), "no body");

		Template body = mapper.treeToValue(request.get("body"), Template.class);

		Assert.hasText(body.getName(), "name is required");

		Template entity = new Template();
		entity.setName(body.getName());

		mongoTemplate.save(entity);
		return Map.of("status", 201, "data", entity);
	}

	public Map<String, Object> updateTemplate(JsonNode request) throws Exception {
		String id = request.path("param").path("id").asText();
		Assert.hasText(id, "param id is required");
		Assert.notNull(request.get("body"), "no body");

		Template body = mapper.treeToValue(request.get("body"), Template.class);

		Assert.hasText(body.getName(), "name is required");

		Template entity = mongoTemplate.findById(id, Template.class);

		if (entity == null) {
			return Map.of("status", 404, "data", Map.of("message", "Not Found"));
		}

		entity.setName(body.getName());

		mongoTemplate.save(entity);
		return Map.of("status", 200, "data", entity);
	}

	public Map<String, Object> deleteTemplate(JsonNode request) throws Exception {
		String id = request.path("param").path("id").asText();
		Assert.hasText(id, "param id is required");

		Template entity = mongoTemplate.findById(id, Template.class);

		if (entity == null) {
			return Map.of("status", 404, "data", Map.of("message", "Not  Found"));
		}

		mongoTemplate.remove(entity);
		return Map.of("status", 200, "data", entity);
	}

}