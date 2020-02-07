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

import java.util.ArrayList;
import java.util.List;

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

    private String setFilterBy(JsonNode request){

        if (request.path("param").path("partnerId").isTextual()) {
            return "partnerId";
        } else if(request.path("param").path("partnerName").isTextual()){
            return "partnerName";
        } else if(request.path("param").path("project").isTextual()){
            return "project";
        }
        return null;
    }

    private List<Partner> getPartner(String filterBy, String filterValue, String status){

        List<Partner> result;

        if(filterBy.isEmpty() || filterBy.isBlank()){
            if(status.isEmpty()){
                result = mongoTemplate.findAll(Partner.class);
            } else {
                Query query = new Query();
                query.addCriteria(Criteria.where("status").is(status));
                result = mongoTemplate.find(query, Partner.class);
            }
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where(filterBy).is(filterValue));
            result = mongoTemplate.find(query, Partner.class);
        }
        return result;
    }

    public JsonNode getAllPartners(JsonNode request) {

        List<Partner> lstPartner;

        if(request.path("params").path("status").isTextual() && !request.path("params").path("status").isNull()){
            String status = request.path("params").path("status").asText();
            lstPartner = this.getPartner(null, null, status);
        } else {
            lstPartner = this.getPartner(null, null, null);
        }

        if(lstPartner.isEmpty()){
            return response(200, NOT_FOUND);
        }
        return response(200, mapper.convertValue(lstPartner, JsonNode.class));
    }

    public JsonNode getPartnerByFilter(JsonNode request) {

        String filterBy = this.setFilterBy(request);

        if(request.path("param").path(filterBy).isNull()){
            return response(200, "filterBy " + NOT_FOUND);
        }
        String filterValue = request.path("param").path(filterBy).asText();

        List<Partner> lstPartner = this.getPartner(filterBy, filterValue, null);

        if(lstPartner.isEmpty()){
            return response(200, NOT_FOUND);
        }
        return response(200, mapper.convertValue(lstPartner, JsonNode.class));
    }

    public JsonNode updateOnePartnerStatus(JsonNode request) {

        String filterBy = this.setFilterBy(request);

        String filterValue = request.path("param").path(filterBy).asText();

        List<Partner> lstPartner = this.getPartner(filterBy, filterValue, null);

        if(lstPartner.isEmpty()){
            return response(200, NOT_FOUND);
        }
        lstPartner.get(0).setStatus(request.path("param").path("status").asText());
        mongoTemplate.save(lstPartner.get(0));
        return response(200, NOT_FOUND);
    }
}
