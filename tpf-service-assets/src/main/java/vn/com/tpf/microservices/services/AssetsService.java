package vn.com.tpf.microservices.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.models.Assets;

@Service
public class AssetsService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;

//	  public Map<String, Object> getListAccount(JsonNode request, JsonNode token) {
//	    int page = request.path("param").path("page").asInt(1);
//	    int limit = request.path("param").path("limit").asInt(10);
//	    String[] sort = request.path("param").path("sort").asText("createdAt,desc").split(",");
//
//	    Query query = new Query();
//	    Criteria criteria = Criteria.where("username").ne(token.path("user_name").asText());
//
//	    Account account = getInfoAccount(token.path("user_name").asText());
//	    if (account != null) {
//	      criteria.and("departments").in(account.getDepartments());
//	    }
//
//	    if (!request.path("param").path("username").isNull()) {
//	      criteria.andOperator(Criteria.where("username").regex(request.path("param").path("username").asText()));
//	    }
//
//	    query.addCriteria(criteria);
//
//	    long total = mongoTemplate.count(query, Account.class);
//
//	    query.with(PageRequest.of(page - 1, limit, Sort.by(Direction.fromString(sort[1]), sort[0])));
//	    List<Account> list = mongoTemplate.find(query, Account.class);
//
//	    return Map.of("status", 200, "data", list, "total", total);
//	  }

	public Map<String, Object> createAssets(JsonNode request) throws Exception {
		Assert.notNull(request.get("body"), "no body");

		Assets body = mapper.treeToValue(request.get("body"), Assets.class);

		Assert.hasText(body.getKey(), "key is required");
		Assert.notEmpty(body.getAssets(), "assets is required");

		Assets entity = Assets.builder().key(body.getKey()).assets(body.getAssets()).build();
		mongoTemplate.save(entity);
		return Map.of("status", 201, "data", entity);

	}

}