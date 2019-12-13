package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.models.*;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataEntryService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.url.digitex-cminfoapi}")
	private String urlDigitexCmInfoApi;

	@Value("${spring.url.digitex-resubmitcommentapi}")
	private String urlDigitexResubmitCommentApi;

	@Value("${spring.url.digitex-feedbackapi}")
	private String urlDigitexFeedbackApi;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private ApiService apiService;

	@Autowired
	private ConvertService convertService;

	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Arrays.asList(new HttpLogService()));
	}

	public Map<String, Object> addProduct(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);

			List<Product> inputData = requestModel.getProduct();

			List<Product> checkExist = mongoTemplate.findAll(Product.class);
			if (checkExist.size() > 0){
				Query query = new Query();
				Update update = new Update();
				update.set("product", requestModel.getProduct());
				Product addProduct = mongoTemplate.findAndModify(query, update, Product.class);

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}else{
				mongoTemplate.insert(inputData, Product.class);

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}
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

	public Map<String, Object> addProductV2(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);

			List<ProductV2> inputData = requestModel.getProductv2();

			List<ProductV2> checkExist = mongoTemplate.findAll(ProductV2.class);
			if (checkExist.size() > 0){
				Query query = new Query();
				Update update = new Update();
				update.set("product", requestModel.getProductv2());
				ProductV2 addProduct = mongoTemplate.findAndModify(query, update, ProductV2.class);

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}else{
				mongoTemplate.insert(inputData, ProductV2.class);

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}
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

	public Map<String, Object> getProductByName(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		UUID quickLeadId = UUID.randomUUID();
		try{
//			Application app = new Application();
//			quickLeadId = UUID.randomUUID();
//			app.setQuickLeadId(quickLeadId.toString());
//			mongoTemplate.save(app);
//			responseModel.setRequest_id(requestId);
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code("0");
//			responseModel.setData(quickLeadId);
//            return Map.of("status", 200, "data", responseModel);


//			responseModel.setData(quickLeadId);
//
//
			Assert.notNull(request.get("body"), "no body");
			RequestProductModel requestModel = mapper.treeToValue(request.get("body"), RequestProductModel.class);

			List<Product> data = new ArrayList<>();
			if (requestModel.getData().getSearch_value() == null || requestModel.getData().getSearch_value().equals("")){
				data = mongoTemplate.findAll(Product.class);
			}else{
				Query query = new Query();
				query.addCriteria(Criteria.where("productName").is(requestModel.getData().getSearch_value()));
				data = mongoTemplate.find(query, Product.class);
			}
			if (data.size() > 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(data);
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("productName not exist.");
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());


			responseModel.setData(quickLeadId);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getProductByNameV2(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		UUID quickLeadId = UUID.randomUUID();
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestProductModel requestModel = mapper.treeToValue(request.get("body"), RequestProductModel.class);

			List<ProductV2> data = new ArrayList<>();
			if (requestModel.getData().getSearch_value() == null || requestModel.getData().getSearch_value().equals("")){
				data = mongoTemplate.findAll(ProductV2.class);
			}else{
				Query query = new Query();
				query.addCriteria(Criteria.where("productName").is(requestModel.getData().getSearch_value()));
				data = mongoTemplate.find(query, ProductV2.class);
			}
			if (data.size() > 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(data);
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("productName not exist.");
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());


			responseModel.setData(quickLeadId);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getAll(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			List<Application> checkExist = mongoTemplate.findAll(Application.class);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("0");
			responseModel.setData(checkExist);
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

	public Map<String, Object> getByAppId(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);

			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(requestModel.getData().getApplicationId()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(checkExist);
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId not exist.");
			}
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

	public Map<String, Object> getAddress(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			TypedAggregation<AddressFinOne> studentAggregation = Aggregation.newAggregation(AddressFinOne.class,
					Aggregation.group("cityName").
							push(new BasicDBObject
									("_id", "$_id").append
									("areaName", "$areaName").append
									("areaCode", "$areaCode")).as("data"));

			AggregationResults<AddressFinOne> results = mongoTemplate.
					aggregate(studentAggregation, AddressFinOne.class);

			List<AddressFinOne> resultData = results.getMappedResults();
			for (AddressFinOne item :
					resultData) {
				item.setCityName(item.get_id());
			}

			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("0");
			responseModel.setData(resultData);
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

	public Map<String, Object> getBranch(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			List<Branch> resultData = mongoTemplate.findAll(Branch.class);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("0");
			responseModel.setData(resultData);
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

	public Map<String, Object> getBranchByUser(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			if (token.get("branches") == null){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(referenceId);
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("token not branches");
				return Map.of("status", 200, "data", responseModel);
			}
			Query query = new Query();
			List<String> branchUser = new ArrayList<String>();
			branchUser = mapper.readValue(token.get("branches").toString(), List.class);
			query.addCriteria(Criteria.where("branchName").in(branchUser));

			List<Branch> resultData = mongoTemplate.find(query, Branch.class);

			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("0");
			responseModel.setData(resultData);
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("2");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getDetail(JsonNode request, JsonNode token) {
		Application app = new Application();
		try{
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(request.path("param").get("id").asText()));
			app = mongoTemplate.findOne(query, Application.class);
			if (app == null) {
				return Map.of("status", 200, "data", Map.of("message", "Not Exist"));
			}
		}
		catch (Exception e) {
			log.info("Error: " + e);
			return Map.of("status", 200, "data", Map.of("message", "Not Found"));
		}
		return Map.of("status", 200, "data", app);
	}

	public Map<String, Object> firstCheck(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Assert.notNull(request.path("body").path("request_id"), "no body");
			if(request.path("body").path("data").path("customerId").textValue().length() != 9) {
				if (request.path("body").path("data").path("customerId").textValue().length() != 12) {
					responseModel.setRequest_id(requestId);
					responseModel.setReference_id(referenceId);
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("2");
					responseModel.setMessage("Sai CMND!");
					return Map.of("status", 200, "data", responseModel);
				}
			}

			if (!isNumer(request.path("body").path("data").path("customerId").textValue())) {
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(referenceId);
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("2");
				responseModel.setMessage("Sai CMND!");
				return Map.of("status", 200, "data", responseModel);
			}

			if(request.path("body").path("data").path("bankCardNumber").textValue().length() != 16) {
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(referenceId);
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("2");
				responseModel.setMessage("Sai BankCardNumber!");
				return Map.of("status", 200, "data", responseModel);
			}

			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("0");
			responseModel.setData(Map.of("first_check_result", "pass"));

//			String resultFirstCheck = apiService.firstCheck(request);
//			if (resultFirstCheck.equals("pass")){
//				responseModel.setRequest_id(requestId);
//				responseModel.setReference_id(UUID.randomUUID().toString());
//				responseModel.setDate_time(new Timestamp(new Date().getTime()));
//				responseModel.setResult_code("0");
//			}else{
//				responseModel.setRequest_id(requestId);
//				responseModel.setReference_id(UUID.randomUUID().toString());
//				responseModel.setDate_time(new Timestamp(new Date().getTime()));
//				responseModel.setResult_code("1");
//				responseModel.setMessage(resultFirstCheck);
//			}
//
//			responseModel.setRequest_id(requestId);
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code("0");
//			responseModel.setData(encStream3.toString());

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

	public Map<String, Object> sendApp(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			requestId = requestModel.getRequest_id();
			Application data = requestModel.getData();
			data.setCreatedDate(new Timestamp(new Date().getTime()));

			if (data.getApplicationId() == null || data.getApplicationId().equals("")){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId is not null.");
			}else{
				Query query = new Query();
				query.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
				Application checkExist = mongoTemplate.findOne(query, Application.class);

				if (checkExist == null){
					responseModel.setRequest_id(requestId);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("1");
					responseModel.setMessage("applicationId not found.");
				}else if (checkExist != null && checkExist.getApplicationInformation() == null){
					//Create ValidatorFactory which returns validator
					ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
					//It validates bean instances
					Validator validator = factory.getValidator();
					//Validate bean
					Set<ConstraintViolation<Application>> constraintViolations = validator.validate(data);

					if (constraintViolations.size() <= 0 || constraintViolations.isEmpty()){
						Update update = new Update();
						update.set("applicationInformation", data.getApplicationInformation());
						update.set("loanDetails", data.getLoanDetails());
						update.set("references", data.getReferences());
						update.set("dynamicForm", data.getDynamicForm());
						Application resultUpdate = mongoTemplate.findAndModify(query, update, Application.class);

//						--automation fullapp--
						Application dataFullApp = mongoTemplate.findOne(query, Application.class);
						if (dataFullApp.getQuickLead().getDocumentsComment() != null){
							dataFullApp.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
						}
						rabbitMQService.send("tpf-service-automation",
								Map.of("func", "fullInfoApp","body", dataFullApp));

						rabbitMQService.send("tpf-service-app",
								Map.of("func", "updateApp","reference_id", referenceId,
										"param", Map.of("project", "dataentry", "id", dataFullApp.getId()), "body", convertService.toAppDisplay(dataFullApp)));

                        Report report = new Report();
                        report.setQuickLeadId(requestId);
                        report.setApplicationId(data.getApplicationId());
                        report.setFunction("SENDAPP");
                        report.setStatus("PROCESSING");
                        report.setCreatedBy(token.path("user_name").textValue());
                        report.setCreatedDate(new Date());
                        mongoTemplate.save(report);

						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("0");
					}else{
						HashMap<String,String> map = new HashMap<String,String>();
						for (ConstraintViolation<Application> violation : constraintViolations) {
							map.put(violation.getPropertyPath().toString(), violation.getMessage());
						}
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage(map.toString());
					}
				}else {
					responseModel.setRequest_id(requestId);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("1");
					responseModel.setMessage("applicationId not send again.");
				}
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.toString());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> updateApp(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			requestId = requestModel.getRequest_id();
			Application data = requestModel.getData();
			data.setLastModifiedDate(new Timestamp(new Date().getTime()));

			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);

			if (checkExist.size() <= 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId not exists.");
			}else{
				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));

				Update update = new Update();
				update.set("applicationInformation", data.getApplicationInformation());
				update.set("loanDetails", data.getLoanDetails());
				update.set("references", data.getReferences());
				update.set("dynamicForm", data.getDynamicForm());
//				update.set("comment", data.getDynamicForm());
//				update.set("applicationInformation.testdata", data.getApplicationInformation());
//				update.set("applicationInformation.personalInformation.personalInfo.$", data.getApplicationInformation().getPersonalInformation().getPersonalInfo());

				Application resultUpdate = mongoTemplate.findAndModify(query, update, Application.class);

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}
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

	public Map<String, Object> commentApp(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		boolean requestCommnentFromDigiTex = false;
        boolean responseCommnentToDigiTex = false;
		String applicationId = "";
		String commentId = "";
		String comment = "";
		String stageAuto = "";
		List<Document> documentCommnet = new ArrayList<Document>();
        boolean responseCommnentFullAPPFromDigiTex = false;
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			requestId = requestModel.getRequest_id();
			Application data = requestModel.getData();
			data.setLastModifiedDate(new Timestamp(new Date().getTime()));
			applicationId = data.getApplicationId();

			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);

			if (checkExist.size() <= 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId not exists.");
			}else{
				for (CommentModel item : data.getComment()) {
//					documentCommnet = item.getResponse().getDocuments();
					commentId = item.getCommentId();
					Query queryUpdate = new Query();
					queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()).and("comment.commentId").is(item.getCommentId()));
					List<Application> checkCommentExist = mongoTemplate.find(queryUpdate, Application.class);
//					String typeComment = checkCommentExist.get(0).getComment().get(0).getType();

					if (checkCommentExist.size() <= 0){
						if (item.getType().equals("DIGI-TEX")) {// digitex gui comment
//							if (typeComment.equals("DIGI-TEX")) {// digitex gui comment
							Query queryAddComment = new Query();
							queryAddComment.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));

							Update update = new Update();
							item.setCreatedDate(new Date());
							update.push("comment", item);
							Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, update, Application.class);

                            requestCommnentFromDigiTex = true;
						}else{
							//xu ly tai fail automationfull - fico gui comment
						}
					}else {
//						if (item.getType().equals("FICO")) {// digitex tra comment
						if (checkCommentExist.get(0).getComment().get(0).getType().equals("FICO")) {// digitex tra comment(do digites k gui lai type nen k dung item.getType())
							List<CommentModel> listComment = checkCommentExist.get(0).getComment();
							for (CommentModel itemComment : listComment) {
								if (itemComment.getCommentId().equals(item.getCommentId())) {
									if (itemComment.getResponse() == null) {
										stageAuto = itemComment.getStage();
										Update update = new Update();
										update.set("comment.$.response", item.getResponse());
										Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
									}
								}
							}
                            responseCommnentFullAPPFromDigiTex = true;

							// update automation
							if (item.getResponse().getData() != null){
								Application dataUpdate = item.getResponse().getData();
								dataUpdate.setStage(stageAuto);
								rabbitMQService.send("tpf-service-automation",
										Map.of("func", "updateAppError","body", dataUpdate));
							}

						}else{//fico tra comment
							documentCommnet = item.getResponse().getDocuments();
							List<CommentModel> listComment = checkCommentExist.get(0).getComment();
							for (CommentModel itemComment : listComment) {
								if (itemComment.getCommentId().equals(item.getCommentId())) {
									if (itemComment.getResponse() == null) {
										if (item.getResponse().getDocuments().size() > 0){
											for (Document itemCommentFico : item.getResponse().getDocuments()) {
												Link link = new Link();
												link.setUrlFico(itemCommentFico.getFilename());
												link.setUrlPartner(itemCommentFico.getUrlid());
												itemCommentFico.setLink(link);
											}
										}

										Update update = new Update();
										update.set("comment.$.response", item.getResponse());
										Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

										comment = item.getResponse().getComment();
									}
								}
							}

                            responseCommnentToDigiTex = true;

						}
					}
				}

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}
			if (requestCommnentFromDigiTex){
				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
				Update update = new Update();
				update.set("status", "RETURNED");
				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

				Application dataFullApp = mongoTemplate.findOne(query, Application.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp","reference_id", referenceId,
								"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

                Report report = new Report();
                report.setQuickLeadId(requestId);
                report.setApplicationId(data.getApplicationId());
                report.setFunction("DIGITEXX_COMMENT");
                report.setStatus("RETURNED");
                report.setCreatedBy(token.path("user_name").textValue());
                report.setCreatedDate(new Date());
                mongoTemplate.save(report);
            }

            if (responseCommnentToDigiTex){
				ArrayNode documents = mapper.createArrayNode();
				boolean checkIdCard = false;
				boolean checkHousehold = false;
				for (Document item: documentCommnet) {
					ObjectNode doc = mapper.createObjectNode();

					if (item.getType().equals("TPF_ID Card")){
						if (!checkIdCard) {
							doc.put("documentComment", item.getComment());
							if (item.getLink() != null) {
								doc.put("documentId", item.getLink().getUrlPartner());
							}
							documents.add(doc);

							checkIdCard = true;
						}
					}else if (item.getType().equals("TPF_Notarization of ID card")){
						if (!checkIdCard) {
							doc.put("documentComment", item.getComment());
							if (item.getLink() != null) {
								doc.put("documentId", item.getLink().getUrlPartner());
							}
							documents.add(doc);

							checkIdCard = true;
						}
					}if (item.getType().equals("TPF_Family Book")){
						if (!checkHousehold) {
							doc.put("documentComment", item.getComment());
							if (item.getLink() != null) {
								doc.put("documentId", item.getLink().getUrlPartner());
							}
							documents.add(doc);

							checkHousehold = true;
						}
					}else if (item.getType().equals("TPF_Notarization of Family Book")){
						if (!checkHousehold) {
							doc.put("documentComment", item.getComment());
							if (item.getLink() != null) {
								doc.put("documentId", item.getLink().getUrlPartner());
							}
							documents.add(doc);

							checkHousehold = true;
						}
					} else if (item.getType().equals("TPF_Customer Photograph")){
						doc.put("documentComment", item.getComment());
						if (item.getLink() != null) {
							doc.put("documentId", item.getLink().getUrlPartner());
						}
						documents.add(doc);
					}else if (item.getType().equals("TPF_Application cum Credit Contract (ACCA)")){
						doc.put("documentComment", item.getComment());
						if (item.getLink() != null) {
							doc.put("documentId", item.getLink().getUrlPartner());
						}
						documents.add(doc);
					}
				}

				JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "comment-id", commentId,
						"comment", comment, "documents", documents)), JsonNode.class);
				apiService.callApiDigitexx(urlDigitexResubmitCommentApi,dataSend);

//				String resultDG = apiService.callApiDigitexx(urlDigitexResubmitCommentApi,dataSend);
//				if (resultDG != null){
//					responseModel.setRequest_id(requestId);
//					responseModel.setReference_id(referenceId);
//					responseModel.setDate_time(new Timestamp(new Date().getTime()));
//					responseModel.setResult_code("1");
//					responseModel.setMessage(resultDG);
//
//					return Map.of("status", 200, "data", responseModel);
//				}

//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//				headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//				HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(Map.of("application-id", applicationId, "comment-id", commentId,
//						"comment", comment, "documents", documents)), headers);
//				ResponseEntity<?> res = restTemplate.postForEntity(urlDigitexResubmitCommentApi, entity, Object.class);
//				JsonNode body = mapper.valueToTree(res.getBody());

				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
				Update update = new Update();
				update.set("status", "PROCESSING");
				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

				Application dataFullApp = mongoTemplate.findOne(query, Application.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp","reference_id", referenceId,
								"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

                Report report = new Report();
				report.setQuickLeadId(requestId);
                report.setApplicationId(data.getApplicationId());
                report.setFunction("FICO_RETURN_COMMENT");
                report.setStatus("PROCESSING");
                report.setCreatedBy(token.path("user_name").textValue());
                report.setCreatedDate(new Date());
                mongoTemplate.save(report);
            }

            if (responseCommnentFullAPPFromDigiTex){
//				Query queryUpdate = new Query();
//				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
//				Update update = new Update();
//				update.set("status", "COMPLETED");
//				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

                Report report = new Report();
				report.setQuickLeadId(requestId);
                report.setApplicationId(data.getApplicationId());
                report.setFunction("DIGITEXX_RETURN_COMMENT");
                report.setStatus("FULL_APP_FAIL");
                report.setCreatedBy(token.path("user_name").textValue());
                report.setCreatedDate(new Date());
                mongoTemplate.save(report);
            }
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.toString() + e.getCause() + e.getStackTrace());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> updateStatus(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			requestId = requestModel.getRequest_id();
			Application data = requestModel.getData();
			data.setLastModifiedDate(new Timestamp(new Date().getTime()));

			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);

			if (checkExist.size() <= 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId not exists.");
			}else{
				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));

				Update update = new Update();
				update.set("status", data.getStatus());
				update.set("description", data.getDescription());
				if (data.getStatus().equals("MANUALLY")){
					update.set("userName_DE", token.path("user_name").textValue());
				}
				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

				Application dataFullApp = mongoTemplate.findOne(queryUpdate, Application.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp","reference_id", referenceId,
								"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));


				Report report = new Report();
				report.setQuickLeadId(resultUpdate.getQuickLeadId());
				report.setApplicationId(data.getApplicationId());
				report.setFunction("UPDATESTATUS");
				report.setStatus(data.getStatus());
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());
				mongoTemplate.save(report);

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}
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

	public Map<String, Object> quickLead(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{// check lai id
			Assert.notNull(request.get("body"), "no body");
			RequestQuickLeadModel requestModel = mapper.treeToValue(request.get("body"), RequestQuickLeadModel.class);
			requestId = requestModel.getRequest_id();

			QuickLead data = requestModel.getData();
			data.setCreatedDate(new Timestamp(new Date().getTime()));

			Query query = new Query();
			query.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);

			if (checkExist.size() > 0){
				if (request.path("body").path("data").path("retry").textValue() != null && request.path("body").path("data").path("retry").equals("") != true &&
						request.path("body").path("data").path("retry").textValue().equals("") != true) {
					Query queryGetApp = new Query();
					queryGetApp.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
					List<Application> appData = mongoTemplate.find(queryGetApp, Application.class);

					rabbitMQService.send("tpf-service-automation",
							Map.of("func", "quickLeadApp","body",
									appData.get(0)));
				}else {
					Query queryUpdate = new Query();
					queryUpdate.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));

					Update update = new Update();
					update.set("quickLead.quickLeadId", data.getQuickLeadId());
					update.set("quickLead.identificationNumber", data.getIdentificationNumber());
					update.set("quickLead.productTypeCode", data.getProductTypeCode());
					update.set("quickLead.customerType", data.getCustomerType());
					update.set("quickLead.productCode", data.getProductCode());
					update.set("quickLead.loanAmountRequested", data.getLoanAmountRequested());
					update.set("quickLead.firstName", data.getFirstName());
					update.set("quickLead.lastName", data.getLastName());
					update.set("quickLead.city", data.getCity());
					update.set("quickLead.sourcingChannel", data.getSourcingChannel());
					update.set("quickLead.dateOfBirth", data.getDateOfBirth());
					update.set("quickLead.sourcingBranch", data.getSourcingBranch());
					update.set("quickLead.natureOfOccupation", data.getNatureOfOccupation());
					update.set("quickLead.schemeCode", data.getSchemeCode());
					update.set("quickLead.comment", data.getComment());
					update.set("quickLead.preferredModeOfCommunication", data.getPreferredModeOfCommunication());
					update.set("quickLead.leadStatus", data.getLeadStatus());
					update.set("quickLead.communicationTranscript", data.getCommunicationTranscript());
                    update.set("quickLead.$.documents", data.getDocuments());
					update.set("status", "NEW");

					Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

//				--automation QuickLead--
					Query queryGetApp = new Query();
					queryGetApp.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
					List<Application> appData = mongoTemplate.find(queryGetApp, Application.class);

					rabbitMQService.send("tpf-service-automation",
							Map.of("func", "quickLeadApp", "body",
									appData.get(0)));

					rabbitMQService.send("tpf-service-app",
							Map.of("func", "createApp", "reference_id", referenceId,"body", convertService.toAppDisplay(appData.get(0))));

					Report report = new Report();
					report.setQuickLeadId(data.getQuickLeadId());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("QUICKLEAD");
					report.setStatus("NEW");
					report.setCreatedBy(token.path("user_name").textValue());
					report.setCreatedDate(new Date());
					mongoTemplate.save(report);
				}

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("quickLeadId not exist.");
			}
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

	public Map<String, Object> uploadFile(JsonNode request, JsonNode token) throws Exception {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		String description = "";
		try{
			UUID quickLeadId = UUID.randomUUID();
			List<QLDocument> dataUpload = new ArrayList<>();
			Assert.notNull(request.get("body"), "no body");
			if (request.get("appId").textValue().equals("new")){
				try {
					dataUpload = mapper.readValue(request.path("body").toString(), new TypeReference<List<QLDocument>>() {});
				} catch (IOException e) {
					e.printStackTrace();
				}

				QuickLead quickLead = new QuickLead();
				quickLead.setDocuments(dataUpload);

				Application app = new Application();
				app.setQuickLeadId(quickLeadId.toString());
				app.setQuickLead(quickLead);
				app.setStatus("NEW");
				app.setUserName(token.path("user_name").textValue());
				app.setCreatedDate(new Date());
				mongoTemplate.save(app);

				Map<String, Object> responseUI = new HashMap<>();
				responseUI.put("quickLeadId", quickLeadId);
				responseUI.put("documents", dataUpload);

//				responseModel.setRequest_id(requestId);
				if (request.path("uploadDigiTex").textValue() == null) {
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("0");
					responseModel.setData(responseUI);

					description = "upload Seccess";
				}else{
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("2");
					responseModel.setData(responseUI);
					responseModel.setMessage("uploadFile DigiTex fail!");

					description = "uploadFile DigiTex fail!";
				}
				Report report = new Report();
				report.setQuickLeadId(quickLeadId.toString());
				report.setFunction("UPLOADFILE");
				report.setStatus("NEW");
				report.setDescription(description);
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());
				mongoTemplate.save(report);
			}else {
				Query query = new Query();
				query.addCriteria(Criteria.where("applicationId").is(request.get("appId").asText()));
				List<Application> checkExist = mongoTemplate.find(query, Application.class);
				if (checkExist.size() > 0){
					dataUpload = mapper.readValue(request.path("body").toString(), new TypeReference<List<QLDocument>>() {});
					for (QLDocument item : dataUpload) {
						Query queryUpdate = new Query();
						queryUpdate.addCriteria(Criteria.where("applicationId").is(request.get("appId").asText()).and("quickLead.documentsComment.originalname").is(item.getOriginalname()));
						List<Application> checkCommentExist = mongoTemplate.find(queryUpdate, Application.class);

						if (checkCommentExist.size() <= 0){
							Query queryAddComment = new Query();
							queryAddComment.addCriteria(Criteria.where("applicationId").is(request.get("appId").asText()));

							Update update = new Update();
							update.addToSet("quickLead.documentsComment", item);
							Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, update, FindAndModifyOptions.options().upsert(true), Application.class);
						}else {
							Update update = new Update();
							update.set("quickLead.documentsComment.$.filename", item.getFilename());
							update.set("quickLead.documentsComment.$.urlid", item.getUrlid());
							update.set("quickLead.documentsComment.$.md5", item.getMd5());
							Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
						}
					}

					Map<String, Object> responseUI = new HashMap<>();
					responseUI.put("quickLeadId", quickLeadId);
					responseUI.put("applicationd", request.get("appId").asText());
					responseUI.put("documents", dataUpload);

					if (request.path("uploadDigiTex").textValue() == null) {
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("0");
						responseModel.setData(responseUI);
					}else{
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("2");
						responseModel.setData(responseUI);
						responseModel.setMessage("uploadFile DigiTex fail!");
					}
				}else{
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("1");
					responseModel.setMessage("AppId not exist.");
				}

				Report report = new Report();
				report.setQuickLeadId(quickLeadId.toString());
				report.setApplicationId(request.get("appId").asText());
				report.setFunction("UPLOADFILE_COMMENT");
				report.setStatus("RETURNED");
				report.setDescription(description);
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());
				mongoTemplate.save(report);
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("3");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> uploadDigiTex(JsonNode request, JsonNode token) throws Exception {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			UUID quickLeadId = UUID.randomUUID();
			List<QLDocument> dataUpload = new ArrayList<>();
			Assert.notNull(request.get("body"), "no body");
			if (request.path("body").path("data").path("applicationId").textValue() == null){
				JsonNode resultUpload = apiService.retryUploadDigiTex(request);
				if (resultUpload.path("uploadDigiTex").textValue() == null) {
					dataUpload = mapper.readValue(resultUpload.toString(), new TypeReference<List<QLDocument>>() {
					});
					for (QLDocument item : dataUpload) {
						Query queryUpdate = new Query();
						queryUpdate.addCriteria(Criteria.where("quickLeadId").is(request.get("body").path("data").path("quickLeadId").textValue()).and("quickLead.documents.originalname").is(item.getOriginalname()));

						Update update = new Update();
						update.set("quickLead.documents.$.urlid", item.getUrlid());
						Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
					}

					Map<String, Object> responseUI = new HashMap<>();
					responseUI.put("quickLeadId", request.get("body").path("data").path("quickLeadId").textValue());
					responseUI.put("documents", dataUpload);

					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("0");
					responseModel.setData(responseUI);
				}else{
					Map<String, Object> responseUI = new HashMap<>();
					responseUI.put("quickLeadId", request.get("body").path("data").path("quickLeadId").textValue());

					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("2");
					responseModel.setData(responseUI);
					responseModel.setMessage("uploadFile DigiTex fail!");
				}
			}else{
				JsonNode resultUpload = apiService.retryUploadDigiTex(request);
				if (resultUpload.path("uploadDigiTex").textValue() == null) {
					dataUpload = mapper.readValue(resultUpload.toString(), new TypeReference<List<QLDocument>>() {
					});
					for (QLDocument item : dataUpload) {
						Query queryUpdate = new Query();
						queryUpdate.addCriteria(Criteria.where("applicationId").is(request.get("body").path("data").path("applicationId").textValue()).and("quickLead.documentsComment.originalname").is(item.getOriginalname()));

						Update update = new Update();
						update.set("quickLead.documentsComment.$.urlid", item.getUrlid());
						Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
					}

					Map<String, Object> responseUI = new HashMap<>();
					responseUI.put("applicationId", request.get("body").path("data").path("applicationId").textValue());
					responseUI.put("documents", dataUpload);

					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("0");
					responseModel.setData(responseUI);
				}else{
					Map<String, Object> responseUI = new HashMap<>();
					responseUI.put("applicationId", request.get("body").path("data").path("applicationId").textValue());

					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("2");
					responseModel.setData(responseUI);
					responseModel.setMessage("uploadFile DigiTex fail!");
				}
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("3");
			responseModel.setMessage(e.getMessage());
		}
 		return Map.of("status", 200, "data", responseModel);

	}

	public Map<String, Object> updateAutomation(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Query query = new Query();
			query.addCriteria(Criteria.where("quickLeadId").is(request.path("body").path("quickLeadId").textValue()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				if (request.path("body").path("applicationId").textValue() != null && request.path("body").path("applicationId").equals("") != true &&
						request.path("body").path("applicationId").textValue().equals("") != true && request.path("body").path("applicationId").textValue().equals("UNKNOWN") != true &&
						request.path("body").path("applicationId").textValue().equals("UNKNOW") != true) {
					Update update = new Update();
					update.set("applicationId", request.path("body").path("applicationId").textValue());
					update.set("status", "PROCESSING");
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					String customerName = resultUpdatetest.getQuickLead().getLastName() + " " +
							resultUpdatetest.getQuickLead().getFirstName();
					String idCardNo = resultUpdatetest.getQuickLead().getIdentificationNumber();
					String applicationId = request.path("body").path("applicationId").textValue();
					ArrayList<String> inputQuery = new ArrayList<String>();
					if (resultUpdatetest.getQuickLead().getDocuments() != null) {
						for (QLDocument item : resultUpdatetest.getQuickLead().getDocuments()) {
							if (item.getUrlid() != null) {
								inputQuery.add(item.getUrlid());
							}
						}
					}

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("customer-name", customerName, "id-card-no", idCardNo,
							"application-id", applicationId, "document-ids", inputQuery)), JsonNode.class);
					apiService.callApiDigitexx(urlDigitexCmInfoApi,dataSend);
//					String resultDG =  apiService.callApiDigitexx(urlDigitexCmInfoApi,dataSend);

//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//					headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//					HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(Map.of("customer-name", customerName, "id-card-no", idCardNo,
//							"application-id", applicationId, "document-ids", inputQuery)), headers);
//					ResponseEntity<?> res = restTemplate.postForEntity(urlDigitexCmInfoApi, entity, Object.class);
//					JsonNode body = mapper.valueToTree(res.getBody());

					Report report = new Report();
					report.setQuickLeadId(request.path("body").path("quickLeadId").textValue());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("QUICKLEAD");
					report.setStatus("PROCESSING");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());
					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));
				}else{
					Report report = new Report();
					report.setQuickLeadId(request.path("body").path("quickLeadId").textValue());
					report.setFunction("QUICKLEAD");
					report.setStatus("AUTO_QL_FAIL");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());
					mongoTemplate.save(report);

					Update update = new Update();
//					update.set("applicationId", "UNKNOWN");
					update.set("status", "AUTO_QL_FAIL");
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));
				}

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("quickLeadId not exist.");
			}
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

	public Map<String, Object> updateFullApp(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		String commentId = UUID.randomUUID().toString().substring(0,10);
		String errors = "";
		String applicationId = "";
		try{
			applicationId = request.path("body").path("applicationId").textValue();
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").textValue()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				if (request.path("body").path("status").textValue().equals("OK")) {
					Update update = new Update();
					update.set("status", "COMPLETED");
					update.set("description", request.path("body").path("description").textValue());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					Report report = new Report();
					report.setQuickLeadId(resultUpdatetest.getQuickLeadId());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("SENDFULLAPP");
					report.setStatus("COMPLETED");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());
					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), JsonNode.class);
					apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);

//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//					headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//					HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), headers);
//					ResponseEntity<?> res = restTemplate.postForEntity(urlDigitexFeedbackApi, entity, Object.class);
//					JsonNode body = mapper.valueToTree(res.getBody());

				}else{
					errors = request.path("body").path("stage").textValue();

					Update update = new Update();
					update.set("status", "FULL_APP_FAIL");
					update.set("description", request.path("body").path("description").textValue());
					update.set("stage", request.path("body").path("stage").textValue());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					//save comment
					CommentModel commentModel = new CommentModel();
					commentModel.setCommentId(commentId);
					commentModel.setType("FICO");
					commentModel.setCode("FICO_ERR");
					commentModel.setStage(request.path("body").path("stage").textValue());
					commentModel.setRequest(request.path("body").path("description").textValue());

					Query queryAddComment = new Query();
					queryAddComment.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").textValue()));
					Update updateComment = new Update();
					commentModel.setCreatedDate(new Date());
					updateComment.push("comment", commentModel);
					Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, updateComment, Application.class);

					Report report = new Report();
					report.setQuickLeadId(resultUpdate.getQuickLeadId());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("FICO_COMMENT");
					report.setStatus("FULL_APP_FAIL");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());
					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					//fico gui comment

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "fail",
							"commend-id", commentId, "errors", errors)), JsonNode.class);
					apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);

//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//					headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//					HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "fail",
//							"commend-id", commentId, "errors", errors)), headers);
//					ResponseEntity<?> res = restTemplate.postForEntity(urlDigitexFeedbackApi, entity, Object.class);
//					JsonNode body = mapper.valueToTree(res.getBody());
				}

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId not exist.");
			}
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

	public Map<String, Object> updateAppError(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String applicationId = "";
		String commentId = UUID.randomUUID().toString().substring(0,10);
		String referenceId = UUID.randomUUID().toString();
		String errors = "";
		try{
			applicationId = request.path("body").path("applicationId").textValue();
			errors = request.path("body").path("description").textValue();
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").textValue()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				if (request.path("body").path("status").textValue().equals("OK")) {
					Update update = new Update();
					update.set("status", "COMPLETED");
					update.set("description", request.path("body").path("description").textValue());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					Report report = new Report();
					report.setQuickLeadId(resultUpdatetest.getQuickLeadId());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("UPDATEFULLAPP");
					report.setStatus("COMPLETED");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());
					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), JsonNode.class);
					apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);

//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//					headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//					HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), headers);
//					ResponseEntity<?> res = restTemplate.postForEntity(urlDigitexFeedbackApi, entity, Object.class);
//					JsonNode body = mapper.valueToTree(res.getBody());

				}else{
					errors = request.path("body").path("stage").textValue();

					Update update = new Update();
					update.set("status", "FULL_APP_FAIL");
					update.set("description", request.path("body").path("description").textValue());
					update.set("stage", request.path("body").path("stage").textValue());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					//save comment
					CommentModel commentModel = new CommentModel();
					commentModel.setCommentId(commentId);
					commentModel.setType("FICO");
					commentModel.setCode("FICO_ERR");
					commentModel.setStage(request.path("body").path("stage").textValue());
					commentModel.setRequest(request.path("body").path("description").textValue());

					Query queryAddComment = new Query();
					queryAddComment.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").textValue()));
					Update updateComment = new Update();
					commentModel.setCreatedDate(new Date());
					updateComment.push("comment", commentModel);
					Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, updateComment, Application.class);

					Report report = new Report();
					report.setQuickLeadId(resultUpdate.getQuickLeadId());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("FICO_COMMENT");
					report.setStatus("FULL_APP_FAIL");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());
					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					//fico gui comment

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "fail",
							"commend-id", commentId, "errors", errors)), JsonNode.class);
					apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);

//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//					headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//					HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "fail",
//							"commend-id", commentId, "errors", errors)), headers);
//					ResponseEntity<?> res = restTemplate.postForEntity(urlDigitexFeedbackApi, entity, Object.class);
//					JsonNode body = mapper.valueToTree(res.getBody());
				}

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId not exist.");
			}
		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

    public Map<String, Object> getTATReport(JsonNode request, JsonNode token) {
        ResponseModel responseModel = new ResponseModel();
        String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		ByteArrayInputStream in = null;
        try{
            Assert.notNull(request.get("body"), "no body");
			Query query = new Query();

			if (request.path("body").path("data").path("fromDate").textValue() != null && !request.path("body").path("data").path("fromDate").textValue().equals("")
					&& request.path("body").path("data").path("toDate").textValue() != null && !request.path("body").path("data").path("toDate").textValue().equals("")){
				Timestamp fromDate = Timestamp.valueOf(request.path("body").path("data").path("fromDate").textValue() + " 00:00:00");
				Timestamp toDate = Timestamp.valueOf(request.path("body").path("data").path("toDate").textValue() + " 23:23:59");

				query.addCriteria(Criteria.where("createdDate").gte(fromDate).lte(toDate));
			}

            List<Report> listData = mongoTemplate.find(query, Report.class);
            // export excel
			in = tatReportToExcel(listData);

            if (listData.size() > 0){
                responseModel.setRequest_id(requestId);
                responseModel.setReference_id(UUID.randomUUID().toString());
                responseModel.setDate_time(new Timestamp(new Date().getTime()));
                responseModel.setResult_code("0");
                responseModel.setData(listData);
            }else{
                responseModel.setRequest_id(requestId);
                responseModel.setReference_id(UUID.randomUUID().toString());
                responseModel.setDate_time(new Timestamp(new Date().getTime()));
                responseModel.setResult_code("1");
                responseModel.setMessage("no data");
            }
        }
        catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
            responseModel.setRequest_id(requestId);
            responseModel.setReference_id(referenceId);
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code("1");
            responseModel.setMessage(e.getMessage());
        }
        return Map.of("status", 200, "data", Base64.getEncoder().encodeToString(in.readAllBytes()));
    }

    public Map<String, Object> getStatusReport(JsonNode request, JsonNode token) {
        ResponseModel responseModel = new ResponseModel();
        String requestId = request.path("body").path("request_id").textValue();
        String referenceId = UUID.randomUUID().toString();
		ByteArrayInputStream in = null;
        try{
            Assert.notNull(request.get("body"), "no body");
			AggregationOperation match1;

            List<String> inputQuery = new ArrayList<String>();
            inputQuery.add("COMPLETED");
            inputQuery.add("RETURNED");
            inputQuery.add("RESPONSED");
			inputQuery.add("PROCESSING");

			if (request.path("body").path("data").path("fromDate").textValue() != null && !request.path("body").path("data").path("fromDate").textValue().equals("")
					&& request.path("body").path("data").path("toDate").textValue() != null && !request.path("body").path("data").path("toDate").textValue().equals("")){
				Timestamp fromDate = Timestamp.valueOf(request.path("body").path("data").path("fromDate").textValue() + " 00:00:00");
				Timestamp toDate = Timestamp.valueOf(request.path("body").path("data").path("toDate").textValue() + " 23:23:59");

				match1 = Aggregation.match(Criteria.where("createdDate").gte(fromDate).lte(toDate).and("status").in(inputQuery));
			}else{
				match1 = Aggregation.match(Criteria.where("status").in(inputQuery));
			}

//			AggregationOperation match1 = Aggregation.match(Criteria.where("createdDate").gte(fromDate).lte(toDate).and("status").in(inputQuery));
            AggregationOperation group = Aggregation.group("status").count().as("count");
            AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "count");
            AggregationOperation project = Aggregation.project().andExpression("_id").as("status").andExpression("count").as("appNo");
            //AggregationOperation limit = Aggregation.limit(Constants.BOARD_TOP_LIMIT);
            Aggregation aggregation = Aggregation.newAggregation(match1, group, sort, project/*, limit*/);
            AggregationResults<ReportStatus> results = mongoTemplate.aggregate(aggregation, Report.class, ReportStatus.class);

            List<ReportStatus> resultData = results.getMappedResults();

			in = statusReportToExcel(resultData);

            if (resultData.size() > 0){
                responseModel.setRequest_id(requestId);
                responseModel.setReference_id(UUID.randomUUID().toString());
                responseModel.setDate_time(new Timestamp(new Date().getTime()));
                responseModel.setResult_code("0");
                responseModel.setData(resultData);
            }else{
                responseModel.setRequest_id(requestId);
                responseModel.setReference_id(UUID.randomUUID().toString());
                responseModel.setDate_time(new Timestamp(new Date().getTime()));
                responseModel.setResult_code("1");
                responseModel.setMessage("no data");
            }
        }
        catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
            responseModel.setRequest_id(requestId);
            responseModel.setReference_id(referenceId);
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code("1");
            responseModel.setMessage(e.getMessage());
        }
        return Map.of("status", 200, "data", Base64.getEncoder().encodeToString(in.readAllBytes()));
    }

	public Map<String, Object> getDocumentId(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String referenceId = UUID.randomUUID().toString();
		ByteArrayInputStream in = null;
		try{
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(request.path("appId").textValue()));
			Application dataFullApp = mongoTemplate.findOne(query, Application.class);


			if (dataFullApp != null){
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(dataFullApp.getQuickLead().getDocuments());

			}else{
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("no data");
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public static ByteArrayInputStream tatReportToExcel(List<Report> report) throws IOException {
		String[] COLUMNs = {"Seq", "App no.", "Create Date", "Create By", "Status"};
		try(
				Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
		){
			CreationHelper createHelper = workbook.getCreationHelper();

			Sheet sheet = workbook.createSheet("tatreport");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(COLUMNs[col]);
				cell.setCellStyle(headerCellStyle);
			}

			// CellStyle for Age
			CellStyle ageCellStyle = workbook.createCellStyle();
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

			int rowIdx = 1;
			for (Report item : report) {
				Row row = sheet.createRow(rowIdx++);

				CellStyle cellStyle = workbook.createCellStyle();
				CreationHelper createHelper_2 = workbook.getCreationHelper();
				short dateFormat = createHelper_2.createDataFormat().getFormat("M/d/yyyy HH:MM AM/PM");
				cellStyle.setDataFormat(dateFormat);

				row.createCell(0).setCellValue(rowIdx - 1);
				row.createCell(1).setCellValue(item.getApplicationId());

				Cell cell = row.createCell(2);
				cell.setCellValue(Calendar.getInstance());
				cell.setCellStyle(cellStyle);

//				row.createCell(2).setCellValue(item.getCreatedDate());
				row.createCell(3).setCellValue(item.getCreatedBy());
				row.createCell(4).setCellValue(item.getStatus());

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	public static ByteArrayInputStream statusReportToExcel (List<ReportStatus> report) throws IOException {
		String[] COLUMNs = {"App no.", "Status"};
		try(
				Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
		){
			CreationHelper createHelper = workbook.getCreationHelper();

			Sheet sheet = workbook.createSheet("statusreport");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(COLUMNs[col]);
				cell.setCellStyle(headerCellStyle);
			}

			// CellStyle for Age
			CellStyle ageCellStyle = workbook.createCellStyle();
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

			int rowIdx = 1;
			for (ReportStatus item : report) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(item.getAppNo());
				row.createCell(1).setCellValue(item.getStatus());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	public Map<String, Object> getListStatus(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String referenceId = UUID.randomUUID().toString();
		try{
			Query query = new Query();
			query.addCriteria(Criteria.where("status").ne(null));

			List<String> listStatus= mongoTemplate.findDistinct(query,"status",Application.class,String.class);

			if (listStatus.size() > 0){
				responseModel.setReference_id(referenceId);
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(listStatus);
				return Map.of("status", 200, "data", responseModel);
			}else{
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				return Map.of("status", 200, "data", responseModel);
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getSearchReport(JsonNode request, JsonNode token) {
		long total=0;
		int page=1;
		int limit=10;
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		ByteArrayInputStream in = null;
		try{
			Assert.notNull(request.get("body"), "no body");

			Criteria criteria=new Criteria();

			if(request.path("body").path("data").path("applicationId").textValue()!=null)
			{
				criteria=Criteria.where("applicationId").is(request.path("body").path("data").path("applicationId").textValue());
			}
			else{
				criteria=Criteria.where("applicationId").ne(null);
			}

			if (request.path("body").path("data").path("fromDate").textValue() != null && request.path("body").path("data").path("toDate").textValue() != null){
				Timestamp fromDate = Timestamp.valueOf(request.path("body").path("data").path("fromDate").textValue() + " 00:00:00");
				Timestamp toDate = Timestamp.valueOf(request.path("body").path("data").path("toDate").textValue() + " 23:23:59");

				criteria.and("createdDate").gte(fromDate).lte(toDate);
			}

			if(request.path("body").path("data").path("status").textValue()!=null)
			{
				criteria.and("status").is(request.path("body").path("data").path("status").textValue());
			}else
			{
				criteria.and("status").ne(null);
			}

			if(request.path("body").path("data").path("createBy").textValue()!=null)
			{
				criteria.and("userName").is(request.path("body").path("data").path("createBy").textValue());
			}

			List<Application> resultData=new ArrayList<Application>();

			//get by page, limit
			if(!request.path("body").path("data").path("page").isNull() && !request.path("body").path("data").path("limit").isNull()) {
				page = request.path("body").path("data").path("page").asInt(1);
				limit = request.path("body").path("data").path("limit").asInt(10);

				MatchOperation matchOperation=Aggregation.match(criteria);
				LimitOperation limitOperation=Aggregation.limit(limit);
				SkipOperation skipOperation=Aggregation.skip((long)(page-1) * limit);
				Aggregation aggregation = Aggregation.newAggregation(matchOperation,skipOperation,limitOperation);

				total=mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation), Application.class,Application.class).getMappedResults().size();

				AggregationResults<Application> output = mongoTemplate.aggregate(aggregation, Application.class,Application.class);
				resultData = output.getMappedResults();

			}else//get all
			{
				MatchOperation matchOperation=Aggregation.match(criteria);
				Aggregation aggregation = Aggregation.newAggregation(matchOperation);

				total=mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation), Application.class,Application.class).getMappedResults().size();

				AggregationResults<Application> output = mongoTemplate.aggregate(aggregation, Application.class,Application.class);
				resultData = output.getMappedResults();
			}


			List<Object> resultCustome=resultData.stream().map(temp -> {
				ReportModel obj = new ReportModel();
				obj.setApplicationId(temp.getApplicationId());
				obj.setStatus(temp.getStatus());
				if(temp.getApplicationInformation()!=null)
				{
					obj.setFullName(temp.getApplicationInformation().getPersonalInformation().getPersonalInfo().getFullName());
					obj.setIdentificationNumber(temp.getApplicationInformation().getPersonalInformation().getIdentifications()
							.stream().filter(c->c.getIdentificationType().equals("Current National ID")).findAny().isPresent()
							?temp.getApplicationInformation().getPersonalInformation().getIdentifications()
							.stream().filter(c->c.getIdentificationType().equals("Current National ID")).findAny().get().getIdentificationNumber():"");
				}
				obj.setCreatedDate(temp.getCreatedDate());
				return obj;
			}).collect(Collectors.toList());


			if (resultCustome.size() > 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(resultCustome
				);
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("no data");
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel,"total",total);
	}

	public boolean isValidIdNumer(String strNum) {
		if(!strNum.matches("^[0-9]+$") && strNum.length()!=9 && strNum.length()!=12)
		{
			return false;
		}
		return true;
	}

	public boolean isNumer(String strNum) {
		if(!strNum.matches("^[0-9]+$"))
		{
			return false;
		}
		return true;
	}
}