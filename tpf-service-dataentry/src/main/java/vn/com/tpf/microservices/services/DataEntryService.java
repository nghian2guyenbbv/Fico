package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.exception.DataEntryException;
import vn.com.tpf.microservices.models.*;
import vn.com.tpf.microservices.shared.ThirdPartyType;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregationOptions;

@Service
public class DataEntryService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.queue.automation}")
	private String queueAutoSGB;

	@Value("${spring.url.digitex-cminfoapi}")
	private String urlDigitexCmInfoApi;

	@Value("${spring.url.digitex-resubmitcommentapi}")
	private String urlDigitexResubmitCommentApi;

	@Value("${spring.url.digitex-feedbackapi}")
	private String urlDigitexFeedbackApi;

	@Value("${spring.url.esb.create-lead}")
	private String urlCreateLead;

	@Value("${spring.url.esb.update-app}")
	private String urlUpdateApp;

	@Value("${spring.url.esb.get-status}")
	private String urlGetStatus;

	@Value("${spring.url.esb.wait-time}")
	private long waitTime;

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

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private RestTemplate restTemplate;

	@Autowired
	private GetDataF1Service getDataF1Service;

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
//			query.with(new Sort(Sort.Direction.DESC, "comment.createdDate"));

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
//			Assert.notNull(request.path("body").path("request_id"), "no body");
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

//			responseModel.setRequest_id(requestId);
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code("0");
//			responseModel.setData(Map.of("first_check_result", "pass"));

			String resultFirstCheck = apiService.firstCheck(request, token);
			if (resultFirstCheck.equals("pass")){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(Map.of("first_check_result", "pass"));
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(Map.of("first_check_result", "fail"));
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

	public Map<String, Object> sendApp(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		Date startDate = new Date();
		long duration = 0;
		long durationValidate = 0;
		long durationInsert = 0;
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

//				//check hold
//				if(checkExist != null && checkExist.isHolding()){
//					if(!request.get("body").path("data").hasNonNull("isFeedBack")){
//						this.responseToPartner(checkExist);
//					}
//					responseModel.setRequest_id(requestId);
//					responseModel.setReference_id(referenceId);
//					responseModel.setDate_time(new Timestamp(new Date().getTime()));
//					responseModel.setResult_code("1");
//					responseModel.setMessage("Application is hold");
//
//					return Map.of("status", 200, "data", responseModel);
//				}

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

						String validIdentifications = null;
						String validaddresses = null;
						for (Identification item : data.getApplicationInformation().getPersonalInformation().getIdentifications()) {
							if (item.getIdentificationType().toUpperCase().equals("Current National ID".toUpperCase())){
								if (item.getIdentificationNumber() == null || item.getIdentificationNumber().equals("")){
									validIdentifications = validIdentifications + "identificationNumber not null;";
								}
								if (item.getIssuingCountry() == null || item.getIssuingCountry().equals("")){
									validIdentifications = validIdentifications + "issuingCountry not null;";
								}
								if (item.getPlaceOfIssue() == null || item.getPlaceOfIssue().equals("")){
									validIdentifications = validIdentifications + "placeOfIssue not null;";
								}
								if (item.getIssueDate() == null || item.getIssueDate().equals("")){
									validIdentifications = validIdentifications + "issueDate not null;";
								}
							}
						}
						if (validIdentifications != null){
							responseModel.setRequest_id(requestId);
							responseModel.setReference_id(UUID.randomUUID().toString());
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code("1");
							responseModel.setMessage("Current National ID: " + validIdentifications);

							return Map.of("status", 200, "data", responseModel);
						}

						for (Address item : data.getApplicationInformation().getPersonalInformation().getAddresses()) {
							if (item.getAddressType().toUpperCase().equals("Current Address".toUpperCase())){
								if (item.getCountry() == null || item.getCountry().equals("")){
									validaddresses = validaddresses + "country not null;";
								}
								if (item.getState() == null || item.getState().equals("")){
									validaddresses = validaddresses + "state not null;";
								}
								if (item.getCity() == null || item.getCity().equals("")){
									validaddresses = validaddresses + "city not null;";
								}
								if (item.getZipcode() == null || item.getZipcode().equals("")){
									validaddresses = validaddresses + "zipcode not null;";
								}
								if (item.getArea() == null || item.getArea().equals("")){
									validaddresses = validaddresses + "area not null;";
								}
								if (item.getAddressLine1() == null || item.getAddressLine1().equals("")){
									validaddresses = validaddresses + "addressLine1 not null;";
								}
								if (item.getAddressLine2() == null || item.getAddressLine2().equals("")){
									validaddresses = validaddresses + "addressLine2 not null;";
								}
								if (item.getAddressLine3() == null || item.getAddressLine3().equals("")){
									validaddresses = validaddresses + "addressLine3 not null;";
								}
								if (item.getMonthsInCurrentAddress() == null || item.getMonthsInCurrentAddress().equals("")){
									validaddresses = validaddresses + "monthsInCurrentAddress not null;";
								}
								if (item.getYearsInCurrentAddress() == null || item.getYearsInCurrentAddress().equals("")){
									validaddresses = validaddresses + "yearsInCurrentAddress not null;";
								}

								if (item.getPhoneNumbers().size() <= 0){
									validaddresses = validaddresses + "phoneNumbers not null;";
								}else{
									for (PhoneNumber item2 : item.getPhoneNumbers()) {
										if (item2.getPhoneType().toUpperCase().equals("Mobile Phone".toUpperCase())){
											if (item2.getIsdCode() == null || item2.getIsdCode().equals("")){
												validaddresses = validaddresses + "isdCode not null;";
											}
											if (item2.getPhoneNumber() == null || item2.getPhoneNumber().equals("")){
												validaddresses = validaddresses + "phoneNumber not null;";
											}
											if (item2.getCountryCode() == null || item2.getCountryCode().equals("")){
												validaddresses = validaddresses + "countryCode not null;";
											}
										}
									}
								}
							}
						}
						if (validaddresses != null){
							responseModel.setRequest_id(requestId);
							responseModel.setReference_id(UUID.randomUUID().toString());
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code("1");
							responseModel.setMessage("Current Address: " + validaddresses);

							return Map.of("status", 200, "data", responseModel);
						}
						durationValidate = Duration.between(startDate.toInstant(), new Date().toInstant()).toSeconds();

						Update update = new Update();
						update.set("applicationInformation", data.getApplicationInformation());
						update.set("loanDetails", data.getLoanDetails());
						update.set("references", data.getReferences());
						update.set("dynamicForm", data.getDynamicForm());
						update.set("lastModifiedDate", new Date());
						Application resultUpdate = mongoTemplate.findAndModify(query, update, Application.class);

//						--automation fullapp--
						Application dataFullApp = mongoTemplate.findOne(query, Application.class);

						durationInsert = Duration.between(startDate.toInstant(), new Date().toInstant()).toSeconds();

						if (dataFullApp.getQuickLead().getDocumentsComment() != null){
							dataFullApp.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
						}

						if(apiService.chooseAutoOrApiF1() == 1) {
							new Thread(() -> {
								try{
									ObjectNode result = mapper.createObjectNode();
									result.put("request_id", request.path("body").path("request_id").textValue());
									result.put("applicationId", dataFullApp.getApplicationId());

									JsonNode response = apiService.callApiF1(urlUpdateApp, convertService.toAppApiF1(dataFullApp));
									result.set("response", response);
									if(!response.hasNonNull("errMsg") && response.findPath("responseCode").asInt(1) == 0){
										Thread.sleep(waitTime);
										JsonNode body = mapper.convertValue(Map.of("loanApplicationNumber", dataFullApp.getApplicationId()), JsonNode.class);
										JsonNode getStage = apiService.callApiF1(urlGetStatus, body);
										result.set("stage", getStage);
									}
									updateFullAppApiF1(result);
								}catch(Exception e){
									ObjectNode logInfo = mapper.createObjectNode();
									logInfo.put("applicationId", dataFullApp.getApplicationId());
									logInfo.put("exception", e.toString());
									log.info("{}", logInfo);
								}
							}).start();
						}else{
							rabbitMQService.send(queueAutoSGB, Map.of("func", "fullInfoApp","body", dataFullApp));
						}

						rabbitMQService.send("tpf-service-app",
								Map.of("func", "updateApp","reference_id", referenceId,
										"param", Map.of("project", "dataentry", "id", dataFullApp.getId()), "body", convertService.toAppDisplay(dataFullApp)));

						Report report = new Report();
						report.setQuickLeadId(dataFullApp.getQuickLeadId());
						report.setApplicationId(data.getApplicationId());
						report.setFunction("SENDAPP");
						report.setStatus("PROCESSING");
						report.setCreatedBy(token.path("user_name").textValue());
						report.setCreatedDate(new Date());
						if(dataFullApp != null){
							report.setPartnerId(dataFullApp.getPartnerId());
							report.setPartnerName(dataFullApp.getPartnerName());
						}
						mongoTemplate.save(report);

						new Thread(() -> {
							try {
								String resultInsertORA = insertToOracle_App(dataFullApp);
								if (!resultInsertORA.equals("success")) {
									log.info("ReferenceId : " + referenceId + "; Insert to Oracle: " + resultInsertORA);
								}
							}
							catch (Exception e) {}
						}).start();

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

			duration = Duration.between(startDate.toInstant(), new Date().toInstant()).toSeconds();
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel, "duration", "validate: " + durationValidate + " insert: " + durationInsert + " duration: " + duration);
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
		boolean responseCommnentToDigiTexDuplicate = false;
		String applicationId = "";
		String commentId = "";
		String comment = "";
		String stageAuto = "";
		String errorAuto = "";
		String quickLeadId = "";
		String commentDescription = "";
		String commentType = "";
		List<Document> documentCommnet = new ArrayList<Document>();
		boolean responseCommnentFullAPPFromDigiTex = false;
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			requestId = requestModel.getRequest_id();
			Application data = requestModel.getData();

			data.setLastModifiedDate(new Timestamp(new Date().getTime()));
			applicationId = data.getApplicationId();

			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);

			String partnerId = "";
			String partnerName = "";

			if (checkExist.size() <= 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId not exists.");
			} else {
				try {
					if (checkExist.get(0).getStatus().equals("COMPLETED")) {
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(referenceId);
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("applicationId is completed!");

						return Map.of("status", 200, "data", responseModel);
					}

				} catch (Exception ex) {}

				if (StringUtils.hasLength(checkExist.get(0).getCreateFrom()) && !checkExist.get(0).getStatus().equals("FULL_APP_FAIL")){
					return commentAppForOtherService(requestId, data, checkExist.get(0), request);
				}
				partnerId = checkExist.get(0).getPartnerId();
				partnerName = checkExist.get(0).getPartnerName();
				if (StringUtils.isEmpty(partnerId)) {
					return Map.of("status", 200, "data", "partnerId null");
				}


				for (CommentModel item : data.getComment()) {
//					documentCommnet = item.getResponse().getDocuments();
					commentId = item.getCommentId();
					Query queryUpdate = new Query();
					queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()).and("comment.commentId").is(item.getCommentId()));
					List<Application> checkCommentExist = mongoTemplate.find(queryUpdate, Application.class);
//					String typeComment = checkCommentExist.get(0).getComment().get(0).getType();

					if (checkCommentExist.size() <= 0) {
						if (ThirdPartyType.fromName(item.getType().toUpperCase()) != null) {// digitex gui comment
							Query queryAddComment = new Query();
							queryAddComment.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));

							Update update = new Update();
							item.setCreatedDate(new Date());
							update.push("comment", item);
							Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, update, Application.class);
							if (item.getRequest() != null) {
								commentDescription = item.getRequest();
							}
							requestCommnentFromDigiTex = true;

							new Thread(() -> {
								try {
									String resultInsertORA = insertToOracle_Return(data.getApplicationId(), item.getCommentId(), item.getType(), item.getRequest(), null,
											token.path("user_name").textValue(), item.getCreatedDate(), null);
									if (!resultInsertORA.equals("success")) {
										log.info("ReferenceId : " + referenceId + "; Insert to Oracle Return: " + resultInsertORA);
									}
								}
								catch (Exception e) {}
							}).start();

						}else{
							responseModel.setRequest_id(requestId);
							responseModel.setReference_id(referenceId);
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code("1");
							responseModel.setMessage("Comment Type Invalid!");

							return Map.of("status", 200, "data", responseModel);
						}
					} else {
						if (item.getType().equals("FICO")) {// digitex tra comment
//						if (checkCommentExist.get(0).getComment().get(0).getType().equals("FICO")) {// digitex tra comment(do digites k gui lai type nen k dung item.getType())
							boolean checkResponseComment = false;
							Date createdDate = new Date();
							Date updatedDate = new Date();
							String commentDGT = "";
							String commentDGTLD;

							List<CommentModel> listComment = checkCommentExist.get(0).getComment();
							if (checkCommentExist.get(0).getError() != null) {
								errorAuto = checkCommentExist.get(0).getError();
							}

							//validate so luong tra comment tu digitex
//							int countCommentDG = 0;
//							for (CommentModel itemComment : listComment) {
//								if (itemComment.getType().equals("FICO")) {
//									if (itemComment.getResponse() != null) {
//										countCommentDG = countCommentDG + 1;
//									}
//								}
//							}
//							if (countCommentDG > 3){
//								responseModel.setRequest_id(requestId);
//								responseModel.setReference_id(referenceId);
//								responseModel.setDate_time(new Timestamp(new Date().getTime()));
//								responseModel.setResult_code("1");
//								responseModel.setMessage("application can not retry!");
//								return Map.of("status", 200, "data", responseModel);
//							}
							//

							try {
								quickLeadId = checkCommentExist.get(0).getQuickLeadId();
							} catch (Exception ex) {
							}

							for (CommentModel itemComment : listComment) {
								if (itemComment.getCommentId().equals(item.getCommentId())) {
									if (itemComment.getResponse() == null) {
										stageAuto = itemComment.getStage();
										Update update = new Update();
										update.set("comment.$.response", item.getResponse());
										Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

										checkResponseComment = true;
										createdDate = itemComment.getCreatedDate();
										updatedDate = new Date();
										commentDGT = item.getResponse().getComment();
									}
								}
							}

							if (!checkResponseComment) {
								responseModel.setRequest_id(requestId);
								responseModel.setReference_id(referenceId);
								responseModel.setDate_time(new Timestamp(new Date().getTime()));
								responseModel.setResult_code("1");
								responseModel.setMessage("applicationId can not return comment!");

								return Map.of("status", 200, "data", responseModel);
							}

							responseCommnentFullAPPFromDigiTex = true;

							// update automation
							if (item.getResponse().getData() != null) {
								Application dataUpdate = item.getResponse().getData();
								if (checkCommentExist.get(0).getQuickLead().getDocumentsComment() != null){
									dataUpdate.setDocuments(checkCommentExist.get(0).getQuickLead().getDocumentsComment());
								}
								dataUpdate.setQuickLead(checkCommentExist.get(0).getQuickLead());

//								if (checkCommentExist.get(0).getQuickLead().getDocumentsAfterSubmit() != null) {
//									dataUpdate.setDocuments(checkCommentExist.get(0).getQuickLead().getDocumentsAfterSubmit());
//								}else if (checkCommentExist.get(0).getQuickLead().getDocumentsComment() != null){
//                                    dataUpdate.setDocuments(checkCommentExist.get(0).getQuickLead().getDocumentsComment());
//                                }
								dataUpdate.setStage(stageAuto);
								dataUpdate.setError(errorAuto);
								if(apiService.chooseAutoOrApiF1() == 1) {
									new Thread(() -> {
										try{
											ObjectNode result = mapper.createObjectNode();
											result.put("request_id", request.path("body").path("request_id").textValue());
											result.put("applicationId", dataUpdate.getApplicationId());

											JsonNode response = apiService.callApiF1(urlUpdateApp, convertService.toAppApiF1(dataUpdate));
											result.set("response", response);
											if(!response.hasNonNull("errMsg") && response.findPath("responseCode").asInt(1) == 0){
												Thread.sleep(waitTime);
												JsonNode body = mapper.convertValue(Map.of("loanApplicationNumber", dataUpdate.getApplicationId()), JsonNode.class);
												JsonNode getStage = apiService.callApiF1(urlGetStatus, body);
												result.set("stage", getStage);
											}
											updateAppErrorApiF1(result);
										}catch(Exception e){
											ObjectNode logInfo = mapper.createObjectNode();
											logInfo.put("applicationId", dataUpdate.getApplicationId());
											logInfo.put("exception", e.toString());
											log.info("{}", logInfo);
										}
									}).start();
								}else{
									rabbitMQService.send(queueAutoSGB,
											Map.of("func", "updateAppError","body", dataUpdate));
								}

								commentDGTLD = commentDGT;
								new Thread(() -> {
									try {
										dataUpdate.setQuickLead(checkCommentExist.get(0).getQuickLead());
										dataUpdate.setCreatedDate(checkCommentExist.get(0).getCreatedDate());
										dataUpdate.setLastModifiedDate(checkCommentExist.get(0).getLastModifiedDate());
										String resultInsertORA = insertToOracle_App(dataUpdate);
										if (!resultInsertORA.equals("success")) {
											log.info("ReferenceId : " + referenceId + "; Insert to Oracle: " + resultInsertORA);
										}
									}
									catch (Exception e) {}
									try {
										String resultInsertORA = insertToOracle_Return(data.getApplicationId(), item.getCommentId(), item.getType(), item.getRequest(), commentDGTLD,
												token.path("user_name").textValue(), null, new Date());
										if (!resultInsertORA.equals("success")) {
											log.info("ReferenceId : " + referenceId + "; Insert to Oracle Return: " + resultInsertORA);
										}
									}
									catch (Exception e) {}
								}).start();
							}
						}else{//fico tra comment
							try{
								if (item.getResponse().getComment() == null || item.getResponse().getComment().equals("")){
									responseModel.setRequest_id(requestId);
									responseModel.setReference_id(referenceId);
									responseModel.setDate_time(new Timestamp(new Date().getTime()));
									responseModel.setResult_code("1");
									responseModel.setMessage("vui lòng nhập comment!");
									return Map.of("status", 200, "data", responseModel);
								}
							} catch (Exception ex) {

							}

							documentCommnet = item.getResponse().getDocuments();
							List<CommentModel> listComment = checkCommentExist.get(0).getComment();
							for (CommentModel itemComment : listComment) {
								if (itemComment.getCommentId().equals(item.getCommentId())) {
									if (item.getResponse() != null) {
//										if (itemComment.getResponse() == null) {
										if (item.getResponse().getDocuments().size() > 0) {
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
										new Thread(() -> {
											try {
												String resultInsertORA = insertToOracle_Return(data.getApplicationId(), item.getCommentId(), item.getType(), null, item.getResponse().getComment(),
														token.path("user_name").textValue(), item.getCreatedDate(), new Date());
												if (!resultInsertORA.equals("success")) {
													log.info("ReferenceId : " + referenceId + "; Insert to Oracle Return: " + resultInsertORA);
												}
											}
											catch (Exception e) {}
										}).start();
										responseCommnentToDigiTexDuplicate = true;
//										}
									}
								}
							}

							responseCommnentToDigiTex = true;

						}
					}
				}
			}
			if (requestCommnentFromDigiTex) {
				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
				Update update = new Update();
				update.set("status", "RETURNED");
				update.set("lastModifiedDate", new Date());
				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

				Application dataFullApp = mongoTemplate.findOne(query, Application.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp", "reference_id", referenceId,
								"param", Map.of("project", "dataentry", "id", dataFullApp.getId()), "body", convertService.toAppDisplay(dataFullApp)));


				Report report = new Report();
				report.setQuickLeadId(dataFullApp.getQuickLeadId());
				report.setApplicationId(data.getApplicationId());
				report.setFunction("DIGITEXX_COMMENT");
				report.setStatus("RETURNED");
				report.setCommentDescription(commentDescription);
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());
				if (dataFullApp != null) {
					report.setPartnerId(dataFullApp.getPartnerId());
					report.setPartnerName(dataFullApp.getPartnerName());
				}
				mongoTemplate.save(report);
			}


			if (responseCommnentToDigiTex) {
//                if (responseCommnentToDigiTexDuplicate) { // bo check tra comment nhieu lan
				ArrayNode documents = mapper.createArrayNode();
				boolean checkIdCard = false;
				boolean checkHousehold = false;
				for (Document item : documentCommnet) {
					ObjectNode doc = mapper.createObjectNode();

					if (item.getType().toUpperCase().equals("TPF_ID Card".toUpperCase())) {
						if (!checkIdCard) {
							if (item.getComment() != null) {
								doc.put("documentComment", item.getComment());
							} else {
								doc.put("documentComment", "");
							}
							if (item.getLink() != null) {
								doc.put("documentId", item.getLink().getUrlPartner());
								documents.add(doc);
							}

							checkIdCard = true;
						}
					} else if (item.getType().toUpperCase().equals("TPF_Notarization of ID card".toUpperCase())) {
						if (!checkIdCard) {
							if (item.getComment() != null) {
								doc.put("documentComment", item.getComment());
							} else {
								doc.put("documentComment", "");
							}
							if (item.getLink() != null) {
								doc.put("documentId", item.getLink().getUrlPartner());
								documents.add(doc);
							}

							checkIdCard = true;
						}
					}
					if (item.getType().toUpperCase().equals("TPF_Family Book".toUpperCase())) {
						if (!checkHousehold) {
							if (item.getComment() != null) {
								doc.put("documentComment", item.getComment());
							} else {
								doc.put("documentComment", "");
							}
							if (item.getLink() != null) {
								doc.put("documentId", item.getLink().getUrlPartner());
								documents.add(doc);
							}

							checkHousehold = true;
						}
					} else if (item.getType().toUpperCase().equals("TPF_Notarization of Family Book".toUpperCase())) {
						if (!checkHousehold) {
							if (item.getComment() != null) {
								doc.put("documentComment", item.getComment());
							} else {
								doc.put("documentComment", "");
							}
							if (item.getLink() != null) {
								doc.put("documentId", item.getLink().getUrlPartner());
								documents.add(doc);
							}

							checkHousehold = true;
						}
					} else if (item.getType().toUpperCase().equals("TPF_Customer Photograph".toUpperCase())) {
						if (item.getComment() != null) {
							doc.put("documentComment", item.getComment());
						} else {
							doc.put("documentComment", "");
						}
						if (item.getLink() != null) {
							doc.put("documentId", item.getLink().getUrlPartner());
							documents.add(doc);
						}
					} else if (item.getType().toUpperCase().equals("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
						if (item.getComment() != null) {
							doc.put("documentComment", item.getComment());
						} else {
							doc.put("documentComment", "");
						}
						if (item.getLink() != null) {
							doc.put("documentId", item.getLink().getUrlPartner());
							documents.add(doc);
						}
					}
				}

				JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "comment-id", commentId,
						"comment", comment, "documents", documents)), JsonNode.class);

				Map partner = getPartner(partnerId);
				if (StringUtils.isEmpty(partner.get("data"))) {
					return Map.of("result_code", 3, "message", "Not found partner");
				}
				try {
					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					String resubmitCommentApi = (String) mapper.convertValue(url, Map.class).get("resubmitCommentApi");

					JsonNode responseDG = mapper.createObjectNode();

					if (partnerId.equals("1")) {
						String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
						responseDG = apiService.callApiPartner(resubmitCommentApi, dataSend, tokenPartner, partnerId);
//						responseDG = apiService.callApiDigitexx(urlDigitexResubmitCommentApi, dataSend);
					} else if (partnerId.equals("2")) {
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if (StringUtils.isEmpty(tokenPartner)) {
							return Map.of("result_code", 3, "message", "Not get token saigon-bpo");
						}
						responseDG = apiService.callApiPartner(resubmitCommentApi, dataSend, tokenPartner, partnerId);
					}

					if (!responseDG.path("error-code").textValue().equals("")) {
						if (!responseDG.path("error-code").textValue().equals("null")) {
							log.info("ReferenceId : " + referenceId);
							responseModel.setRequest_id(requestId);
							responseModel.setReference_id(referenceId);
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code("1");
							responseModel.setMessage(responseDG.path("error-code").textValue() + responseDG.path("error-description").textValue());

							return Map.of("status", 200, "data", responseModel);
						}
					}
				} catch (Exception ex) {}

				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
				Update update = new Update();
				update.set("status", "PROCESSING");
				update.set("lastModifiedDate", new Date());
				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

				Application dataFullApp = mongoTemplate.findOne(query, Application.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp", "reference_id", referenceId,
								"param", Map.of("project", "dataentry", "id", dataFullApp.getId()), "body", convertService.toAppDisplay(dataFullApp)));

				Report report = new Report();
				report.setQuickLeadId(dataFullApp.getQuickLeadId());
				report.setApplicationId(data.getApplicationId());
				report.setFunction("FICO_RETURN_COMMENT");
				report.setStatus("PROCESSING");
				report.setCommentDescription(comment);
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());

				report.setPartnerId(partnerId);
				report.setPartnerName(partnerName);

				mongoTemplate.save(report);
//				} else {
//					responseModel.setRequest_id(requestId);
//					responseModel.setReference_id(referenceId);
//					responseModel.setDate_time(new Timestamp(new Date().getTime()));
//					responseModel.setResult_code("1");
//					responseModel.setMessage("Không thể trả thêm comment!");
//					return Map.of("status", 200, "data", responseModel);
//				}
			}


			if (responseCommnentFullAPPFromDigiTex) {
				Report report = new Report();
				report.setQuickLeadId(quickLeadId);
				report.setApplicationId(data.getApplicationId());
				report.setFunction("DIGITEXX_RETURN_COMMENT");
				report.setStatus("FULL_APP_FAIL");
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());

				report.setPartnerId(partnerId);
				report.setPartnerName(partnerName);

				mongoTemplate.save(report);
			}
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("0");
		} catch (Exception e) {
			log.info("ReferenceId : " + referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage("Others error");
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
				try{
					if (checkExist.get(0).getStatus().equals("COMPLETED")){
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(referenceId);
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("applicationId is completed!");

						return Map.of("status", 200, "data", responseModel);
					}else if (checkExist.get(0).getStatus().equals("CANCEL")){
						if (data.getStatus().toUpperCase().equals("CANCEL")){
							responseModel.setRequest_id(requestId);
							responseModel.setReference_id(referenceId);
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code("1");
							responseModel.setMessage("applicationId is cancel!");

							return Map.of("status", 200, "data", responseModel);
						}
					}else if (checkExist.get(0).getStatus().equals("MANUALLY")){
						if (data.getStatus().toUpperCase().equals("MANUALLY")){
							responseModel.setRequest_id(requestId);
							responseModel.setReference_id(referenceId);
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code("1");
							responseModel.setMessage("applicationId is manually!");

							return Map.of("status", 200, "data", responseModel);
						}
					}
//					//event hold
//                    else if(!StringUtils.isEmpty(data.getStatus()) && (data.getStatus().toUpperCase().equals("HOLD")
//							|| data.getStatus().toUpperCase().equals("ACTIVE"))){
//						if(data.getStatus().toUpperCase().equals("HOLD") && checkExist.get(0).isHolding()){
//							responseModel.setRequest_id(requestId);
//							responseModel.setReference_id(referenceId);
//							responseModel.setDate_time(new Timestamp(new Date().getTime()));
//							responseModel.setResult_code("1");
//							responseModel.setMessage("Application is hold");
//							return Map.of("status", 200, "data", responseModel);
//
//						}else if(data.getStatus().toUpperCase().equals("ACTIVE") && !checkExist.get(0).isHolding()){
//							responseModel.setRequest_id(requestId);
//							responseModel.setReference_id(referenceId);
//							responseModel.setDate_time(new Timestamp(new Date().getTime()));
//							responseModel.setResult_code("1");
//							responseModel.setMessage("Application is active");
//							return Map.of("status", 200, "data", responseModel);
//						}
//
//						return holdApp(checkExist.get(0), request, token);
//
//					}
//					//check hold
//                    else if(checkExist.get(0).isHolding()){
//						if(!request.get("body").path("data").hasNonNull("isFeedBack")){
//							this.responseToPartner(checkExist.get(0));
//						}
//						responseModel.setRequest_id(requestId);
//						responseModel.setReference_id(referenceId);
//						responseModel.setDate_time(new Timestamp(new Date().getTime()));
//						responseModel.setResult_code("1");
//						responseModel.setMessage("Application is hold");
//
//						return Map.of("status", 200, "data", responseModel);
//					}
				}
				catch (Exception ex){}

				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));

				Update update = new Update();
				update.set("status", data.getStatus().toUpperCase());
				update.set("description", data.getDescription());
				update.set("lastModifiedDate", new Date());
				if (data.getStatus().toUpperCase().equals("MANUALLY".toUpperCase())){
					update.set("userName_DE", token.path("user_name").textValue());
				}
				if (data.getStatus().toUpperCase().equals("CANCEL".toUpperCase())){
					update.set("reasonCancel", data.getDescription());
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
				report.setStatus(data.getStatus().toUpperCase());
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());

				if(dataFullApp != null){
					report.setPartnerId(dataFullApp.getPartnerId());
					report.setPartnerName(dataFullApp.getPartnerName());
				}

				report.setDescription(data.getDescription());

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
			responseModel.setMessage("Others error");
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
					queryGetApp.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()).and("applicationId").not().ne(null).and("status").is("AUTO_QL_FAIL"));
					List<Application> appData = mongoTemplate.find(queryGetApp, Application.class);

					Update update = new Update();
					update.set("status", "NEW");
					update.set("lastModifiedDate", new Date());
					Application resultUpdate = mongoTemplate.findAndModify(queryGetApp, update, Application.class);

					if (resultUpdate == null){
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("applicationId is exist!");

						return Map.of("status", 200, "data", responseModel);
					}

					Query queryGetApp2 = new Query();
					queryGetApp2.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
					List<Application> appDataFull = mongoTemplate.find(queryGetApp2, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", appDataFull.get(0).getId()), "body", convertService.toAppDisplay(appDataFull.get(0))));

					if(apiService.chooseAutoOrApiF1() == 1) {
						{
							new Thread(() -> {
								try{
									ObjectNode result = mapper.createObjectNode();
									result.put("request_id", request.path("body").path("request_id").textValue());
									result.put("quickLeadId", appData.get(0).getQuickLeadId());

									JsonNode response = apiService.callApiF1(urlCreateLead, convertService.toApiF1(appData.get(0)));
									if(response.hasNonNull("errMsg")){
										response = apiService.callApiF1(urlCreateLead, response.path("appConvert"));
									}
									result.set("response", response);
									updateAfterQuickLeadF1(result);
									return;
								}catch(Exception e){
									log.info("quickLead.thread.apiService.callApiF1.Exception {}", e.toString());
								}
							}).start();
						}
					}else{
						if (appData.get(0).getPartnerId().equals("3")){
							rabbitMQService.send(queueAutoSGB, Map.of("func", "quickLeadAppAssignPool", "body", appData.get(0)));
						}else {
							rabbitMQService.send(queueAutoSGB, Map.of("func", "quickLeadApp", "body", appData.get(0)));
						}
					}
				}else {
					if(!request.path("body").path("data").path("partnerId").isTextual()){
						return Map.of("status", 200, "data", "partnerId not found");
					}
					Query queryUpdate = new Query();
					queryUpdate.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()).and("applicationId").not().ne(null).and("status").is("UPLOADFILE"));

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
					update.set("lastModifiedDate", new Date());

//					update.set("partnerId", data.getPartnerId());
					Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
					if (resultUpdate == null){
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("applicationId is exist!");

						return Map.of("status", 200, "data", responseModel);
					}

					Report report = new Report();
					report.setQuickLeadId(data.getQuickLeadId());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("QUICKLEAD");
					report.setStatus("NEW");
					report.setCreatedBy(token.path("user_name").textValue());
					report.setCreatedDate(new Date());

					report.setPartnerId(resultUpdate.getPartnerId());
					report.setPartnerName(resultUpdate.getPartnerName());

					mongoTemplate.save(report);

//				--automation QuickLead--
					Query queryGetApp = new Query();
					queryGetApp.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
					List<Application> appData = mongoTemplate.find(queryGetApp, Application.class);

					rabbitMQService.send("tpf-service-app",
							Map.of("func", "createApp", "reference_id", referenceId,"body", convertService.toAppDisplay(appData.get(0))));

					if(apiService.chooseAutoOrApiF1() == 1) {
						new Thread(() -> {
							try{
								ObjectNode result = mapper.createObjectNode();
								result.put("request_id", request.path("body").path("request_id").textValue());
								result.put("quickLeadId", appData.get(0).getQuickLeadId());

								JsonNode response = apiService.callApiF1(urlCreateLead, convertService.toApiF1(appData.get(0)));
								if(response.hasNonNull("errMsg")){
									response = apiService.callApiF1(urlCreateLead, response.path("appConvert"));
								}
								result.set("response", response);
								updateAfterQuickLeadF1(result);
							}catch(Exception e){
								log.info("quickLead.thread.apiService.callApiF1.Exception {}", e.toString());
							}
						}).start();
					}else{
						if (appData.get(0).getPartnerId().equals("3")){
							rabbitMQService.send(queueAutoSGB, Map.of("func", "quickLeadAppAssignPool", "body", appData.get(0)));
						}else {
							rabbitMQService.send(queueAutoSGB, Map.of("func", "quickLeadApp", "body", appData.get(0)));
						}
					}
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
			responseModel.setMessage(e.toString());
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
				app.setStatus("UPLOADFILE");
				app.setUserName(token.path("user_name").textValue());
				app.setCreatedDate(new Date());

				app.setPartnerId(request.get("partnerId").asText());
				app.setPartnerName(request.get("partnerName").asText());

				if (request.hasNonNull("routingId")) {
					app.setRoutingId(request.get("routingId").asLong(-1));
				}

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

					description = "upload Success";
				}else{
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("2");
					responseModel.setData(responseUI);
					responseModel.setMessage("uploadFile Partner fail!");

					description = "uploadFile Partner fail!";
				}
				Report report = new Report();
				report.setQuickLeadId(quickLeadId.toString());
				report.setFunction("UPLOADFILE");
				report.setStatus("UPLOADFILE");
				report.setDescription(description);
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());

				report.setPartnerId(request.get("partnerId").asText());
				report.setPartnerName(request.get("partnerName").asText());

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
							update.set("quickLead.documentsComment.$.contentType", item.getContentType());
							Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
						}
					}

//					if (checkExist.get(0).getApplicationInformation() != null){
//						dataUpload = mapper.readValue(request.path("body").toString(), new TypeReference<List<QLDocument>>() {
//						});
//						for (QLDocument item : dataUpload) {
//							Query queryUpdate = new Query();
//							queryUpdate.addCriteria(Criteria.where("applicationId").is(request.get("appId").asText()).and("quickLead.documentsAfterSubmit.originalname").is(item.getOriginalname()));
//							List<Application> checkCommentExist = mongoTemplate.find(queryUpdate, Application.class);
//
//							if (checkCommentExist.size() <= 0) {
//								Query queryAddComment = new Query();
//								queryAddComment.addCriteria(Criteria.where("applicationId").is(request.get("appId").asText()));
//
//								Update update = new Update();
//								update.addToSet("quickLead.documentsAfterSubmit", item);
//								Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, update, FindAndModifyOptions.options().upsert(true), Application.class);
//							} else {
//								Update update = new Update();
//								update.set("quickLead.documentsAfterSubmit.$.filename", item.getFilename());
//								update.set("quickLead.documentsAfterSubmit.$.urlid", item.getUrlid());
//								update.set("quickLead.documentsAfterSubmit.$.md5", item.getMd5());
//								update.set("quickLead.documentsAfterSubmit.$.contentType", item.getContentType());
//								Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
//							}
//						}
//					}else {
//						dataUpload = mapper.readValue(request.path("body").toString(), new TypeReference<List<QLDocument>>() {
//						});
//						for (QLDocument item : dataUpload) {
//							Query queryUpdate = new Query();
//							queryUpdate.addCriteria(Criteria.where("applicationId").is(request.get("appId").asText()).and("quickLead.documentsComment.originalname").is(item.getOriginalname()));
//							List<Application> checkCommentExist = mongoTemplate.find(queryUpdate, Application.class);
//
//							if (checkCommentExist.size() <= 0) {
//								Query queryAddComment = new Query();
//								queryAddComment.addCriteria(Criteria.where("applicationId").is(request.get("appId").asText()));
//
//								Update update = new Update();
//								update.addToSet("quickLead.documentsComment", item);
//								Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, update, FindAndModifyOptions.options().upsert(true), Application.class);
//							} else {
//								Update update = new Update();
//								update.set("quickLead.documentsComment.$.filename", item.getFilename());
//								update.set("quickLead.documentsComment.$.urlid", item.getUrlid());
//								update.set("quickLead.documentsComment.$.md5", item.getMd5());
//								update.set("quickLead.documentsComment.$.contentType", item.getContentType());
//								Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
//							}
//						}
//					}

					Map<String, Object> responseUI = new HashMap<>();
					responseUI.put("quickLeadId", quickLeadId);
					responseUI.put("applicationId", request.get("appId").asText());
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
						responseModel.setMessage("uploadFile Partner fail!");
					}
				}else{
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("1");
					responseModel.setMessage("AppId not exist.");
				}

//				Report report = new Report();
//				report.setQuickLeadId(quickLeadId.toString());
//				report.setApplicationId(request.get("appId").asText());
//				report.setFunction("UPLOADFILE_COMMENT");
//				report.setStatus("RETURNED");
//				report.setDescription(description);
//				report.setCreatedBy(token.path("user_name").textValue());
//				report.setCreatedDate(new Date());
//				mongoTemplate.save(report);
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("3");
			responseModel.setMessage(e.toString());
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
			responseModel.setMessage("Others error");
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
				try{
					if (checkExist.get(0).getApplicationId() != null){
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("applicationId is exist!");
						return Map.of("status", 200, "data", responseModel);
					}
				}
				catch (Exception e) {}
				String partnerId = "";
				String partnerName = "";
				partnerId = checkExist.get(0).getPartnerId();
				partnerName = checkExist.get(0).getPartnerName();
				if(StringUtils.isEmpty(partnerId)){
					return Map.of("status", 200, "data", "partnerId null");

				}
				if (request.path("body").path("applicationId").textValue() != null && request.path("body").path("applicationId").equals("") != true &&
						request.path("body").path("applicationId").textValue().equals("") != true && request.path("body").path("applicationId").textValue().equals("UNKNOWN") != true &&
						request.path("body").path("applicationId").textValue().equals("UNKNOW") != true) {

					Query queryAppId = new Query();
					queryAppId.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").textValue()));
					List<Application> checkExistAppId = mongoTemplate.find(queryAppId, Application.class);
					if (checkExistAppId.size() > 0){
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("applicationId duplicate.");

						return Map.of("status", 200, "data", responseModel);
					}

					Update update = new Update();
					update.set("applicationId", request.path("body").path("applicationId").textValue());
					update.set("status", "PROCESSING");
					update.set("lastModifiedDate", new Date());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					if (!resultUpdatetest.getPartnerId().equals("3")) {
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

						Map partner = this.getPartner(partnerId);
						if (StringUtils.isEmpty(partner.get("data"))) {
							return Map.of("result_code", 3, "message", "Not found partner");
						}
						Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
						String cmInfoApi = (String) mapper.convertValue(url, Map.class).get("cmInfoApi");

						if (partnerId.equals("1")) {
							String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
							apiService.callApiPartner(cmInfoApi, dataSend, tokenPartner, partnerId);
//							apiService.callApiDigitexx(urlDigitexCmInfoApi, dataSend);
						} else if (partnerId.equals("2")) {
							String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
							Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
							String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
							if (StringUtils.isEmpty(tokenPartner)) {
								return Map.of("result_code", 3, "message", "Not get token saigon-bpo");
							}
							apiService.callApiPartner(cmInfoApi, dataSend, tokenPartner, partnerId);
						}
					}
					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					if (StringUtils.hasLength(dataFullApp.getCreateFrom()) && !"WEB".equals(dataFullApp.getCreateFrom().toUpperCase())){
						String automationAcc = request.path("body").path("automationAcc").asText("");
						rabbitMQService.send("tpf-service-esb",
								Map.of("func", "updateAutomation", "reference_id", referenceId, "body", Map.of("app_id", dataFullApp.getApplicationId() != null ? dataFullApp.getApplicationId() : "",
										"project", dataFullApp.getCreateFrom(),
										"automation_result", "QUICKLEAD_PASS",
										"description", "Thanh cong",
										"transaction_id", dataFullApp.getQuickLeadId(),
										"automation_account", automationAcc)));
					}else{
						rabbitMQService.send("tpf-service-app",
								Map.of("func", "updateApp","reference_id", referenceId,
										"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));
					}

					Report report = new Report();
					report.setQuickLeadId(request.path("body").path("quickLeadId").textValue());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("QUICKLEAD");
					report.setStatus("PROCESSING");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());

					report.setPartnerId(partnerId);
					report.setPartnerName(partnerName);

					mongoTemplate.save(report);
				}
				else{

					Report report = new Report();
					report.setQuickLeadId(request.path("body").path("quickLeadId").textValue());
					report.setFunction("QUICKLEAD");
					report.setStatus("AUTO_QL_FAIL");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());

					report.setPartnerId(partnerId);
					report.setPartnerName(partnerName);

					mongoTemplate.save(report);

					Update update = new Update();
//					update.set("applicationId", "UNKNOWN");
					update.set("status", "AUTO_QL_FAIL");
					update.set("lastModifiedDate", new Date());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					if (StringUtils.hasLength(dataFullApp.getCreateFrom()) && !"WEB".equals(dataFullApp.getCreateFrom().toUpperCase())){
						String automationAcc = request.path("body").path("automationAcc").asText("");
						rabbitMQService.send("tpf-service-esb",
								Map.of("func", "updateAutomation", "reference_id", referenceId, "body", Map.of("app_id", dataFullApp.getApplicationId() != null ? dataFullApp.getApplicationId() : "",
										"project", dataFullApp.getCreateFrom(),
										"automation_result", "QUICKLEAD_FAILED",
										"description", "Khong thanh cong",
										"transaction_id", dataFullApp.getQuickLeadId(),
										"automation_account", automationAcc)));
					}else{
						rabbitMQService.send("tpf-service-app",
								Map.of("func", "updateApp","reference_id", referenceId,
										"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));
					}
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
			responseModel.setMessage(e.toString());
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
		final String requestDes;
		final String appId;
		try{
			applicationId = request.path("body").path("applicationId").textValue();
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").textValue()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				String partnerId = "";
				String partnerName = "";
				partnerId = checkExist.get(0).getPartnerId();
				partnerName = checkExist.get(0).getPartnerName();

				if(StringUtils.isEmpty(partnerId)){
					return Map.of("status", 200, "data", "partnerId null");
				}
				if (request.path("body").path("status").textValue().toUpperCase().equals("OK")) {
					Update update = new Update();
					update.set("status", "COMPLETED");
					update.set("description", request.path("body").path("description").textValue());
					update.set("lastModifiedDate", new Date());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					Report report = new Report();
					report.setQuickLeadId(resultUpdatetest.getQuickLeadId());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("SENDFULLAPP");
					report.setStatus("COMPLETED");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());

					report.setPartnerId(partnerId);
					report.setPartnerName(partnerName);

					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), JsonNode.class);

					Map partner = this.getPartner(partnerId);
					if(StringUtils.isEmpty(partner.get("data"))){
						return Map.of("result_code", 3, "message","Not found partner");
					}
					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					String feedbackApi = (String) (mapper.convertValue(url, Map.class).get("feedbackApi"));

					if(partnerId.equals("1")){
						String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
//						apiService.callApiDigitexx(urlDigitexFeedbackApi, dataSend);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return Map.of("result_code", 3, "message","Not get token saigon-bpo");
						}
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
					}
				}else{
					errors = request.path("body").path("stage").textValue();

					try {
						if (request.path("body").path("description").textValue() != null) {
							if (request.path("body").path("description").textValue().contains("move_to_next_stage")) {
								Query queryLogin = new Query();
								queryLogin.addCriteria(Criteria.where("applicationId").is(applicationId));
								Application dataFullApp = mongoTemplate.findOne(queryLogin, Application.class);
								if (dataFullApp.getQuickLead().getDocumentsComment() != null){
									dataFullApp.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
								}
								dataFullApp.setStage("END OF LEAD DETAIL");
								rabbitMQService.send(queueAutoSGB,
										Map.of("func", "updateAppError", "body", dataFullApp));

								responseModel.setRequest_id(requestId);
								responseModel.setReference_id(UUID.randomUUID().toString());
								responseModel.setDate_time(new Timestamp(new Date().getTime()));
								responseModel.setResult_code("1");
								responseModel.setMessage("move_to_next_stage");
								return Map.of("status", 200, "data", responseModel);
							}
						}
					}
					catch (Exception e) {
						log.info("ReferenceId : "+ referenceId + "Error move_to_next_stage: " + e);
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage(e.toString());
					}

					if (errors.equals("LOGIN FINONE")){
						try {
							Query queryLogin = new Query();
							queryLogin.addCriteria(Criteria.where("applicationId").is(applicationId));
							Application dataFullApp = mongoTemplate.findOne(queryLogin, Application.class);

							if (dataFullApp.getQuickLead().getDocumentsComment() != null){
								dataFullApp.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
							}
							rabbitMQService.send(queueAutoSGB,
									Map.of("func", "fullInfoApp", "body", dataFullApp));

							responseModel.setRequest_id(requestId);
							responseModel.setReference_id(UUID.randomUUID().toString());
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code("1");
							responseModel.setMessage("LOGIN FINONE");
							return Map.of("status", 200, "data", responseModel);
						}
						catch (Exception e) {
							log.info("ReferenceId : "+ referenceId + "Error LOGIN FINONE: " + e);
						}
					}

					if (errors.toUpperCase().equals("END OF LEAD DETAIL") ||
							errors.toUpperCase().equals("PERSONAL INFORMATION") ||
							errors.toUpperCase().equals("EMPLOYMENT DETAILS")){
					}else{
						errors = "OTHER";
					}

					Update update = new Update();
					update.set("status", "FULL_APP_FAIL");
					update.set("description", errors);
					update.set("stage", errors);
					update.set("error", request.path("body").path("error").textValue());
					update.set("lastModifiedDate", new Date());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					//save comment
					CommentModel commentModel = new CommentModel();
					commentModel.setCommentId(commentId);
					commentModel.setType("FICO");
					commentModel.setCode("FICO_ERR");
					commentModel.setStage(errors);
					commentModel.setDescription(request.path("body").path("description").textValue());
					commentModel.setRequest(errors);

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
					report.setPartnerId(partnerId);
					report.setPartnerName(partnerName);

					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					//fico gui comment

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "failed",
							"commend-id", commentId, "errors", errors)), JsonNode.class);

					Map partner = this.getPartner(partnerId);
					if(StringUtils.isEmpty(partner.get("data"))){
						return Map.of("result_code", 3, "message","Not found partner");
					}
					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					String feedbackApi = (String) (mapper.convertValue(url, Map.class).get("feedbackApi"));

					if(partnerId.equals("1")){
						String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
//						apiService.callApiDigitexx(urlDigitexFeedbackApi, dataSend);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return Map.of("result_code", 3, "message","Not get token saigon-bpo");
						}
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
					}
					try{
						appId = applicationId;
						requestDes = errors;
						new Thread(() -> {
							try {
								String resultInsertORA = insertToOracle_Return(appId, commentId,"FICO", requestDes, null, null, new Date(), null);
								if (!resultInsertORA.equals("success")) {
									log.info("ReferenceId : " + referenceId + "; Insert to Oracle Return: " + resultInsertORA);
								}
							}
							catch (Exception e) {}
						}).start();
					}
					catch (Exception e) {}
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
		final String requestDes;
		final String appId;
		try{
			applicationId = request.path("body").path("applicationId").textValue();
			errors = request.path("body").path("stage").textValue();
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").textValue()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				String partnerId = "";
				String partnerName = "";
				partnerId = checkExist.get(0).getPartnerId();
				partnerName = checkExist.get(0).getPartnerName();
				if(StringUtils.isEmpty(partnerId)){
					return Map.of("status", 200, "data", "partnerId null");
				}
				if (request.path("body").path("status").textValue().toUpperCase().equals("OK")) {
					Update update = new Update();
					update.set("status", "COMPLETED");
					update.set("description", request.path("body").path("description").textValue());
					update.set("lastModifiedDate", new Date());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					Report report = new Report();
					report.setQuickLeadId(resultUpdatetest.getQuickLeadId());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("UPDATEFULLAPP");
					report.setStatus("COMPLETED");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());

					report.setPartnerId(partnerId);
					report.setPartnerName(partnerName);

					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), JsonNode.class);

					Map partner = this.getPartner(partnerId);
					if(StringUtils.isEmpty(partner.get("data"))){
						return Map.of("result_code", 3, "message","Not found partner");
					}
					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					String feedbackApi = (String) mapper.convertValue(url, Map.class).get("feedbackApi");

					if(partnerId.equals("1")){
						String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
//						apiService.callApiDigitexx(urlDigitexFeedbackApi, dataSend);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return Map.of("result_code", 3, "message","Not get token saigon-bpo");
						}
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
					}

				}else{
					errors = request.path("body").path("stage").textValue();

					try {
						if (request.path("body").path("description").textValue() != null) {
							if (request.path("body").path("description").textValue().contains("move_to_next_stage")) {
								boolean checkFullApp = false;
								Query queryLogin = new Query();
								queryLogin.addCriteria(Criteria.where("applicationId").is(applicationId));
								Application dataFullApp = mongoTemplate.findOne(queryLogin, Application.class);

								List<CommentModel> dataUpdate = dataFullApp.getComment();
								dataUpdate.sort(Comparator.comparing(CommentModel::getCreatedDate).reversed());

								Application dataUpdateSendAuto = new Application();
								for (CommentModel item : dataUpdate) {
									if (item.getResponse() != null) {
										dataUpdateSendAuto = item.getResponse().getData();
										if (dataFullApp.getQuickLead().getDocumentsComment() != null){
											dataUpdateSendAuto.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
										}
//										if (dataFullApp.getQuickLead().getDocumentsAfterSubmit() != null) {
//											dataUpdateSendAuto.setDocuments(dataFullApp.getQuickLead().getDocumentsAfterSubmit());
//										}else if (dataFullApp.getQuickLead().getDocumentsComment() != null){
//											dataUpdateSendAuto.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
//										}
										dataUpdateSendAuto.setStage("END OF LEAD DETAIL");
										if (dataFullApp.getError() != null) {
											dataUpdateSendAuto.setError(dataFullApp.getError());
										}
										checkFullApp = true;
										break;
									}
								}
								if (!checkFullApp) {
									dataUpdateSendAuto = dataFullApp;
									dataUpdateSendAuto.setStage("END OF LEAD DETAIL");
									dataUpdateSendAuto.setError(" ");
								}
								rabbitMQService.send(queueAutoSGB,
										Map.of("func", "updateAppError", "body", dataUpdateSendAuto));

								responseModel.setRequest_id(requestId);
								responseModel.setReference_id(UUID.randomUUID().toString());
								responseModel.setDate_time(new Timestamp(new Date().getTime()));
								responseModel.setResult_code("1");
								responseModel.setMessage("move_to_next_stage");
								return Map.of("status", 200, "data", responseModel);
							}
						}
					}
					catch (Exception e) {
						log.info("ReferenceId : "+ referenceId + "Error move_to_next_stage: " + e);
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("move_to_next_stage");
					}

					if (errors.equals("LOGIN FINONE")){
						try{
							Query queryLogin = new Query();
							queryLogin.addCriteria(Criteria.where("applicationId").is(applicationId));
							Application dataFullApp = mongoTemplate.findOne(queryLogin, Application.class);

							List<CommentModel> dataUpdate = dataFullApp.getComment();
							dataUpdate.sort(Comparator.comparing(CommentModel::getCreatedDate).reversed());

							Application dataUpdateSendAuto = new Application();
							for (CommentModel item : dataUpdate) {
								if (item.getResponse() != null){
									dataUpdateSendAuto = item.getResponse().getData();
									if (dataFullApp.getQuickLead().getDocumentsComment() != null){
										dataUpdateSendAuto.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
									}
//									if (dataFullApp.getQuickLead().getDocumentsAfterSubmit() != null) {
//										dataUpdateSendAuto.setDocuments(dataFullApp.getQuickLead().getDocumentsAfterSubmit());
//									}else if (dataFullApp.getQuickLead().getDocumentsComment() != null){
//										dataUpdateSendAuto.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
//									}
									dataUpdateSendAuto.setStage(item.getStage());
									if (dataFullApp.getError() != null){
										dataUpdateSendAuto.setError(dataFullApp.getError());
									}
									break;
								}
							}
							rabbitMQService.send(queueAutoSGB,
									Map.of("func", "updateAppError","body", dataUpdateSendAuto));

							responseModel.setRequest_id(requestId);
							responseModel.setReference_id(UUID.randomUUID().toString());
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code("1");
							responseModel.setMessage("LOGIN FINONE");
							return Map.of("status", 200, "data", responseModel);
						}
						catch (Exception e) {
							log.info("ReferenceId : "+ referenceId + "Error LOGIN FINONE: " + e);
						}
					}

					if (errors.toUpperCase().equals("END OF LEAD DETAIL") ||
							errors.toUpperCase().equals("PERSONAL INFORMATION") ||
							errors.toUpperCase().equals("EMPLOYMENT DETAILS")){
					}else{
						errors = "OTHER";
					}

					Update update = new Update();
					update.set("status", "FULL_APP_FAIL");
					update.set("description", errors);
					update.set("stage", errors);
					update.set("error", request.path("body").path("error").textValue());
					update.set("lastModifiedDate", new Date());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

					//save comment
					CommentModel commentModel = new CommentModel();
					commentModel.setCommentId(commentId);
					commentModel.setType("FICO");
					commentModel.setCode("FICO_ERR");
					commentModel.setStage(errors);
					commentModel.setDescription(request.path("body").path("description").textValue());
					commentModel.setRequest(errors);

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

					report.setPartnerId(partnerId);
					report.setPartnerName(partnerName);

					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					//fico gui comment

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "failed",
							"commend-id", commentId, "errors", errors)), JsonNode.class);

					Map partner = this.getPartner(partnerId);
					if(StringUtils.isEmpty(partner.get("data"))){
						return Map.of("result_code", 3, "message","Not found partner");
					}
					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					String feedbackApi = (String) mapper.convertValue(url, Map.class).get("feedbackApi");

					if(partnerId.equals("1")){
						String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
//						apiService.callApiDigitexx(urlDigitexFeedbackApi, dataSend);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return Map.of("result_code", 3, "message","Not get token saigon-bpo");
						}
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
					}

					try{
						appId = applicationId;
						requestDes = errors;
						new Thread(() -> {
							try {
								String resultInsertORA = insertToOracle_Return(appId, commentId, "FICO", requestDes, null, null, new Date(), null);
								if (!resultInsertORA.equals("success")) {
									log.info("ReferenceId : " + referenceId + "; Insert to Oracle Return: " + resultInsertORA);
								}
							}
							catch (Exception e) {}
						}).start();
					}
					catch (Exception e) {}
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
//			Query query = new Query();
//
//			if (request.path("body").path("data").path("fromDate").textValue() != null && !request.path("body").path("data").path("fromDate").textValue().equals("")
//					&& request.path("body").path("data").path("toDate").textValue() != null && !request.path("body").path("data").path("toDate").textValue().equals("")){
//				Timestamp fromDate = Timestamp.valueOf(request.path("body").path("data").path("fromDate").textValue() + " 00:00:00");
//				Timestamp toDate = Timestamp.valueOf(request.path("body").path("data").path("toDate").textValue() + " 23:23:59");
//
//				query.addCriteria(Criteria.where("createdDate").gte(fromDate).lte(toDate));
//			}
//
//            List<Report> listData = mongoTemplate.find(query, Report.class);

			LookupOperation lookupOperation = LookupOperation.newLookup()
					.from("dataentry")
					.localField("applicationId")
					.foreignField("applicationId")
					.as("applications");

//			AggregationOperation replaceRoot = Aggregation.replaceRoot().withValueOf(ArrayOperators.ArrayElemAt.arrayOf("$dataentry_report").elementAt(0), );
//			AggregationOperation replaceRoot = Aggregation.replaceRoot().withValueOf(ObjectOperators.valueOf("$dataentry_report").mergeWith(Aggregation.ROOT));



			AggregationOperation match1;
			if (request.path("body").path("data").path("fromDate").textValue() != null && !request.path("body").path("data").path("fromDate").textValue().equals("")
					&& request.path("body").path("data").path("toDate").textValue() != null && !request.path("body").path("data").path("toDate").textValue().equals("")){
				Timestamp fromDate = Timestamp.valueOf(request.path("body").path("data").path("fromDate").textValue() + " 00:00:00");
				Timestamp toDate = Timestamp.valueOf(request.path("body").path("data").path("toDate").textValue() + " 23:23:59");

				match1 = Aggregation.match(Criteria.where("createdDate").gte(fromDate).lte(toDate).and("applicationId").ne(null));
			}else{
				match1 = Aggregation.match(Criteria.where("applicationId").ne(null));
			}

//			AggregationOperation match1 = Aggregation.match(Criteria.where("createdDate").gte(fromDate).lte(toDate).and("status").in(inputQuery));
//			AggregationOperation group = Aggregation.group("applicationId", "createdDate", "status", "quickLeadId", "description", "function", "createdBy", "commentDescription"
//            );


			AggregationOperation group = Aggregation.group(Fields.from( Fields.field("applicationId", "applicationId")).and("createdDate","createdDate")
							.and("status","status").and("quickLeadId","quickLeadId").and("description","description")
							.and("function","function").and("createdBy","createdBy")
							.and("commentDescription","commentDescription")
							.and("branch","applications.quickLead.sourcingBranch")
							.and("firstName","applications.quickLead.firstName")
							.and("lastName","applications.quickLead.lastName")
							.and("fullName","applications.applicationInformation.personalInformation.personalInfo.fullName")
							.and("identificationNumber","applications.quickLead.identificationNumber")
							.and("partnerName","partnerName")
//                            .and("isHolding","applications.isHolding")
							.and("description","description")
//                    .and("identificationNumberFull","applications.applicationInformation.personalInformation.identifications.identificationNumber")
			);

			AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "applicationId", "createdDate");
//			AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "applicationId").and(Sort.Direction.DESC, "createdDate");
			AggregationOperation project = Aggregation.project().andExpression("_id.applicationId").as("applicationId").andExpression("_id.createdDate").as("createdDate")
					.andExpression("_id.commentDescription").as("commentDescription") .andExpression("_id.applications.quickLead.sourcingBranch").as("branch");
			//AggregationOperation limit = Aggregation.limit(Constants.BOARD_TOP_LIMIT);
			Aggregation aggregation = Aggregation.newAggregation(match1, lookupOperation, group, sort /*, project/*, limit*/)
					.withOptions(newAggregationOptions().allowDiskUse(true).build());

			AggregationResults<Report> results = mongoTemplate.aggregate(aggregation, Report.class, Report.class);

			List<Report> listData = results.getMappedResults();


			String appId = "";
			Date startDate = new Date();
			Date finishDate = new Date();
			for (Report item:listData) {
				try {
					try{

//                        if (item.getApplications().size() > 0) {
//                            if (item.getApplications().get(0).getApplicationInformation() == null) {
//                                item.setFullName(item.getApplications().get(0).getQuickLead().getFirstName() + " " + item.getApplications().get(0).getQuickLead().getLastName());
//                                item.setIdentificationNumber(item.getApplications().get(0).getQuickLead().getIdentificationNumber());
//                            } else {
//                                if (item.getApplications().get(0).getApplicationInformation().getPersonalInformation() != null) {
//                                    item.setFullName(item.getApplications().get(0).getApplicationInformation().getPersonalInformation().getPersonalInfo().getFullName());
//                                    item.setIdentificationNumber(item.getApplications().get(0).getApplicationInformation().getPersonalInformation().getIdentifications().get(0).getIdentificationNumber());
//                                }
//                            }
//                        }


						if (item.getFullName() == null || item.getFullName().equals("")){
							item.setFullName(item.getFirstName() + " " + item.getLastName());
						}
						if (item.getIdentificationNumberFull() == null || item.getIdentificationNumberFull().equals("")){
						}else{
							item.setIdentificationNumber(item.getIdentificationNumberFull());
						}
					}
					catch (Exception exx) {
						String a ="";
					}

					if (item.getFunction().equals("QUICKLEAD") && item.getStatus().equals("PROCESSING")) {
						startDate = item.getCreatedDate();
						appId = item.getApplicationId();
					} else {
						if (item.getApplicationId().equals(appId) && !item.getStatus().equals("COMPLETED")) {
							finishDate = item.getCreatedDate();
						} else {
							item.setDuration(Duration.between(startDate.toInstant(), finishDate.toInstant()).toMinutes());
						}
					}

					if (item.getPartnerName() == null || item.getPartnerName().equals("")){
						item.setPartnerName("DIGI-TEXX");
					}

//                    if (item.getIsHolding().equals("true")){
//                        item.setHold("YES");
//                    }
				}
				catch (Exception ex) {
				}
			}

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
			responseModel.setMessage(e.toString());

			return Map.of("status", 200, "data", responseModel);
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
			inputQuery.add("FULL_APP_FAIL");
			inputQuery.add("MANUALLY");
			inputQuery.add("PROCESSING");
			inputQuery.add("CANCEL");

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
			Aggregation aggregation = Aggregation.newAggregation(match1, group, sort, project/*, limit*/)
					.withOptions(newAggregationOptions().allowDiskUse(true).build());
			AggregationResults<ReportStatus> results = mongoTemplate.aggregate(aggregation, Application.class, ReportStatus.class);

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
			responseModel.setMessage(e.toString());

			return Map.of("status", 200, "data", responseModel);
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
		String[] COLUMNs = {"Seq","VENDOR", "App no.", "Action", "Create Date", "Create By", "Status", "Comment", "Full Name", "ID", "Branch", "Duration(Minutes)", "Description"};
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
				short dateFormat = createHelper_2.createDataFormat().getFormat("M/d/yyyy HH:MM:SS AM/PM");
				cellStyle.setDataFormat(dateFormat);

				row.createCell(0).setCellValue(rowIdx - 1);
				row.createCell(1).setCellValue(item.getPartnerName());
				row.createCell(2).setCellValue(item.getApplicationId());
				row.createCell(3).setCellValue(item.getFunction());

				Cell cell = row.createCell(4);
				cell.setCellValue(item.getCreatedDate());
				cell.setCellStyle(cellStyle);

//				row.createCell(2).setCellValue(item.getCreatedDate());

				row.createCell(5).setCellValue(item.getCreatedBy());
				row.createCell(6).setCellValue(item.getStatus());
				row.createCell(7).setCellValue(item.getCommentDescription());
				row.createCell(8).setCellValue(item.getFullName());
				row.createCell(9).setCellValue(item.getIdentificationNumber());
				row.createCell(10).setCellValue(item.getBranch());
				row.createCell(11).setCellValue(item.getDuration());
//                row.createCell(12).setCellValue(item.getHold());
				row.createCell(12).setCellValue(item.getDescription());

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
			List<String> inputQuery = new ArrayList<String>();

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

			if(request.path("body").path("data").path("status")!=null && !request.path("body").path("data").path("status").toString().equals("") &&
					!request.path("body").path("data").path("status").isNull())
			{
				inputQuery = mapper.readValue(request.path("body").path("data").path("status").toString(), List.class);
				if (inputQuery.size() > 0) {
					criteria.and("status").in(inputQuery);
				}
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
			if(request.path("body").path("data").get("page")!=null && request.path("body").path("data").get("limit")!=null &&
					!request.path("body").path("data").get("page").isNull() && !request.path("body").path("data").get("limit").isNull()) {
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
				}else {
					obj.setFullName(temp.getQuickLead().getFirstName() +" "+ temp.getQuickLead().getLastName());
					obj.setIdentificationNumber(temp.getQuickLead().getIdentificationNumber());
				}
				obj.setCreatedDate(temp.getCreatedDate());
				obj.setSaleBranch(temp.getQuickLead().getSourcingBranch());
				obj.setCreatedBy(temp.getUserName());
				obj.setUpdateDate(temp.getLastModifiedDate());
				obj.setPartnerName(temp.getPartnerName());
				obj.setDsaCode(temp.getDynamicForm()!=null?temp.getDynamicForm().get(0).getSaleAgentCode():"");
//				obj.setHolding(temp.isHolding());
				return obj;
			}).collect(Collectors.toList());


			if (resultCustome.size() > 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(resultCustome);
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

	public Map<String, Object> getPartner(JsonNode request, JsonNode token) throws Exception{
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getPartner","body", request.path("body")));
		if(response.path("data").isNull()){
			return Map.of("status", 200, "data", "");
		}
		return Map.of("status", 200, "data", response.path("data"));
	}

	private Map<String, Object> getPartner(String id) throws Exception{
		Map partnerMap = new HashMap();
		partnerMap.put("partnerId", id);
		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getPartner","body", partnerMap));
		if(response.path("data").isNull()){
			return Map.of("status", 200, "data", "");
		}
		Map result = Map.of("data", response.path("data").path(0));
		return result;
	}

	public Map<String, Object> uploadPartner(JsonNode request, JsonNode token) throws Exception {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();

		try{
			UUID quickLeadId = UUID.randomUUID();
			List<QLDocument> dataUpload = new ArrayList<>();
			Assert.notNull(request.get("body"), "no body");
			if (request.path("body").path("data").path("applicationId").textValue() == null){
				String partnerId = "";
				Query query = new Query();
				query.addCriteria(Criteria.where("quickLeadId").is(request.path("body").path("data").path("quickLeadId").textValue()));
				List<Application> checkExist = mongoTemplate.find(query, Application.class);
				if(checkExist.size() > 0){
					partnerId = checkExist.get(0).getPartnerId();
				}
				if(StringUtils.isEmpty(partnerId)){
					return Map.of("status", 200, "data", "partnerId null");
				}
				Map partner = this.getPartner(partnerId);
				if(StringUtils.isEmpty(partner.get("data"))){
					return Map.of("result_code", 3, "message","Not found partner");
				}

				JsonNode resultUpload = apiService.retryUploadPartner(request, partner);
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
					responseModel.setMessage("uploadFile Partner fail!");
				}
			}else{
				String partnerId = "";
				Query query = new Query();
				query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("data").path("applicationId").textValue()));
				List<Application> checkExist = mongoTemplate.find(query, Application.class);
				if(checkExist.size() > 0){
					partnerId = checkExist.get(0).getPartnerId();
				}
				if(StringUtils.isEmpty(partnerId)){
					return Map.of("status", 200, "data", "partnerId null");
				}
				Map partner = getPartner(partnerId);
				if(StringUtils.isEmpty(partner.get("data"))){
					return Map.of("result_code", 3, "message","Not found partner");
				}
				JsonNode resultUpload = apiService.retryUploadPartner(request, partner);
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
					responseModel.setMessage("uploadFile Partner fail!");
				}
			}
		}
		catch (Exception e) {
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("3");
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);

	}

	public Map<String, Object> getTokenSaigonBpo(JsonNode request, JsonNode token) throws Exception{
		if(!request.path("body").path("partnerId").isTextual()){
			return Map.of("status", 200, "data", "partnerId not found");
		}
		String partnerId = request.path("body").path("partnerId").asText();
		Map partner = this.getPartner(partnerId);
		if(StringUtils.isEmpty(partner.get("data"))){
			return Map.of("status", 200, "data", "Not found partner");
		}
		Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
		String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
		Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
		String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);

		return Map.of("status", 200, "data", tokenPartner);
	}

	public String insertToOracle_App(Application application) {
		try {
			String IDCard = "";
			String currentAddressCity = "";
			String insurance = "N";
			final String IDCardLD;
			final String currentAddressCityLD;
			final String insuranceLD;
			for (Identification item: application.getApplicationInformation().getPersonalInformation().getIdentifications()) {
				if (item.getIdentificationType().equals("Current National ID")){
					IDCard = item.getIdentificationNumber();
					currentAddressCity = item.getPlaceOfIssue();
				}
				if (application.getLoanDetails().getVapDetails() != null){
					insurance = "Y";
				}
			}
			IDCardLD = IDCard;
			currentAddressCityLD = currentAddressCity;
			insuranceLD = insurance;

			List prmtrsList = new ArrayList();
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.DATE));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.CHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.TIMESTAMP));
//			prmtrsList.add(new SqlOutParameter("result", Types.VARCHAR));

			Map<String, Object> resultData = jdbcTemplate.call(connection -> {
				CallableStatement callableStatement = connection.prepareCall("{call ETL_MGO_APPLICATION(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
				callableStatement.setString(1, application.getApplicationId());
				callableStatement.setDate(2, application.getCreatedDate() != null ? new java.sql.Date(application.getCreatedDate().getTime()) : null);
				callableStatement.setString(3, application.getDynamicForm().get(0).getSaleAgentCode());
				callableStatement.setString(4, application.getQuickLead().getSourcingBranch());
				callableStatement.setString(5, application.getQuickLead().getSourcingBranch());
				callableStatement.setString(6, application.getLoanDetails().getSourcingDetails().getLoanPurposeDesc());
				callableStatement.setString(7, application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getFullName());
				callableStatement.setString(8, IDCardLD);
				callableStatement.setString(9, currentAddressCityLD);
				callableStatement.setString(10, insuranceLD);
				callableStatement.setString(11, application.getQuickLeadId());
				callableStatement.setString(12, application.getPartnerName());
				callableStatement.setDate(13, application.getLastModifiedDate() != null ? new java.sql.Date(application.getLastModifiedDate().getTime()) : null);
//				callableStatement.registerOutParameter(3, Types.VARCHAR);
				return callableStatement;
			}, prmtrsList);

			return "success";
		}catch (Exception e) {
			return e.toString();
		}
	}

	public String insertToOracle_Return(String applicationId, String documentId, String commentType, String request, String reponse, String userName, Date createDate, Date updateDate){
		try {
			List prmtrsList = new ArrayList();
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.VARCHAR));
			prmtrsList.add(new SqlParameter(Types.TIMESTAMP));
			prmtrsList.add(new SqlParameter(Types.TIMESTAMP));

			Map<String, Object> resultData = jdbcTemplate.call(connection -> {
				CallableStatement callableStatement = connection.prepareCall("{call ETL_MGO_APPLICATION_RETURN(?, ?, ?, ?, ?, ?, ?, ?)}");
				callableStatement.setString(1, applicationId);
				callableStatement.setString(2, documentId);
				callableStatement.setString(3, commentType);
				callableStatement.setString(4, request != null ? request : "");
				callableStatement.setString(5, reponse != null ? reponse : "");
				callableStatement.setString(6, userName != null ? userName : "");
				callableStatement.setDate(7, createDate != null ? new java.sql.Date(createDate.getTime()) : null);
				callableStatement.setDate(8, updateDate != null ? new java.sql.Date(updateDate.getTime()) : null );
				return callableStatement;
			}, prmtrsList);

			return "success";
		}catch (Exception e) {
			return e.toString();
		}
	}

//	private Map<String, Object> holdApp(Application app, JsonNode request, JsonNode token) {
//		log.info("{}",request.path("body").toString());
//		ResponseModel responseModel = new ResponseModel();
//		String requestId = request.path("body").path("request_id").textValue();
//		String referenceId = UUID.randomUUID().toString();
//		try{
//			List<String> list = Arrays.asList("PROCESSING", "RETURNED", "FULL_APP_FAIL");
//			boolean match = list.stream().anyMatch(s -> app.getStatus().contains(s));
//
//			if(!match){
//				return Map.of("status", 200, "data", "status not valid");
//			}
//			String event = request.path("body").path("data").path("status").asText();
//			Update update = new Update();
//			if(event.toUpperCase().equals("HOLD")){
//				update.set("isHolding", true);
//			}else{
//				update.set("isHolding", false);
//			}
//
//			Query query = new Query();
//			query.addCriteria(Criteria.where("applicationId").is(app.getApplicationId()));
//			Application resultUpdate = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Application.class);
//
//			rabbitMQService.send("tpf-service-app",
//					Map.of("func", "updateApp","reference_id", referenceId,
//							"param", Map.of("project", "dataentry", "id", resultUpdate.getId()), "body", convertService.toAppDisplay(resultUpdate)));
//
//			Report report = new Report();
//			report.setQuickLeadId(resultUpdate.getQuickLeadId());
//			report.setApplicationId(resultUpdate.getApplicationId());
//			report.setFunction(event);
//			report.setStatus(resultUpdate.getStatus());
//			report.setCreatedBy(token.path("user_name").textValue());
//			report.setCreatedDate(new Date());
//			if(resultUpdate != null){
//				report.setPartnerId(resultUpdate.getPartnerId());
//				report.setPartnerName(resultUpdate.getPartnerName());
//			}
//			String reason = request.path("body").path("data").path("description").asText("");
//			report.setCommentDescription(reason);
//			mongoTemplate.save(report);
//
//			responseModel.setRequest_id(requestId);
//			responseModel.setReference_id(referenceId);
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code("0");
//
//			if(!request.path("body").path("data").hasNonNull("isFeedBack")){
//				Map partner = this.getPartner(app.getPartnerId());
//				if(StringUtils.isEmpty(partner.get("data"))){
//					return Map.of("result_code", 3, "message","Not found partner");
//				}
//
//				JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", app.getApplicationId(),
//						"status", event, "description", reason)), JsonNode.class);
//				JsonNode responseDG = null;
//				if(app.getPartnerId().equals("1")){
//					responseDG = apiService.callApiDigitexx(urlDigitexFeedbackApi, dataSend);
//				} else if(app.getPartnerId().equals("2")){
//					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
//					String feedbackApi = (String) (mapper.convertValue(url, Map.class).get("feedbackApi"));
//					String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
//					Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
//					String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
//					if(StringUtils.isEmpty(tokenPartner)){
//						return Map.of("result_code", 3, "message","Not get token saigon-bpo");
//					}
//					responseDG = apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, app.getPartnerId());
//				}
//
//
//				if (!responseDG.path("error-code").textValue().equals("")) {
//					if (!responseDG.path("error-code").textValue().equals("null")) {
//						log.info("ReferenceId : " + referenceId);
//						responseModel.setRequest_id(requestId);
//						responseModel.setReference_id(referenceId);
//						responseModel.setDate_time(new Timestamp(new Date().getTime()));
//						responseModel.setResult_code("1");
//						responseModel.setMessage(responseDG.path("error-code").textValue() + responseDG.path("error-description").textValue());
//
//						return Map.of("status", 200, "data", responseModel);
//					}
//				}
//			}
//		}
//		catch (Exception e) {
//			log.info("ReferenceId : "+ referenceId + "Error: " + e);
//			responseModel.setRequest_id(requestId);
//			responseModel.setReference_id(referenceId);
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code("1");
//			responseModel.setMessage(e.getMessage());
//		}
//		return Map.of("status", 200, "data", responseModel);
//	}

//	private JsonNode responseToPartner(Application checkExist) {
//		try {
//			String status;
//			if(checkExist.isHolding()){
//				status = "HOLD";
//			}else{
//				status = "ACTIVE";
//			}
//
//			Map partner = this.getPartner(checkExist.getPartnerId());
//			if(StringUtils.isEmpty(partner.get("data"))){
//				JsonNode data = mapper.convertValue(Map.of("result_code", 3, "message","Not found partner"), JsonNode.class);
//				return data;
//			}
//
//			JsonNode responseFromPartner = null;
//			JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", checkExist.getApplicationId(), "status", status)), JsonNode.class);
//
//			if(checkExist.getPartnerId().equals("1")){
//				responseFromPartner = apiService.callApiDigitexx(urlDigitexFeedbackApi, dataSend);
//			} else if(checkExist.getPartnerId().equals("2")){
//				Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
//				String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
//				Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
//				String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
//				if(StringUtils.isEmpty(tokenPartner)){
//					JsonNode data = mapper.convertValue(Map.of("result_code", 3, "message","Not get token saigon-bpo"), JsonNode.class);
//					return data;
//				}
//				String feedbackApi = (String) (mapper.convertValue(url, Map.class).get("feedbackApi"));
//				responseFromPartner = apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, checkExist.getPartnerId());
//			}
//
//			if (!responseFromPartner.path("error-code").textValue().equals("")) {
//				if (!responseFromPartner.path("error-code").textValue().equals("null")) {
//					ResponseModel responseModel = new ResponseModel();
//					responseModel.setDate_time(new Timestamp(new Date().getTime()));
//					responseModel.setResult_code("1");
//					responseModel.setMessage(responseFromPartner.path("error-code").textValue() + responseFromPartner.path("error-description").textValue());
//					JsonNode data = mapper.convertValue(responseModel, JsonNode.class);
//					return data;
//				}
//			}
//
//			ResponseModel responseModel = new ResponseModel();
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code("0");
//			JsonNode data = mapper.convertValue(responseModel, JsonNode.class);
//			return data;
//
//		} catch(Exception e){
//			log.info("{}", e.getMessage());
//		}
//		return null;
//	}

	public Map<String, Object> getAppByQuickLeadId(JsonNode request){
		try{
			Query query = new Query();
			if(request.path("body").path("quickLeadId").isTextual()){
				query.addCriteria(Criteria.where("quickLeadId").is(request.path("body").path("quickLeadId").textValue()));
			}else if(request.path("body").path("data").path("quickLeadId").isTextual()){
				query.addCriteria(Criteria.where("quickLeadId").is(request.path("body").path("data").path("quickLeadId").textValue()));
			}else
				return null;

			List<Application> app = mongoTemplate.find(query, Application.class);

			if(app.size() > 0){
				return Map.of("status", 200, "data", app.get(0));
			}

		}catch(Exception e){
			log.error("getAppByQuickLeadId " + e.getMessage());
		}
		return null;
	}

	private Map<String, Object> updateAfterQuickLeadF1(JsonNode request){
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Query query = new Query();
			String quickLeadId = request.path("quickLeadId").asText("");
			query.addCriteria(Criteria.where("quickLeadId").is(quickLeadId));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() <= 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("quickLeadId not exist.");
				return Map.of("status", 200, "data", responseModel);
			}

			if (checkExist.get(0).getApplicationId() != null){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId is exist!");
				return Map.of("status", 200, "data", responseModel);
			}

			String partnerId = checkExist.get(0).getPartnerId();
			String partnerName = checkExist.get(0).getPartnerName();

			if(StringUtils.isEmpty(partnerId)){
				return Map.of("status", 200, "data", "partnerId null");
			}

			String applicationNumber = request.findPath("applicationNumber").asText("");
			if (StringUtils.isEmpty(applicationNumber)){
				Report report = new Report();
				report.setQuickLeadId(quickLeadId);
				report.setFunction("QUICKLEAD");
				report.setStatus("AUTO_QL_FAIL");
				report.setCreatedBy("AUTOMATION");
				report.setCreatedDate(new Date());

				report.setPartnerId(partnerId);
				report.setPartnerName(partnerName);

				mongoTemplate.save(report);

				Update update = new Update();
				update.set("status", "AUTO_QL_FAIL");
				update.set("lastModifiedDate", new Date());
				Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

				Application dataFullApp = mongoTemplate.findOne(query, Application.class);
				if (StringUtils.hasLength(dataFullApp.getCreateFrom()) && !"WEB".equals(dataFullApp.getCreateFrom().toUpperCase())){
					String automationAcc = request.path("body").path("automationAcc").asText("");
					rabbitMQService.send("tpf-service-esb",
							Map.of("func", "updateAutomation", "reference_id", referenceId, "body", Map.of("app_id", dataFullApp.getApplicationId() != null ? dataFullApp.getApplicationId() : "",
									"project", dataFullApp.getCreateFrom(),
									"automation_result", "QUICKLEAD_FAILED",
									"description", "Khong thanh cong",
									"transaction_id", dataFullApp.getQuickLeadId(),
									"automation_account", automationAcc)));
				}else{
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));
				}
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				return Map.of("status", 200, "data", responseModel);
			}

			applicationNumber = applicationNumber.trim();
			Query queryAppId = new Query();
			queryAppId.addCriteria(Criteria.where("applicationId").is(applicationNumber));
			List<Application> checkExistAppId = mongoTemplate.find(queryAppId, Application.class);
			if (checkExistAppId.size() > 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId duplicate.");

				return Map.of("status", 200, "data", responseModel);
			}

			Update update = new Update();
			update.set("quickLead.customerId", request.path("response").path("responseData").path("customerId").asText(""));
			update.set("applicationId", applicationNumber);
			update.set("status", "PROCESSING");
			update.set("lastModifiedDate", new Date());
			Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

			if (!resultUpdatetest.getPartnerId().equals("3")){
				String customerName = resultUpdatetest.getQuickLead().getLastName() + " " +
						resultUpdatetest.getQuickLead().getFirstName();
				String idCardNo = resultUpdatetest.getQuickLead().getIdentificationNumber();
				String applicationId = applicationNumber;
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

				Map partner = this.getPartner(partnerId);
				if(StringUtils.isEmpty(partner.get("data"))){
					return Map.of("result_code", 3, "message","Not found partner");
				}
				Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
				String cmInfoApi = (String) mapper.convertValue(url, Map.class).get("cmInfoApi");

				if(partnerId.equals("1")){
					String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
					apiService.callApiPartner(cmInfoApi, dataSend, tokenPartner, partnerId);
				} else if(partnerId.equals("2")){
					String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
					Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
					String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
					if(StringUtils.isEmpty(tokenPartner)){
						return Map.of("result_code", 3, "message","Not get token saigon-bpo");
					}
					apiService.callApiPartner(cmInfoApi, dataSend, tokenPartner, partnerId);
				}

			}
			Application dataFullApp = mongoTemplate.findOne(query, Application.class);
			if (StringUtils.hasLength(dataFullApp.getCreateFrom()) && !"WEB".equals(dataFullApp.getCreateFrom().toUpperCase())){
				String automationAcc = request.path("body").path("automationAcc").asText("");
				rabbitMQService.send("tpf-service-esb",
						Map.of("func", "updateAutomation", "reference_id", referenceId, "body", Map.of("app_id", dataFullApp.getApplicationId() != null ? dataFullApp.getApplicationId() : "",
								"project", dataFullApp.getCreateFrom(),
								"automation_result", "QUICKLEAD_PASS",
								"description", "Thanh cong",
								"transaction_id", dataFullApp.getQuickLeadId(),
								"automation_account", automationAcc)));
			}else{
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp","reference_id", referenceId,
								"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));
			}
			Report report = new Report();
			report.setQuickLeadId(quickLeadId);
			report.setApplicationId(applicationNumber);
			report.setFunction("QUICKLEAD");
			report.setStatus("PROCESSING");
			report.setCreatedBy("AUTOMATION");
			report.setCreatedDate(new Date());

			report.setPartnerId(partnerId);
			report.setPartnerName(partnerName);

			mongoTemplate.save(report);

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
			responseModel.setMessage(e.toString());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	private Map<String, Object> updateFullAppApiF1(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("request_id").asText("");

		String referenceId = UUID.randomUUID().toString();
		String commentId = UUID.randomUUID().toString().substring(0,10);
		String errors = "";
		String applicationId = "";
		final String requestDes;
		final String appId;
		try{
			applicationId = request.path("applicationId").asText("");
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(applicationId));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() <= 0){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage("applicationId not exist.");
				return Map.of("status", 200, "data", responseModel);
			}
			String partnerId = checkExist.get(0).getPartnerId();
			String partnerName = checkExist.get(0).getPartnerName();

			if(StringUtils.isEmpty(partnerId)){
				return Map.of("status", 200, "data", "partnerId null");
			}

			String errMsg = "";
			if (request.path("response").hasNonNull("errMsg")){
				errMsg = "FAIL";
			}else{
				String stageResponse = request.findPath("otherInfo").path("currentProcessingStage").asText("");
				if (StringUtils.hasLength(stageResponse)) {
					String stage = StringUtils.hasLength(stageResponse.split(",")[0]) ? stageResponse.split(",")[0].trim() : "";
					if (StringUtils.hasLength(stage) && "LEAD_DETAILS".equals(stage)){
						errMsg = StringUtils.collectionToCommaDelimitedString(getDataF1Service.getListError(applicationId.trim()));
					}
				}else{
					errMsg = "FAIL";
				}
			}

			if(request.findPath("responseCode").asInt(1) == 0
					&& StringUtils.isEmpty(request.findPath("failureMessage").asText("failureMessage"))
					&& StringUtils.isEmpty(errMsg)){
				Update update = new Update();
				update.set("status", "COMPLETED");
				update.set("description", request.findPath("successMessage").asText(""));
				update.set("lastModifiedDate", new Date());
				Application resultUpdatetest = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),Application.class);

				Report report = new Report();
				report.setQuickLeadId(resultUpdatetest.getQuickLeadId());
				report.setApplicationId(applicationId);
				report.setFunction("SENDFULLAPP");
				report.setStatus("COMPLETED");
				report.setCreatedBy("AUTOMATION");
				report.setCreatedDate(new Date());

				report.setPartnerId(partnerId);
				report.setPartnerName(partnerName);

				mongoTemplate.save(report);

				Application dataFullApp = mongoTemplate.findOne(query, Application.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp","reference_id", referenceId,
								"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

				JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), JsonNode.class);

				Map partner = this.getPartner(partnerId);
				if(StringUtils.isEmpty(partner.get("data"))){
					return Map.of("result_code", 3, "message","Not found partner");
				}
				Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
				String feedbackApi = (String) (mapper.convertValue(url, Map.class).get("feedbackApi"));

				if(partnerId.equals("1")){
					String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
					apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
				} else if(partnerId.equals("2")){
					String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
					Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
					String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
					if(StringUtils.isEmpty(tokenPartner)){
						return Map.of("result_code", 3, "message","Not get token saigon-bpo");
					}
					apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
				}
			}else{
				String faultReason = request.findPath("faultReason").asText(request.findPath("failureMessage").asText(""));
				String faultMessage =  StringUtils.hasLength(errMsg) ? errMsg : request.findPath("faultMessage").asText(request.findPath("failureMessage").asText(""));

				Update update = new Update();
				update.set("status", "FULL_APP_FAIL");
				update.set("description", faultReason);
				update.set("error", faultMessage);
				update.set("lastModifiedDate", new Date());
				Application resultUpdatetest = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Application.class);

				//save comment
				CommentModel commentModel = new CommentModel();
				commentModel.setCommentId(commentId);
				commentModel.setType("FICO");
				commentModel.setCode("FICO_ERR");
				commentModel.setDescription(faultReason);
				commentModel.setRequest(faultMessage);

				Query queryAddComment = new Query();
				queryAddComment.addCriteria(Criteria.where("applicationId").is(applicationId));
				Update updateComment = new Update();
				commentModel.setCreatedDate(new Date());
				updateComment.push("comment", commentModel);
				Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, updateComment, Application.class);

				Report report = new Report();
				report.setQuickLeadId(resultUpdate.getQuickLeadId());
				report.setApplicationId(resultUpdate.getApplicationId());
				report.setFunction("FICO_COMMENT");
				report.setStatus("FULL_APP_FAIL");
				report.setCreatedBy("AUTOMATION");
				report.setCreatedDate(new Date());
				report.setPartnerId(partnerId);
				report.setPartnerName(partnerName);

				mongoTemplate.save(report);

				Application dataFullApp = mongoTemplate.findOne(query, Application.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp","reference_id", referenceId,
								"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

				//fico gui comment
				JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "failed",
						"commend-id", commentId, "errors", faultMessage)), JsonNode.class);

				Map partner = this.getPartner(partnerId);
				if(StringUtils.isEmpty(partner.get("data"))){
					return Map.of("result_code", 3, "message","Not found partner");
				}
				Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
				String feedbackApi = (String) (mapper.convertValue(url, Map.class).get("feedbackApi"));

				if(partnerId.equals("1")){
					String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
					apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
//						apiService.callApiDigitexx(urlDigitexFeedbackApi, dataSend);
				} else if(partnerId.equals("2")){
					String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
					Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
					String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
					if(StringUtils.isEmpty(tokenPartner)){
						return Map.of("result_code", 3, "message","Not get token saigon-bpo");
					}
					apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
				}
				try{
					appId = applicationId;
					requestDes = errors;
					new Thread(() -> {
						try {
							String resultInsertORA = insertToOracle_Return(appId, commentId,"FICO", requestDes, null, null, new Date(), null);
							if (!resultInsertORA.equals("success")) {
								log.info("ReferenceId : " + referenceId + "; Insert to Oracle Return: " + resultInsertORA);
							}
						}
						catch (Exception e) {}
					}).start();
				}
				catch (Exception e) {}
			}

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

	public Map<String, Object> updateAppErrorApiF1(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("request_id").asText("");

		String applicationId = "";
		String commentId = UUID.randomUUID().toString().substring(0,10);
		String referenceId = UUID.randomUUID().toString();
		String errors = "";
		final String requestDes;
		final String appId;
		try{
			applicationId = request.path("applicationId").asText("");
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(applicationId));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				String partnerId = checkExist.get(0).getPartnerId();
				String partnerName = checkExist.get(0).getPartnerName();
				if(StringUtils.isEmpty(partnerId)){
					return Map.of("status", 200, "data", "partnerId null");
				}

				String errMsg = "";
				if (request.path("response").hasNonNull("errMsg")){
					errMsg = "FAIL";
				}else{
					String stageResponse = request.findPath("otherInfo").path("currentProcessingStage").asText("");
					if (StringUtils.hasLength(stageResponse)) {
						String stage = StringUtils.hasLength(stageResponse.split(",")[0]) ? stageResponse.split(",")[0].trim() : "";
						if (StringUtils.hasLength(stage) && "LEAD_DETAILS".equals(stage)){
							errMsg = StringUtils.collectionToCommaDelimitedString(getDataF1Service.getListError(applicationId.trim()));
						}
					}else{
						errMsg = "FAIL";
					}
				}

				if(request.findPath("responseCode").asInt(1) == 0
						&& StringUtils.isEmpty(request.findPath("failureMessage").asText("failureMessage"))
						&& StringUtils.isEmpty(errMsg)){
					Update update = new Update();
					update.set("status", "COMPLETED");
					update.set("description", request.findPath("successMessage").asText("Success"));
					update.set("lastModifiedDate", new Date());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),Application.class);

					Report report = new Report();
					report.setQuickLeadId(resultUpdatetest.getQuickLeadId());
					report.setApplicationId(resultUpdatetest.getApplicationId());
					report.setFunction("UPDATEFULLAPP");
					report.setStatus("COMPLETED");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());

					report.setPartnerId(partnerId);
					report.setPartnerName(partnerName);

					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), JsonNode.class);

					Map partner = this.getPartner(partnerId);
					if(StringUtils.isEmpty(partner.get("data"))){
						return Map.of("result_code", 3, "message","Not found partner");
					}
					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					String feedbackApi = (String) mapper.convertValue(url, Map.class).get("feedbackApi");

					if(partnerId.equals("1")){
						String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return Map.of("result_code", 3, "message","Not get token saigon-bpo");
						}
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
					}

				}else{
					String faultReason = request.findPath("faultReason").asText(request.findPath("failureMessage").asText(""));
					String faultMessage =  StringUtils.hasLength(errMsg) ? errMsg : request.findPath("faultMessage").asText(request.findPath("failureMessage").asText(""));

					Update update = new Update();
					update.set("status", "FULL_APP_FAIL");
					update.set("description", faultReason);
					update.set("error", faultMessage);
					update.set("lastModifiedDate", new Date());
					Application resultUpdatetest = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Application.class);

					//save comment
					CommentModel commentModel = new CommentModel();
					commentModel.setCommentId(commentId);
					commentModel.setType("FICO");
					commentModel.setCode("FICO_ERR");
					commentModel.setDescription(faultReason);
					commentModel.setRequest(faultMessage);

					Query queryAddComment = new Query();
					queryAddComment.addCriteria(Criteria.where("applicationId").is(applicationId));
					Update updateComment = new Update();
					commentModel.setCreatedDate(new Date());
					updateComment.push("comment", commentModel);

					Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, updateComment, Application.class);

					Report report = new Report();
					report.setQuickLeadId(resultUpdate.getQuickLeadId());
					report.setApplicationId(resultUpdate.getApplicationId());
					report.setFunction("FICO_COMMENT");
					report.setStatus("FULL_APP_FAIL");
					report.setCreatedBy("AUTOMATION");
					report.setCreatedDate(new Date());

					report.setPartnerId(partnerId);
					report.setPartnerName(partnerName);

					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);

					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					//fico gui comment

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "failed",
							"commend-id", commentId, "errors", faultMessage)), JsonNode.class);

					Map partner = this.getPartner(partnerId);
					if(StringUtils.isEmpty(partner.get("data"))){
						return Map.of("result_code", 3, "message","Not found partner");
					}
					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					String feedbackApi = (String) mapper.convertValue(url, Map.class).get("feedbackApi");

					if(partnerId.equals("1")){
						String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
//						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
						apiService.callApiDigitexx(urlDigitexFeedbackApi, dataSend);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return Map.of("result_code", 3, "message","Not get token saigon-bpo");
						}
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
					}

					try{
						appId = applicationId;
						requestDes = errors;
						new Thread(() -> {
							try {
								String resultInsertORA = insertToOracle_Return(appId, commentId, "FICO", requestDes, null, null, new Date(), null);
								if (!resultInsertORA.equals("success")) {
									log.info("ReferenceId : " + referenceId + "; Insert to Oracle Return: " + resultInsertORA);
								}
							}
							catch (Exception e) {}
						}).start();
					}
					catch (Exception e) {}
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

	public Map<String, Object> callApiF1(JsonNode request){
		String func = request.path("body").path("func").asText("");
		JsonNode jsonNode = mapper.createObjectNode();
		switch (func) {
			case "getStatus":
				String loanApplicationNumber = request.path("body").path("applicationId").asText("");
				jsonNode = apiService.callApiF1(urlGetStatus, mapper.convertValue(Map.of("loanApplicationNumber", loanApplicationNumber), JsonNode.class));
				break;
//			case "update":
//				jsonNode = apiService.callApiF1(urlUpdateApp, request);
//				break;
//			default:
//				jsonNode = apiService.callApiF1(urlGetStatus, request);
//				break;
		}
		return Map.of("status", 200, "data", jsonNode);
	}

	/**
	 * Mobility call data entry quick lead
	 * @param request
	 * @return
	 */
	public  Map<String, Object> sendAppNonWeb(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		ObjectNode logInfo = mapper.createObjectNode();
		logInfo.put("func", new Throwable().getStackTrace()[0].getMethodName());
		logInfo.put("request", request.toString());
		try{
			RequestQuickLeadModel requestModel = mapper.treeToValue(request.get("body"), RequestQuickLeadModel.class);

			QuickLead data = requestModel.getData();
			if (StringUtils.isEmpty(data.getPartnerId())){
				throw new Exception("partnerId null");
			}
			String project = request.findPath("project").asText("");
			if (StringUtils.isEmpty(project)){
				throw new Exception("project null");
			}
			if (data.getDocuments().size() <= 0){
				throw new Exception("document missing");
			}
			Optional optional = data.getDocuments()
					.stream()
					.filter(qlDocument -> StringUtils.hasLength(qlDocument.getType()) && StringUtils.hasLength(qlDocument.getContentType())).findAny();
			if (optional.isEmpty()){
				throw new Exception("document type or content type missing");
			}

			data.setCreatedDate(new Timestamp(new Date().getTime()));
			data.getDocuments().stream().map(qlDocument -> {
				qlDocument.setOriginalname(qlDocument.getType() + "." + qlDocument.getContentType());
				return qlDocument;
			}).collect(Collectors.toList());

			String errMsg = saveApp(data, project);
			if (StringUtils.hasLength(errMsg)){
				throw new Exception(errMsg);
			}
			logInfo.put("saveApp", "SUCCESS");

			errMsg = sendFileToPartner(data.getQuickLeadId(), data.getPartnerId(), data.getDocuments(), true);
			if (StringUtils.hasLength(errMsg)){
				throw new Exception(errMsg);
			}
			logInfo.put("sendFileToPartner", "SUCCESS");

			Query query = new Query();
			query.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
			List<Application> appData = mongoTemplate.find(query, Application.class);
			if (appData.size() <= 0){
				throw new Exception("application not exist");
			}

			Report report = new Report();
			report.setQuickLeadId(data.getQuickLeadId());
			report.setApplicationId(request.path("body").path("applicationId").textValue());
			report.setFunction("QUICKLEAD");
			report.setStatus("NEW");
			report.setCreatedBy(project);
			report.setCreatedDate(new Date());

			report.setPartnerId(appData.get(0).getPartnerId());
			report.setPartnerName(appData.get(0).getPartnerName());

			mongoTemplate.save(report);
			logInfo.put("saveReport", "SUCCESS");
			String requestId = StringUtils.hasLength(requestModel.getRequest_id()) ? requestModel.getRequest_id() : UUID.randomUUID().toString();
			if(apiService.chooseAutoOrApiF1() == 1) {
				new Thread(() -> {
					try{
						ObjectNode result = mapper.createObjectNode();
						result.put("request_id", requestId);
						result.put("quickLeadId", appData.get(0).getQuickLeadId());

						JsonNode response = apiService.callApiF1(urlCreateLead, convertService.toApiF1(appData.get(0)));
						result.set("response", response);
						updateAfterQuickLeadF1(result);
					}catch(Exception e){
						log.info("quickLead.thread.apiService.callApiF1.Exception {}", e.toString());
					}
				}).start();
			}else{
				rabbitMQService.send(queueAutoSGB, Map.of("func", "quickLeadApp", "body", appData.get(0)));
			}

			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("0");
			return Map.of("status", 200, "data", responseModel);
		}catch (Exception e) {
			String error =  StringUtils.hasLength(e.getMessage()) ? e.getMessage(): e.toString();
			logInfo.put("exception", error);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(StringUtils.isEmpty(error) ? "Others error" : error);
			return Map.of("status", 200, "data", responseModel);
		}finally {
			log.info("{}", logInfo);
		}
	}

	/**
	 * Mobility upload file data entry with app RETURNED
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public  Map<String, Object> commentAppNonWeb(JsonNode request) throws Exception {
		ObjectNode logInfo = mapper.createObjectNode();
		logInfo.put("func", new Throwable().getStackTrace()[0].getMethodName());
		logInfo.put("request", request.toString());
		String requestId = request.findPath("request_Id").asText(UUID.randomUUID().toString());
		String referenceId = UUID.randomUUID().toString();
		String createFrom = null;
		String appId = null;
		String quickLeadId = null;
		String error = null;
		ResponseModel responseModel = new ResponseModel();
		responseModel.setRequest_id(requestId);
		responseModel.setReference_id(referenceId);
		responseModel.setDate_time(new Timestamp(new Date().getTime()));
		try {
			Application appRequest = new Application();
			error = convertService.toApplication(request, appRequest);
			if (StringUtils.hasLength(error)){
				throw new DataEntryException(1, error);
			}
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(appRequest.getApplicationId()));
			List<Application> appDB = mongoTemplate.find(query, Application.class);
			if (appDB.size() <= 0){
				throw new DataEntryException(1, "applicationId not exists");
			}
			createFrom = appDB.get(0).getCreateFrom();
			appId = appDB.get(0).getApplicationId();
			quickLeadId = appDB.get(0).getQuickLeadId();
			Optional opt = appDB.get(0).getComment()
					.stream()
					.filter(commentModel -> commentModel.getResponse() == null).findAny();
			if (!opt.isPresent()){
				throw new DataEntryException(1, "application had response");
			}
			opt = appDB.get(0).getQuickLead().getDocuments()
					.stream()
					.filter(qlDocument -> qlDocument.getOriginalname().equals(appRequest.getComment().get(0).getResponse().getDocuments().get(0).getOriginalname())).findAny();
			if (!opt.isPresent()){
				throw new DataEntryException(1, "document type not valid");
			}
			appDB.get(0).getQuickLead().getDocuments()
					.stream()
					.filter(qlDocument -> qlDocument.getOriginalname().equals(appRequest.getQuickLead().getDocumentsComment().get(0).getOriginalname()))
					.findAny().ifPresent(qlDocument -> {
				appRequest.getQuickLead().getDocumentsComment().get(0).setUrlid(qlDocument.getUrlid());
				appRequest.getComment().get(0).getResponse().getDocuments().get(0).setUrlid(qlDocument.getUrlid());
				appRequest.getComment().get(0).getResponse().getDocuments().get(0).getLink().setUrlPartner(qlDocument.getUrlid());
				appDB.get(0).getQuickLead().setDocumentsComment(appRequest.getComment().get(0).getResponse().getDocuments()
						.stream().map(document -> {
							QLDocument qlDocument1 = new QLDocument();
							qlDocument1.setType(document.getType());
							qlDocument1.setFilename(document.getFilename());
							qlDocument1.setOriginalname(document.getOriginalname());
							qlDocument1.setUrlid(qlDocument.getUrlid());
							return qlDocument1;
						}).collect(Collectors.toList()));
			});

			appDB.get(0).getComment()
					.stream()
					.filter(commentModel -> commentModel.getResponse() == null && !appDB.get(0).getStatus().equals("FULL_APP_FAIL"))
					.findAny().ifPresent(commentModel -> {
				appRequest.getComment().get(0).setCommentId(commentModel.getCommentId());
				appRequest.getComment().get(0).setType(commentModel.getType());
				appRequest.getComment().get(0).setCode(commentModel.getCode());
				appRequest.getComment().get(0).setRequest(commentModel.getRequest());
				appRequest.getComment().get(0).setCreatedDate(commentModel.getCreatedDate());
				commentModel.setResponse(appRequest.getComment().get(0).getResponse());
			});

			mongoTemplate.save(appDB.get(0));
			return commentAppForOtherService(requestId, appRequest, appDB.get(0), null);
		}catch (DataEntryException e){
			error =  StringUtils.hasLength(e.getMessage()) ? e.getMessage(): e.toString();
			logInfo.put("exception", error);
			responseModel.setResult_code(String.valueOf(e.getType()));
			responseModel.setMessage(error);
			if (StringUtils.hasLength(createFrom) && !"WEB".equals(createFrom.toUpperCase())){
				rabbitMQService.send("tpf-service-esb",
						Map.of("func", "updateAutomation", "reference_id", referenceId, "body", Map.of("app_id", appId != null ? appId : "",
								"project", createFrom,
								"automation_result", "RESPONSEQUERY FAILED",
								"description", error,
								"transaction_id", quickLeadId,
								"automation_account", "")));
			}

			return Map.of("status", 200, "data", responseModel);
		}catch (Exception e){
			error =  StringUtils.hasLength(e.getMessage()) ? e.getMessage(): e.toString();
			logInfo.put("exception", error);
			responseModel.setResult_code("3");
			responseModel.setMessage("Others error");

			if (StringUtils.hasLength(createFrom) && !"WEB".equals(createFrom.toUpperCase())){
				rabbitMQService.send("tpf-service-esb",
						Map.of("func", "updateAutomation", "reference_id", referenceId, "body", Map.of("app_id", appId != null ? appId : "",
								"project", createFrom,
								"automation_result", "RESPONSEQUERY FAILED",
								"description", error,
								"transaction_id", quickLeadId,
								"automation_account", "")));
			}
			return Map.of("status", 200, "data", responseModel);
		}finally {
			log.info("{}", logInfo);
		}
	}

	/**
	 * data entry send comment to mobility
	 * @param requestId
	 * @param appRequest
	 * @param appDB
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> commentAppForOtherService(String requestId, Application appRequest, Application appDB, JsonNode request) throws Exception {
		ObjectNode logInfo = mapper.createObjectNode();
		logInfo.put("func", new Throwable().getStackTrace()[0].getMethodName());
		String referenceId = UUID.randomUUID().toString();
		String automationAcc = "";
		String automationResult = "";
		String description = "";
		ResponseModel responseModel = new ResponseModel();
		responseModel.setRequest_id(requestId);
		responseModel.setReference_id(referenceId);
		responseModel.setDate_time(new Timestamp(new Date().getTime()));
		try {
			logInfo.put("applicationId", appRequest.getApplicationId());
			if (appRequest.getComment().size() <= 0){
				throw new DataEntryException(1, "comments null");
			}
			appRequest.setLastModifiedDate(new Timestamp(new Date().getTime()));
			String project = appDB.getCreateFrom();
			String errMsg = appRequest.getComment().stream().map(commentModel -> {
				Query queryComment = new Query();
				queryComment.addCriteria(Criteria.where("applicationId").is(appDB.getApplicationId()).and("comment.commentId").is(commentModel.getCommentId()));
				boolean exist = mongoTemplate.exists(queryComment, Application.class);
				String err;
				if (!exist){
					//comment before full app
					err = saveData(commentModel, appDB.getApplicationId(), referenceId, project);
					if (StringUtils.hasLength(err)){
						return err;
					}
					logInfo.put("saveData", "SUCCESS");
					if (request != null){
						err = sendCommentToOtherService(project, request);
						logInfo.put("send-to-" + project, StringUtils.hasLength(err) ? err : "SUCCESS");
					}
					return err;
				}
				//fico comment to partner
				if (appRequest.getQuickLead().getDocumentsComment() != null && appRequest.getQuickLead().getDocumentsComment().size() > 0){
					err = sendFileToPartner(appDB.getQuickLeadId(), appDB.getPartnerId(), appRequest.getQuickLead().getDocumentsComment(), false);
					if (StringUtils.hasLength(err)){
						return err;
					}
				}
				err = sendCommentToPartner(referenceId, commentModel, appRequest, appDB, project);
				logInfo.put("send-to-partner", StringUtils.hasLength(err) ? err : "SUCCESS");
				if (StringUtils.isEmpty(err)){
					if (StringUtils.hasLength(appDB.getCreateFrom()) && !"WEB".equals(appDB.getCreateFrom().toUpperCase())){
						String account = request.path("body").path("automationAcc").asText("");
						try {
							rabbitMQService.send("tpf-service-esb",
									Map.of("func", "updateAutomation", "reference_id", referenceId, "body", Map.of("app_id", appDB.getApplicationId() != null ? appDB.getApplicationId() : "",
											"project", appDB.getCreateFrom(),
											"automation_result", "RESPONSEQUERY PASS",
											"description", "Thanh cong",
											"transaction_id", appDB.getQuickLeadId(),
											"automation_account", account)));
						} catch (Exception e) {
							err = e.getMessage();
						}
					}
				}
				return err;
			}).collect(Collectors.joining());

			if (StringUtils.hasLength(errMsg)){
				throw new DataEntryException(2, errMsg);
			}
			responseModel.setResult_code("0");
		} catch (DataEntryException e){
			String error =  StringUtils.hasLength(e.getMessage()) ? e.getMessage(): e.toString();
			description = error;
			logInfo.put("exception", error);
			responseModel.setResult_code(String.valueOf(e.getType()));
			responseModel.setMessage(error);
			automationResult = "RESPONSEQUERY FAILED";
			if (StringUtils.hasLength(appDB.getCreateFrom()) && !"WEB".equals(appDB.getCreateFrom().toUpperCase())){
				rabbitMQService.send("tpf-service-esb",
						Map.of("func", "updateAutomation", "reference_id", referenceId, "body", Map.of("app_id", appDB.getApplicationId() != null ? appDB.getApplicationId() : "",
								"project", appDB.getCreateFrom(),
								"automation_result", automationResult,
								"description", description,
								"transaction_id", appDB.getQuickLeadId(),
								"automation_account", automationAcc)));
			}
			rollBackApp(appRequest, appDB);
		} catch (Exception e){
			String error =  StringUtils.hasLength(e.getMessage()) ? e.getMessage(): e.toString();
			description = error;
			logInfo.put("exception", error);
			responseModel.setResult_code("3");
			responseModel.setMessage("Others error");
			automationResult = "RESPONSEQUERY FAILED";
			if (StringUtils.hasLength(appDB.getCreateFrom()) && !"WEB".equals(appDB.getCreateFrom().toUpperCase())){
				rabbitMQService.send("tpf-service-esb",
						Map.of("func", "updateAutomation", "reference_id", referenceId, "body", Map.of("app_id", appDB.getApplicationId() != null ? appDB.getApplicationId() : "",
								"project", appDB.getCreateFrom(),
								"automation_result", automationResult,
								"description", description,
								"transaction_id", appDB.getQuickLeadId(),
								"automation_account", automationAcc)));
			}
			rollBackApp(appRequest, appDB);
		} finally {
			log.info("{}", logInfo);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	private void rollBackApp(Application appRequest, Application appDB) {
		try {
			appDB.getComment()
					.stream().filter(commentModel -> commentModel.getCommentId().equals(appRequest.getComment().get(0).getCommentId()))
					.findAny().ifPresent(commentModel -> commentModel.setResponse(null));
			mongoTemplate.save(appDB);
		}catch (Exception e){
			throw e;
		}
	}

	private String sendCommentToPartner(String referenceId, CommentModel commentModel, Application appRequest, Application appDB, String userName) {
		ObjectNode logInfo = mapper.createObjectNode();
		logInfo.put("func", "sendCommentToPartner");
		try{
			logInfo.put("applicationId", appRequest.getApplicationId());

			if (commentModel.getResponse().getComment() == null || commentModel.getResponse().getComment().equals("")){
				throw new Exception("vui lòng nhập comment!");
			}
			List<Document> documentCommnet = commentModel.getResponse().getDocuments();
			List<CommentModel> listComment = appDB.getComment();
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(appRequest.getApplicationId()).and("comment.commentId").is(commentModel.getCommentId()));
			String comment = "";
			for (CommentModel itemComment : listComment) {
				if (itemComment.getCommentId().equals(commentModel.getCommentId())) {
					if (commentModel.getResponse() != null) {
						if (commentModel.getResponse().getDocuments().size() > 0) {
							for (Document itemCommentFico : commentModel.getResponse().getDocuments()) {
								Link link = new Link();
								link.setUrlFico(itemCommentFico.getFilename());
								link.setUrlPartner(itemCommentFico.getUrlid());
								itemCommentFico.setLink(link);
							}
						}

						Update update = new Update();
						update.set("comment.$.response", commentModel.getResponse());
						Application resultUpdate = mongoTemplate.findAndModify(query, update, Application.class);

						comment = commentModel.getResponse().getComment();
						new Thread(() -> {
							try {
								String resultInsertORA = insertToOracle_Return(appRequest.getApplicationId(), commentModel.getCommentId(), commentModel.getType(), null, commentModel.getResponse().getComment(),
										userName, commentModel.getCreatedDate(), new Date());
								if (!resultInsertORA.equals("success")) {
									log.info("ReferenceId : " + referenceId + "; Insert to Oracle Return: " + resultInsertORA);
								}
							}
							catch (Exception e) {}
						}).start();
					}
				}
			}

			ArrayNode documents = mapper.createArrayNode();
			boolean checkIdCard = false;
			boolean checkHousehold = false;
			for (Document item : documentCommnet) {
				ObjectNode doc = mapper.createObjectNode();

				if (item.getType().toUpperCase().equals("TPF_ID Card".toUpperCase())) {
					if (!checkIdCard) {
						if (item.getComment() != null) {
							doc.put("documentComment", item.getComment());
						} else {
							doc.put("documentComment", "");
						}
						if (item.getLink() != null) {
							doc.put("documentId", item.getLink().getUrlPartner());
							documents.add(doc);
						}

						checkIdCard = true;
					}
				} else if (item.getType().toUpperCase().equals("TPF_Notarization of ID card".toUpperCase())) {
					if (!checkIdCard) {
						if (item.getComment() != null) {
							doc.put("documentComment", item.getComment());
						} else {
							doc.put("documentComment", "");
						}
						if (item.getLink() != null) {
							doc.put("documentId", item.getLink().getUrlPartner());
							documents.add(doc);
						}

						checkIdCard = true;
					}
				}
				if (item.getType().toUpperCase().equals("TPF_Family Book".toUpperCase())) {
					if (!checkHousehold) {
						if (item.getComment() != null) {
							doc.put("documentComment", item.getComment());
						} else {
							doc.put("documentComment", "");
						}
						if (item.getLink() != null) {
							doc.put("documentId", item.getLink().getUrlPartner());
							documents.add(doc);
						}

						checkHousehold = true;
					}
				} else if (item.getType().toUpperCase().equals("TPF_Notarization of Family Book".toUpperCase())) {
					if (!checkHousehold) {
						if (item.getComment() != null) {
							doc.put("documentComment", item.getComment());
						} else {
							doc.put("documentComment", "");
						}
						if (item.getLink() != null) {
							doc.put("documentId", item.getLink().getUrlPartner());
							documents.add(doc);
						}

						checkHousehold = true;
					}
				} else if (item.getType().toUpperCase().equals("TPF_Customer Photograph".toUpperCase())) {
					if (item.getComment() != null) {
						doc.put("documentComment", item.getComment());
					} else {
						doc.put("documentComment", "");
					}
					if (item.getLink() != null) {
						doc.put("documentId", item.getLink().getUrlPartner());
						documents.add(doc);
					}
				} else if (item.getType().toUpperCase().equals("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
					if (item.getComment() != null) {
						doc.put("documentComment", item.getComment());
					} else {
						doc.put("documentComment", "");
					}
					if (item.getLink() != null) {
						doc.put("documentId", item.getLink().getUrlPartner());
						documents.add(doc);
					}
				}
			}

			JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", appDB.getApplicationId(), "comment-id", commentModel.getCommentId(),
					"comment", comment, "documents", documents)), JsonNode.class);

			String partnerId = appDB.getPartnerId();
			Map partner = getPartner(partnerId);
			if (StringUtils.isEmpty(partner.get("data"))) {
				throw new Exception("Not found partner");
			}

			Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
			String resubmitCommentApi = (String) mapper.convertValue(url, Map.class).get("resubmitCommentApi");

			JsonNode responseDG = mapper.createObjectNode();

			if (partnerId.equals("1")) {
				String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
//							apiService.callApiPartner(resubmitCommentApi, dataSend, tokenPartner, partnerId);
				responseDG = apiService.callApiDigitexx(urlDigitexResubmitCommentApi, dataSend);
			} else if (partnerId.equals("2")) {
				String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
				Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
				String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
				if (StringUtils.isEmpty(tokenPartner)) {
					throw new Exception("Not get token saigon-bpo");
				}
				responseDG = apiService.callApiPartner(resubmitCommentApi, dataSend, tokenPartner, partnerId);
			}

			if (!responseDG.path("error-code").textValue().equals("")) {
				if (!responseDG.path("error-code").textValue().equals("null")) {
					throw new Exception(responseDG.path("error-code").textValue() + responseDG.path("error-description").textValue());
				}
			}

			Query queryUpdate = new Query();
			queryUpdate.addCriteria(Criteria.where("applicationId").is(appRequest.getApplicationId()));
			Update update = new Update();
			update.set("status", "PROCESSING");
			update.set("lastModifiedDate", new Date());
			Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

			Application dataFullApp = mongoTemplate.findOne(query, Application.class);
			rabbitMQService.send("tpf-service-app",
					Map.of("func", "updateApp", "reference_id", referenceId,
							"param", Map.of("project", "dataentry", "id", dataFullApp.getId()), "body", convertService.toAppDisplay(dataFullApp)));

			Report report = new Report();
			report.setQuickLeadId(dataFullApp.getQuickLeadId());
			report.setApplicationId(appRequest.getApplicationId());
			report.setFunction("FICO_RETURN_COMMENT");
			report.setStatus("PROCESSING");
			report.setCommentDescription(comment);
			report.setCreatedBy(userName);
			report.setCreatedDate(new Date());

			report.setPartnerId(dataFullApp.getPartnerId());
			report.setPartnerName(dataFullApp.getPartnerName());

			mongoTemplate.save(report);
			return "";
		} catch (Exception e) {
			String error = StringUtils.hasLength(e.getMessage()) ? e.getMessage() : e.toString();
			logInfo.put("exception", error);
			return error;
		} finally {
			log.info("{}", logInfo);
		}
	}

	private String saveData(CommentModel commentModel, String applicationId, String referenceId, String userName) {
		ObjectNode logInfo = mapper.createObjectNode();
		logInfo.put("func", "saveData");
		try {
			logInfo.put("applicationId", applicationId);
			if (ThirdPartyType.fromName(commentModel.getType().toUpperCase()) == null){
				throw new Exception("Comment Type Invalid!");
			}
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(applicationId));

			Update update = new Update();
			commentModel.setCreatedDate(new Date());
			update.push("comment", commentModel);
			update.set("status", "RETURNED");
			update.set("lastModifiedDate", new Date());
			Application dataFullApp = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Application.class);
			logInfo.put("save-app", "SUCCESS");

			new Thread(() -> {
				try {
					String resultInsertORA = insertToOracle_Return(applicationId, commentModel.getCommentId(), commentModel.getType(), commentModel.getRequest(), null,
							userName, commentModel.getCreatedDate(), null);
					if (!resultInsertORA.equals("success")) {
						log.info("ReferenceId : " + referenceId + "; Insert to Oracle Return: " + resultInsertORA);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}).start();

			Report report = new Report();
			report.setQuickLeadId(dataFullApp.getQuickLeadId());
			report.setApplicationId(applicationId);
			report.setFunction("DIGITEXX_COMMENT");
			report.setStatus("RETURNED");

			if (StringUtils.hasLength(commentModel.getRequest())) {
				report.setCommentDescription(commentModel.getRequest());
			}

			report.setCreatedBy(userName);
			report.setCreatedDate(new Date());

			if (dataFullApp != null) {
				report.setPartnerId(dataFullApp.getPartnerId());
				report.setPartnerName(dataFullApp.getPartnerName());
			}
			mongoTemplate.save(report);
			logInfo.put("save-report", "SUCCESS");
			return "";
		}catch (Exception e){
			String error = StringUtils.hasLength(e.getMessage()) ? e.getMessage() : e.toString();
			logInfo.put("exception", error);
			return error;
		}finally {
			log.info("{}", logInfo);
		}
	}

	private String sendCommentToOtherService(String project, JsonNode request) {
		ObjectNode logInfo = mapper.createObjectNode();
		logInfo.put("func", "sendCommentToOtherService");
		try {
			logInfo.put("applicationId", request.toString());
			if (StringUtils.isEmpty(project)){
				throw new Exception("project null");
			}
			rabbitMQService.send("tpf-service-" + project, request);
			logInfo.put("send-to-" + project, "SUCCESS");
			return "";
		}catch (Exception e){
			String error = StringUtils.hasLength(e.getMessage()) ? e.getMessage() : e.toString();
			logInfo.put("exception", error);
			return error;
		}finally {
			log.info("{}", logInfo);
		}
	}

	private String saveApp(QuickLead data, String project) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
			boolean exist = mongoTemplate.exists(query, Application.class);

			if (exist){
				data.setQuickLeadId(data.getQuickLeadId() + "-" + new Date().getTime());
			}

			Map partner = this.getPartner(data.getPartnerId());
			Application app = new Application();
			app.setQuickLeadId(data.getQuickLeadId());
			app.setQuickLead(data);
			app.setStatus("UPLOADFILE");
			app.setUserName(project);
			app.setCreateFrom(project);
			app.setPartnerId(mapper.convertValue(partner.get("data"), Map.class).get("partnerId").toString());
			app.setPartnerName(mapper.convertValue(partner.get("data"), Map.class).get("partnerName").toString());
			mongoTemplate.save(app);

			Report report = new Report();
			report.setQuickLeadId(app.getQuickLeadId());
			report.setFunction("UPLOADFILE");
			report.setStatus("UPLOADFILE");
			report.setDescription("upload success");
			report.setCreatedBy(project);
			report.setCreatedDate(new Date());

			report.setPartnerId(app.getPartnerId());
			report.setPartnerName(app.getPartnerName());

			mongoTemplate.save(report);

			return "";
		} catch (Exception e){
			String error =  StringUtils.hasLength(e.getMessage()) ? e.getMessage(): e.toString();
			return "func: " + new Throwable().getStackTrace()[0].getMethodName() + ": " + error;
		}
	}

	private String sendFileToPartner(String quickLeadId, String partnerId, List<QLDocument> documents, boolean isNew){
		try {
			Map partner = this.getPartner(partnerId);
			Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
			String urlResubmitDoc = (String) mapper.convertValue(url, Map.class).get("resumitDocumentApi");
			String urlDoc = (String) mapper.convertValue(url, Map.class).get("documentApi");
			Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
			String token = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
			if (partnerId.equals("2")){
				String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
				token = apiService.getTokenSaigonBpo(urlGetToken, account);
				if(StringUtils.isEmpty(token)){
					throw new Exception("Not get token saigon-bpo");
				}
			}

			JsonNode resultUpload;
			if (isNew){
				resultUpload = apiService.uploadFileToPartner(documents, partnerId, urlDoc, token);
				if (resultUpload.hasNonNull("error")){
					throw new Exception(resultUpload.path("error").asText(""));
				}

				List<QLDocument> dataUpload = mapper.readValue(resultUpload.toString(), new TypeReference<List<QLDocument>>() {
				});
				for (QLDocument item : dataUpload) {
					Query queryUpdate = new Query();
					queryUpdate.addCriteria(Criteria.where("quickLeadId").is(quickLeadId).and("quickLead.documents.originalname").is(item.getOriginalname()));

					Update update = new Update();
					update.set("quickLead.documents.$.urlid", item.getUrlid());
					mongoTemplate.findAndModify(queryUpdate, update, Application.class);
				}
				return "";
			}
			resultUpload = apiService.uploadFileToPartner(documents, partnerId, urlResubmitDoc, token);
			if (resultUpload.hasNonNull("error")){
				throw new Exception(resultUpload.path("error").asText(""));
			}
			List<QLDocument> dataUpload = mapper.readValue(resultUpload.toString(), new TypeReference<List<QLDocument>>() {
			});
			for (QLDocument item : dataUpload) {
				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("quickLeadId").is(quickLeadId).and("quickLead.documents.originalname").is(item.getOriginalname()));
				Update update = new Update();
				update.set("quickLead.documentsComment.$.filename", item.getFilename());
				update.set("quickLead.documentsComment.$.urlid", item.getUrlid());
				update.set("quickLead.documentsComment.$.md5", item.getMd5());
				update.set("quickLead.documentsComment.$.contentType", item.getContentType());
				mongoTemplate.findAndModify(queryUpdate, update, Application.class);
			}
			return "";
		}catch (Exception e){
			String error =  StringUtils.hasLength(e.getMessage()) ? e.getMessage(): e.toString();
			return "func: " + new Throwable().getStackTrace()[0].getMethodName() + ": " + error;
		}
	}



}