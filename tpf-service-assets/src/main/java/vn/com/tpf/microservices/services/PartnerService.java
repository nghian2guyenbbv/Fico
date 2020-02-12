package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.models.Partner;

import java.util.List;
import java.util.Map;

@Service
public class PartnerService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String NOT_FOUND = "NOT FOUND";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    private JsonNode response(int status, Object object) {
        ObjectNode response = mapper.createObjectNode();
        JsonNode data = mapper.convertValue(object, JsonNode.class);

        response.put("status", status).set("data", data);
        return response;
    }

    private List<Partner> getPartner(Map<String, Object> params){
        Query dynamicQuery = new Query();
        if(params.isEmpty()){
            return mongoTemplate.findAll(Partner.class);
        } else {
            Criteria cri = new Criteria();
            params.forEach((k, v) -> {
                if (v instanceof String)
                    v = ((String) v).toUpperCase();
                cri.and(k).is(v);
            });
            dynamicQuery.addCriteria(cri);
            return mongoTemplate.find(dynamicQuery, Partner.class);
        }
    }

    public JsonNode getPartner(JsonNode request) {
        if(request.path("body").isNull())
            return response(200, "body null");

        Map params = mapper.convertValue(request.path("body"), Map.class);

        List<Partner> result = this.getPartner(params);
        if(result == null || result.isEmpty()){
            return response(200, NOT_FOUND);
        }
        return response(200, mapper.convertValue(result, JsonNode.class));
    }

    public JsonNode updateStatus1Partner(JsonNode request) {
        try {
            Map params = mapper.convertValue(request.path("body"), Map.class);
            List<Partner> result = this.getPartner(params);

            if(result == null || result.isEmpty()){
                return response(200, NOT_FOUND);
            }
            result.get(0).setStatus(request.path("body").path("status").asText());
            mongoTemplate.save(result.get(0));
            return response(200, mapper.convertValue(result, JsonNode.class));
        } catch (Exception e) {
            log.error("Error on " + "PartnerService.updateStatus1Partner(JsonNode request)");
            return response(200, NOT_FOUND);
        }
    }
}
