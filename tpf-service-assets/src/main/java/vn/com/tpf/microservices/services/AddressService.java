package vn.com.tpf.microservices.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import vn.com.tpf.microservices.models.Address;

@Service
public class AddressService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public Map<String, Object> getAddress(JsonNode request) {
		Query query = new Query();
		query.addCriteria(Criteria.where("areaCode").is(request.path("param").path("areaCode").asText()));
		Address address = mongoTemplate.findOne(query, Address.class);

		if (address == null) {
			return Map.of("status", 404, "data", Map.of("message", "Not Found"));
		}

		return Map.of("status", 200, "data", address);
	}

}