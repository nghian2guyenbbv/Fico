package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import vn.com.tpf.microservices.models.Application;
import vn.com.tpf.microservices.models.Product;
import vn.com.tpf.microservices.models.RaiseQuery.RaiseQueryModel;
import vn.com.tpf.microservices.models.RequestModel;
import vn.com.tpf.microservices.models.ResponseModel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RaiseQueryService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RabbitMQService rabbitMQService;


    public Map<String, Object> addRaiseQuery(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        String requestId = request.path("body").path("request_id").textValue();
        String referenceId = UUID.randomUUID().toString();
        try{
            Assert.notNull(request.get("body"), "no body");
            RaiseQueryModel requestModel = mapper.treeToValue(request.get("body"), RaiseQueryModel.class);

            mongoTemplate.insert(requestModel);

            responseModel.setRequest_id(requestId);
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code("0");

        }
        catch (Exception e) {
            log.info("ReferenceId : "+ referenceId + "Error: " + e);
            responseModel.setRequest_id(requestId);
            responseModel.setReference_id(referenceId);
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code("1");
            responseModel.setMessage(e.getMessage());
        }
        return Map.of("status", 200, "data", responseModel);
    }

    public Map<String, Object> responseQuery(Application application){
        ResponseModel responseModel = new ResponseModel();


        return Map.of("status", 200, "data", responseModel);
    }
}
