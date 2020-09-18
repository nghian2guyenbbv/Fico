package vn.com.tpf.microservices.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.DocumentFinnOne;

@Service
public class DocumentFinnOneService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;
	
//	@PostConstruct
//	public void init() {
//		System.out.println("TPF_Application cum Credit Contract (ACCA)".toLowerCase().replace(" ", "_"));
//	}

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}

	public JsonNode getListDocuments(JsonNode request) {
		Query query = new Query();

		if (request.path("body").path("productCode").isTextual() && request.path("body").path("schemeCode").isTextual()) {
			query.addCriteria(Criteria.where("productCode").is(request.path("body").path("productCode").asText()).and("schemeCode").is(request.path("body").path("schemeCode").asText()));
			DocumentFinnOne document = mongoTemplate.findOne(query, DocumentFinnOne.class);
			if (document == null) 
				return response(404, mapper.createObjectNode().put("message", "ProductCode and SchemeCode Not Found"));
			return response(200, mapper.convertValue(document, JsonNode.class));
		} 
		return response(404, mapper.createObjectNode().put("message", "ProductCode and SchemeCode Not Found"));
		
	}

	public JsonNode getProductCode(JsonNode request) {

		if (request.path("body").path("schemeCode").isTextual()) {
			Query query = Query.query(Criteria.where("schemeCode").is(request.path("body").path("schemeCode").asText()));
			DocumentFinnOne document = mongoTemplate.findOne(query, DocumentFinnOne.class);
			if (document == null)
				return response(404, mapper.createObjectNode().put("message", "SchemeCode Not Found"));
			return response(200, mapper.convertValue(document, JsonNode.class));
		}
		return response(404, mapper.createObjectNode().put("message", "SchemeCode Not Found"));

	}
}
