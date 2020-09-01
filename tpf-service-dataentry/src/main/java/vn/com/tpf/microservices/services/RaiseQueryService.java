package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.models.*;
import vn.com.tpf.microservices.models.RaiseQuery.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private DataEntryService dataEntryService;

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

            //call function add comment cua Trung here, de gia lap hien thi tren porta IH
            new Thread(() -> {
                String err = callCommentApp();
                if (StringUtils.hasLength(err)){
                    log.info("callComment ---------------------> {}", err);
                }
            }).start();
            //end call

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
            query.addCriteria(Criteria.where("applicationNo").is(applicationId).and("queryStatus").is("Raised"));
            List<RaiseQueryModel> raiseQueryModelList = mongoTemplate.find(query, RaiseQueryModel.class);
            if (raiseQueryModelList.size() <= 0){
                throw new Exception("raiseQueryModel not exist");
            }

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
                    .responseBy(raiseQueryModel.getRaiseTo())
                    .response(application.getComment().get(0).getResponse().getComment())
                    .queryId(raiseQueryModel.getQueryId()).build();

            List<QmrServiceRequestParamsVoList> lists = new ArrayList<QmrServiceRequestParamsVoList>();
            lists.add(qmrServiceRequestParamsVoList);

            ResponseQueryModel responseQueryModel = ResponseQueryModel.builder()
                    .productProcessor("EXTERNAL")
                    .qmrServiceRequestParamsVoList(lists).build();

            JsonNode response = apiService.callApiF1(urlResponseQuery, mapper.convertValue(responseQueryModel, JsonNode.class));

            slog+=", response: " + response.asText();

            String status = response.path("responseData").path("status").asText("");
            if (response.hasNonNull("errMsg") || (StringUtils.hasLength(status) && status.trim().equals("Failure"))){
                throw new Exception("Fail");
            }

            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code("0");
            responseModel.setData(response);
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

    private String callCommentApp() {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("queryStatus").is("Raised"));
            query.with(Sort.by(Sort.Direction.DESC, "createdDate"));
            List<RaiseQueryModel> raiseQueryModel = mongoTemplate.find(query, RaiseQueryModel.class);

            Set<String> listAppId = raiseQueryModel.stream().map(RaiseQueryModel::getApplicationNo).collect(Collectors.toSet());
            log.info("listAppId -----------------> {}", listAppId.toString());

            Criteria criteria = Criteria.where("applicationId").in(listAppId);
            criteria = criteria.and("createFrom").in(null, "WEB", "web");
            criteria = criteria.and("partnerId").is("3");
            criteria = criteria.andOperator(new Criteria().orOperator(Criteria.where("comment").is(null), Criteria.where("comment.response").ne(null)));
            query = new Query();
            query.addCriteria(criteria);
            List<Application> applications = mongoTemplate.find(query, Application.class);
            if (applications.size() <= 0){
                throw new Exception("app not exist");
            }
            log.info("applications -----------------> {}", applications.toString());

            applications.stream().map(application -> {
                raiseQueryModel
                        .stream()
                        .filter(raiseQueryModel1 -> raiseQueryModel1.getApplicationNo().equals(application.getApplicationId()))
                        .findAny().ifPresent(raiseQueryModel1 -> {
                    CommentModel commentModel = new CommentModel();
                    commentModel.setCommentId("ih-" + UUID.randomUUID().toString().substring(0, 10));
                    commentModel.setStage("");
                    commentModel.setCode("IN-HOUSE");
                    commentModel.setType("IN-HOUSE");
                    commentModel.setRequest(raiseQueryModel1.getComment());
                    commentModel.setCreatedDate(raiseQueryModel1.getCreatedDate());

                    Application application1 = new Application();
                    application1.setApplicationId(application.getApplicationId());
                    application1.setComment(Arrays.asList(commentModel));

                    RequestModel requestModel = new RequestModel();
                    requestModel.setDate_time(new Timestamp(new Date().getTime()));
                    requestModel.setRequest_id(UUID.randomUUID().toString());
                    requestModel.setData(application1);
                    JsonNode request = mapper.convertValue(Map.of("body", requestModel), JsonNode.class);
                    JsonNode token = mapper.createObjectNode();
                    log.info("request -----------------> {}", request.toString());
                    dataEntryService.commentApp(request, token);
                });
                return application;
            }).collect(Collectors.toList());
            return "";
        }catch (Exception e){
            log.info("exception ----------------> {}", e.toString());
            return e.getMessage();
        }
    }
}
