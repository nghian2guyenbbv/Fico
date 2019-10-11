package vn.com.tpf.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Address;

@Service
public class AddressService {

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
		query.addCriteria(Criteria.where("areaCode").is(request.path("param").path("areaCode").asText()));
		Address address = mongoTemplate.findOne(query, Address.class);

		if (address == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"));
		}

		return response(200, mapper.convertValue(address, JsonNode.class));
	}

}