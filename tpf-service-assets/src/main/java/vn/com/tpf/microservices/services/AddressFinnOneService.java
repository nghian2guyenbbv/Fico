package vn.com.tpf.microservices.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.AddressFinnone;

@Service
public class AddressFinnOneService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}

	public JsonNode getAddress(JsonNode request) {
		Query query = new Query();

		if (request.path("param").path("areaCode").isTextual()) {
			query.addCriteria(Criteria.where("areaCode").is(request.path("param").path("areaCode").asText()));
			AddressFinnone address = mongoTemplate.findOne(query, AddressFinnone.class);
			if (address == null) {
				return response(404, mapper.createObjectNode().put("message", "AreaCode Not Found"));
			}
			return response(200, mapper.convertValue(address, JsonNode.class));
		} else if (request.path("param").path("postCode").isTextual()) {
			query.addCriteria(Criteria.where("postCode").is(request.path("param").path("postCode").asText()));
			List<AddressFinnone> address = mongoTemplate.find(query.limit(1), AddressFinnone.class);
			if (address.isEmpty()) {
				return response(404, mapper.createObjectNode().put("message", "PostCode Not Found"));
			}
			return response(200, mapper.convertValue(address.get(0), JsonNode.class));
		}

		return response(200, null);
	}

}