package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.models.*;
import vn.com.tpf.microservices.models.RaiseQuery.*;

import java.sql.Timestamp;
import java.util.*;

@Service
public class RaiseQueryService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Value("${spring.url.uploadfile}")
    private String urlUploadfile;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApiService apiService;

    @Value("${spring.url.esb.responsequery}")
    private String urlResponseQuery;

    public Map<String, Object> addRaiseQuery(JsonNode request) {
        String slog="addRaiseQuery:" + request.get("body").asText();

        ResponseModel responseModel = new ResponseModel();
        String referenceId = UUID.randomUUID().toString();
        try{
            Assert.notNull(request.get("body"), "no body");
            RaiseQueryModel requestModel = mapper.treeToValue(request.get("body"), RaiseQueryModel.class);

            mongoTemplate.insert(requestModel);

            responseModel.setRequest_id(referenceId);
            responseModel.setReference_id(referenceId);
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code("0");
        }
        catch (Exception e) {
            slog+="; ERROR: " + e.toString();
            responseModel.setRequest_id(referenceId);
            responseModel.setReference_id(referenceId);
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code("1");
            responseModel.setMessage(e.getMessage());
        }
        finally {
            log.info("{}",slog);
        }

        return Map.of("status", 200, "data", responseModel);
    }

    public Map<String, Object> responseQuery(Application application) throws JsonProcessingException {
        String slog="responseQuery:" + mapper.writeValueAsString(application);
        ResponseModel responseModel = new ResponseModel();
        String referenceId=UUID.randomUUID().toString();
        try {
            String applicationId = application.getApplicationId();

            //check xem có raisequery ko , get info
            Query query = new Query();
            query.addCriteria(Criteria.where("applicationNo").is(applicationId));
            List<RaiseQueryModel> raiseQueryModelList = mongoTemplate.find(query, RaiseQueryModel.class);

            //get raise query moi nhat
            RaiseQueryModel raiseQueryModel = raiseQueryModelList.stream().max(Comparator.comparing(RaiseQueryModel::getCreatedDate)).get();

            //parse json call api
            String comment = application.getComment().get(0).getResponse().getComment();

            //check co document hay ko
            QueryModuleDocumentDetailsVO queryModuleDocumentDetailsVO = null;
            if (application.getComment().get(0).getResponse().getDocuments() != null && application.getComment().get(0).getResponse().getDocuments().size() > 0) {
                Document document = application.getComment().get(0).getResponse().getDocuments().get(0);
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + document.getFilename(), HttpMethod.GET, entity, byte[].class);

                queryModuleDocumentDetailsVO = QueryModuleDocumentDetailsVO.builder()
                        .document(response.getBody())
                        .documentName(document.getOriginalname()).build();
            }


            QmrServiceRequestParamsVoList qmrServiceRequestParamsVoList = QmrServiceRequestParamsVoList.builder()
                    .queryModuleDocumentDetailsVO(queryModuleDocumentDetailsVO)
                    .applicationNumber(applicationId)
                    .queryCode(raiseQueryModel.getQueryCode())
                    .responseBy(raiseQueryModel.getRaiseBy())
                    .response(application.getComment().get(0).getResponse().getComment())
                    .queryId(raiseQueryModel.getQueryId()).build();

            List<QmrServiceRequestParamsVoList> lists = new ArrayList<QmrServiceRequestParamsVoList>();
            lists.add(qmrServiceRequestParamsVoList);


            JsonNode response = apiService.callApiF1(urlResponseQuery, mapper.convertValue(lists, JsonNode.class));

            slog+=", response: " + response.asText();

            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code("0");

        } catch (Exception e) {

            slog +=", Exception: " + e.toString();

            responseModel.setRequest_id(referenceId);
            responseModel.setReference_id(referenceId);
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code("1");
            responseModel.setMessage(e.getMessage());
        }
        finally {
            log.info("{}",slog);
        }
        return Map.of("status", 200, "data", responseModel);
    }
}
