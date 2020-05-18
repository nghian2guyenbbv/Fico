package vn.com.tpf.microservices.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import vn.com.tpf.microservices.models.MobilityFinnOneFiled;

@Service
public class FieldsFinnOneService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired

	private MongoTemplate mobilityFinnOneFiledTemplate;
	

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}
	
	public JsonNode getListFinnOneFileds(JsonNode request) {
		Query query = new Query();
//		query.addCriteria(Criteria.where(String.format("phoneConfirmed.%s", request.path("body").asText())).is("Yes"));
		MobilityFinnOneFiled fileds = mobilityFinnOneFiledTemplate.findOne(query, MobilityFinnOneFiled.class);

			if (fileds != null) 
				return response(200, mapper.convertValue(fileds, JsonNode.class));
			return response(404, mapper.createObjectNode().put("message", "data not found"));
	}
}