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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
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
import java.sql.Timestamp;
import java.time.Duration;
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


						Update update = new Update();
						update.set("applicationInformation", data.getApplicationInformation());
						update.set("loanDetails", data.getLoanDetails());
						update.set("references", data.getReferences());
						update.set("dynamicForm", data.getDynamicForm());
						update.set("lastModifiedDate", new Date());
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
			responseModel.setMessage("Others error");
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

	public Map<String, Object> oldCommentApp(JsonNode request, JsonNode token) {
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
				try{
					if (checkExist.get(0).getStatus().equals("COMPLETED")){
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(referenceId);
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("applicationId is completed!");

						return Map.of("status", 200, "data", responseModel);
					}
				}
				catch (Exception ex){}

				for (CommentModel item : data.getComment()) {
//					documentCommnet = item.getResponse().getDocuments();
					commentId = item.getCommentId();
					Query queryUpdate = new Query();
					queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()).and("comment.commentId").is(item.getCommentId()));
					List<Application> checkCommentExist = mongoTemplate.find(queryUpdate, Application.class);
//					String typeComment = checkCommentExist.get(0).getComment().get(0).getType();

					if (checkCommentExist.size() <= 0){
						if (ThirdPartyType.fromName(item.getType().toUpperCase()) != null) {// digitex gui comment
							Query queryAddComment = new Query();
							queryAddComment.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));

							Update update = new Update();
							item.setCreatedDate(new Date());
							update.push("comment", item);
							Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, update, Application.class);

							requestCommnentFromDigiTex = true;
						}else{
							responseModel.setRequest_id(requestId);
							responseModel.setReference_id(referenceId);
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code("1");
							responseModel.setMessage("Comment Type Invalid!");

							return Map.of("status", 200, "data", responseModel);
						}
					}else {
						if (item.getType().equals("FICO")) {// digitex tra comment
//						if (checkCommentExist.get(0).getComment().get(0).getType().equals("FICO")) {// digitex tra comment(do digites k gui lai type nen k dung item.getType())
							boolean checkResponseComment = false;

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

							try{
								quickLeadId = checkCommentExist.get(0).getQuickLeadId();
							}
							catch (Exception ex){}

							for (CommentModel itemComment : listComment) {
								if (itemComment.getCommentId().equals(item.getCommentId())) {
									if (itemComment.getResponse() == null) {
										stageAuto = itemComment.getStage();
										Update update = new Update();
										update.set("comment.$.response", item.getResponse());
										Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

										checkResponseComment = true;
									}
								}
							}

							if (!checkResponseComment){
								responseModel.setRequest_id(requestId);
								responseModel.setReference_id(referenceId);
								responseModel.setDate_time(new Timestamp(new Date().getTime()));
								responseModel.setResult_code("1");
								responseModel.setMessage("applicationId can not return comment!");

								return Map.of("status", 200, "data", responseModel);
							}

							responseCommnentFullAPPFromDigiTex = true;

							// update automation
							if (item.getResponse().getData() != null){
								Application dataUpdate = item.getResponse().getData();
								if (checkCommentExist.get(0).getQuickLead().getDocumentsComment() != null){
									dataUpdate.setDocuments(checkCommentExist.get(0).getQuickLead().getDocumentsComment());
								}
								dataUpdate.setStage(stageAuto);
								dataUpdate.setError(errorAuto);
								rabbitMQService.send("tpf-service-automation",
										Map.of("func", "updateAppError","body", dataUpdate));
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
							}catch (Exception ex){

							}

							documentCommnet = item.getResponse().getDocuments();
							List<CommentModel> listComment = checkCommentExist.get(0).getComment();
							for (CommentModel itemComment : listComment) {
								if (itemComment.getCommentId().equals(item.getCommentId())) {
									if (item.getResponse() != null) {
										if (itemComment.getResponse() == null) {
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

											responseCommnentToDigiTexDuplicate = true;
										}
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
				update.set("lastModifiedDate", new Date());
				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

				Application dataFullApp = mongoTemplate.findOne(query, Application.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp","reference_id", referenceId,
								"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

				Report report = new Report();
				report.setQuickLeadId(dataFullApp.getQuickLeadId());
				report.setApplicationId(data.getApplicationId());
				report.setFunction("DIGITEXX_COMMENT");
				report.setStatus("RETURNED");
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());
				mongoTemplate.save(report);
			}

			if (responseCommnentToDigiTex){
				if (responseCommnentToDigiTexDuplicate) {
					ArrayNode documents = mapper.createArrayNode();
					boolean checkIdCard = false;
					boolean checkHousehold = false;
					for (Document item: documentCommnet) {
						ObjectNode doc = mapper.createObjectNode();

						if (item.getType().toUpperCase().equals("TPF_ID Card".toUpperCase())){
							if (!checkIdCard) {
								if (item.getComment() != null) {
									doc.put("documentComment", item.getComment());
								}else{
									doc.put("documentComment", "");
								}
								if (item.getLink() != null) {
									doc.put("documentId", item.getLink().getUrlPartner());
									documents.add(doc);
								}

								checkIdCard = true;
							}
						}else if (item.getType().toUpperCase().equals("TPF_Notarization of ID card".toUpperCase())){
							if (!checkIdCard) {
								if (item.getComment() != null) {
									doc.put("documentComment", item.getComment());
								}else{
									doc.put("documentComment", "");
								}
								if (item.getLink() != null) {
									doc.put("documentId", item.getLink().getUrlPartner());
									documents.add(doc);
								}

								checkIdCard = true;
							}
						}if (item.getType().toUpperCase().equals("TPF_Family Book".toUpperCase())){
							if (!checkHousehold) {
								if (item.getComment() != null) {
									doc.put("documentComment", item.getComment());
								}else{
									doc.put("documentComment", "");
								}
								if (item.getLink() != null) {
									doc.put("documentId", item.getLink().getUrlPartner());
									documents.add(doc);
								}

								checkHousehold = true;
							}
						}else if (item.getType().toUpperCase().equals("TPF_Notarization of Family Book".toUpperCase())){
							if (!checkHousehold) {
								if (item.getComment() != null) {
									doc.put("documentComment", item.getComment());
								}else{
									doc.put("documentComment", "");
								}
								if (item.getLink() != null) {
									doc.put("documentId", item.getLink().getUrlPartner());
									documents.add(doc);
								}

								checkHousehold = true;
							}
						} else if (item.getType().toUpperCase().equals("TPF_Customer Photograph".toUpperCase())){
							if (item.getComment() != null) {
								doc.put("documentComment", item.getComment());
							}else{
								doc.put("documentComment", "");
							}
							if (item.getLink() != null) {
								doc.put("documentId", item.getLink().getUrlPartner());
								documents.add(doc);
							}
						}else if (item.getType().toUpperCase().equals("TPF_Application cum Credit Contract (ACCA)".toUpperCase())){
							if (item.getComment() != null) {
								doc.put("documentComment", item.getComment());
							}else{
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
					//				apiService.callApiDigitexx(urlDigitexResubmitCommentApi,dataSend);

					try {
						JsonNode responseDG = apiService.callApiDigitexx(urlDigitexResubmitCommentApi, dataSend);
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
					}catch (Exception ex){}

					//
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
					update.set("lastModifiedDate", new Date());
					Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					Report report = new Report();
					report.setQuickLeadId(dataFullApp.getQuickLeadId());
					report.setApplicationId(data.getApplicationId());
					report.setFunction("FICO_RETURN_COMMENT");
					report.setStatus("PROCESSING");
					report.setCreatedBy(token.path("user_name").textValue());
					report.setCreatedDate(new Date());
					mongoTemplate.save(report);
				}else{
					responseModel.setRequest_id(requestId);
					responseModel.setReference_id(referenceId);
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("1");
					responseModel.setMessage("Không thể trả thêm comment!");
					return Map.of("status", 200, "data", responseModel);
				}
			}

			if (responseCommnentFullAPPFromDigiTex){
//				Query queryUpdate = new Query();
//				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));
//				Update update = new Update();
//				update.set("status", "COMPLETED");
//				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

				Report report = new Report();
				report.setQuickLeadId(quickLeadId);
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
				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("applicationId").is(data.getApplicationId()));

				Update update = new Update();
				update.set("status", data.getStatus().toUpperCase());
				update.set("description", data.getDescription());
				update.set("lastModifiedDate", new Date());
				if (data.getStatus().toUpperCase().equals("MANUALLY".toUpperCase())){
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
				report.setStatus(data.getStatus().toUpperCase());
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());
				if(dataFullApp != null){
					report.setPartnerId(dataFullApp.getPartnerId());
					report.setPartnerName(dataFullApp.getPartnerName());
				}
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

	public Map<String, Object> oldQuickLead(JsonNode request, JsonNode token) {
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

					rabbitMQService.send("tpf-service-automation",
							Map.of("func", "quickLeadApp","body",
									appData.get(0)));

				}else {
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

					Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
					if (resultUpdate == null){
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("applicationId is exist!");

						return Map.of("status", 200, "data", responseModel);
					}

//				--automation QuickLead--
					Query queryGetApp = new Query();
					queryGetApp.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
					List<Application> appData = mongoTemplate.find(queryGetApp, Application.class);

					rabbitMQService.send("tpf-service-app",
							Map.of("func", "createApp", "reference_id", referenceId,"body", convertService.toAppDisplay(appData.get(0))));

					rabbitMQService.send("tpf-service-automation",
							Map.of("func", "quickLeadApp", "body",
									appData.get(0)));

					Report report = new Report();
					report.setQuickLeadId(data.getQuickLeadId());
					report.setApplicationId(request.path("body").path("applicationId").textValue());
					report.setFunction("QUICKLEAD");
					report.setStatus("NEW");
					report.setCreatedBy(token.path("user_name").textValue());
					report.setCreatedDate(new Date());
					mongoTemplate.save(report);

					rabbitMQService.send("tpf-service-automation",
							Map.of("func", "quickLeadApp", "body",
									appData.get(0)));
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
							Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
						}
					}

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
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);

	}

	public Map<String, Object> oldUpdateAutomation(JsonNode request, JsonNode token) {
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
				catch (Exception e) {
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
					}else {
						Update update = new Update();
						update.set("applicationId", request.path("body").path("applicationId").textValue());
						update.set("status", "PROCESSING");
						update.set("lastModifiedDate", new Date());
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
						apiService.callApiDigitexx(urlDigitexCmInfoApi, dataSend);
//					String resultDG =  apiService.callApiDigitexx(urlDigitexCmInfoApi,dataSend);

//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//					headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//					HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(Map.of("customer-name", customerName, "id-card-no", idCardNo,
//							"application-id", applicationId, "document-ids", inputQuery)), headers);
//					ResponseEntity<?> res = restTemplate.postForEntity(urlDigitexCmInfoApi, entity, Object.class);
//					JsonNode body = mapper.valueToTree(res.getBody());

						Application dataFullApp = mongoTemplate.findOne(query, Application.class);
						rabbitMQService.send("tpf-service-app",
								Map.of("func", "updateApp", "reference_id", referenceId,
										"param", Map.of("project", "dataentry", "id", dataFullApp.getId()), "body", convertService.toAppDisplay(dataFullApp)));

						Report report = new Report();
						report.setQuickLeadId(request.path("body").path("quickLeadId").textValue());
						report.setApplicationId(request.path("body").path("applicationId").textValue());
						report.setFunction("QUICKLEAD");
						report.setStatus("PROCESSING");
						report.setCreatedBy("AUTOMATION");
						report.setCreatedDate(new Date());
						mongoTemplate.save(report);
					}
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
					update.set("lastModifiedDate", new Date());
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
			responseModel.setMessage(e.toString());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> oldUpdateFullApp(JsonNode request, JsonNode token) {
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
					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), JsonNode.class);
					apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);
//					String resultDG = apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);

//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//					headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//					HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), headers);
//					ResponseEntity<?> res = restTemplate.postForEntity(urlDigitexFeedbackApi, entity, Object.class);
//					JsonNode body = mapper.valueToTree(res.getBody());

				}else{
					errors = request.path("body").path("stage").textValue();

					if (errors.equals("LOGIN FINONE")){
						try {
							Query queryLogin = new Query();
							queryLogin.addCriteria(Criteria.where("applicationId").is(applicationId));
							Application dataFullApp = mongoTemplate.findOne(queryLogin, Application.class);
							rabbitMQService.send("tpf-service-automation",
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
					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					//fico gui comment

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "failed",
							"commend-id", commentId, "errors", errors)), JsonNode.class);
					apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);

//					String resultDG = apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);


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

	public Map<String, Object> oldUpdateAppError(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String applicationId = "";
		String commentId = UUID.randomUUID().toString().substring(0,10);
		String referenceId = UUID.randomUUID().toString();
		String errors = "";
		try{
			applicationId = request.path("body").path("applicationId").textValue();
			errors = request.path("body").path("stage").textValue();
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").textValue()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
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
					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), JsonNode.class);
					apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);
//					String resultDG = apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);

//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//					headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//					HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "success")), headers);
//					ResponseEntity<?> res = restTemplate.postForEntity(urlDigitexFeedbackApi, entity, Object.class);
//					JsonNode body = mapper.valueToTree(res.getBody());

				}else{
					errors = request.path("body").path("stage").textValue();

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
									dataUpdateSendAuto.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
									break;
								}
							}
							rabbitMQService.send("tpf-service-automation",
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
					mongoTemplate.save(report);

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

					//fico gui comment

					JsonNode dataSend = mapper.convertValue(mapper.writeValueAsString(Map.of("application-id", applicationId, "status", "failed",
							"commend-id", commentId, "errors", errors)), JsonNode.class);
					apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);
//					String resultDG = apiService.callApiDigitexx(urlDigitexFeedbackApi,dataSend);

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
			AggregationOperation group = Aggregation.group("applicationId", "createdDate", "status", "quickLeadId", "description", "function", "createdBy", "applications");
			AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "applicationId", "createdDate");
//			AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "applicationId").and(Sort.Direction.DESC, "createdDate");
			AggregationOperation project = Aggregation.project().andExpression("_id.applicationId").as("applicationId").andExpression("_id.createdDate").as("createdDate");//.andExpression("createdDate").as("createdDate2");
			//AggregationOperation limit = Aggregation.limit(Constants.BOARD_TOP_LIMIT);
			Aggregation aggregation = Aggregation.newAggregation(match1, lookupOperation, group, sort /*,  project/*, limit*/);
			AggregationResults<Report> results = mongoTemplate.aggregate(aggregation, Report.class, Report.class);

			List<Report> listData = results.getMappedResults();

			String appId = "";
			Date startDate = new Date();
			Date finishDate = new Date();
			for (Report item:listData) {
				try {
					try{
						if (item.getApplications().size() > 0) {
							if (item.getApplications().get(0).getApplicationInformation() == null) {
								item.setFullName(item.getApplications().get(0).getQuickLead().getFirstName() + " " + item.getApplications().get(0).getQuickLead().getLastName());
								item.setIdentificationNumber(item.getApplications().get(0).getQuickLead().getIdentificationNumber());
							} else {
								if (item.getApplications().get(0).getApplicationInformation().getPersonalInformation() != null) {
									item.setFullName(item.getApplications().get(0).getApplicationInformation().getPersonalInformation().getPersonalInfo().getFullName());
									item.setIdentificationNumber(item.getApplications().get(0).getApplicationInformation().getPersonalInformation().getIdentifications().get(0).getIdentificationNumber());
								}
							}
						}
					}
					catch (Exception exx) {
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
			Aggregation aggregation = Aggregation.newAggregation(match1, group, sort, project/*, limit*/);
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
		String[] COLUMNs = {"Seq", "App no.", "Action", "Create Date", "Create By", "Status", "Full Name", "ID", "Duration(Minutes)"};
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
				row.createCell(1).setCellValue(item.getApplicationId());
				row.createCell(2).setCellValue(item.getFunction());

				Cell cell = row.createCell(3);
				cell.setCellValue(item.getCreatedDate());
				cell.setCellStyle(cellStyle);

//				row.createCell(2).setCellValue(item.getCreatedDate());
				row.createCell(4).setCellValue(item.getCreatedBy());
				row.createCell(5).setCellValue(item.getStatus());
				row.createCell(6).setCellValue(item.getFullName());
				row.createCell(7).setCellValue(item.getIdentificationNumber());
				row.createCell(8).setCellValue(item.getDuration());

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
//						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
						apiService.callApiDigitexx(feedbackApi, dataSend);
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

					if (errors.equals("LOGIN FINONE")){
						try {
							Query queryLogin = new Query();
							queryLogin.addCriteria(Criteria.where("applicationId").is(applicationId));
							Application dataFullApp = mongoTemplate.findOne(queryLogin, Application.class);
							rabbitMQService.send("tpf-service-automation",
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
//						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
						apiService.callApiDigitexx(feedbackApi, dataSend);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return Map.of("result_code", 3, "message","Not get token saigon-bpo");
						}
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
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

					rabbitMQService.send("tpf-service-automation",
							Map.of("func", "quickLeadApp","body",
									appData.get(0)));

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

					update.set("partnerId", data.getPartnerId());
					Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

					if (resultUpdate == null){
						responseModel.setRequest_id(requestId);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code("1");
						responseModel.setMessage("applicationId is exist!");
						return Map.of("status", 200, "data", responseModel);
					}

//				--automation QuickLead--
					Query queryGetApp = new Query();
					queryGetApp.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
					List<Application> appData = mongoTemplate.find(queryGetApp, Application.class);

					rabbitMQService.send("tpf-service-app",
							Map.of("func", "createApp", "reference_id", referenceId,"body", convertService.toAppDisplay(appData.get(0))));

					rabbitMQService.send("tpf-service-automation",
							Map.of("func", "quickLeadApp", "body",
									appData.get(0)));

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
			if (checkExist.size() <= 0) {
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
				} catch (Exception ex) {
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

							requestCommnentFromDigiTex = true;
						} else {
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
								if (checkCommentExist.get(0).getQuickLead().getDocumentsComment() != null) {
									dataUpdate.setDocuments(checkCommentExist.get(0).getQuickLead().getDocumentsComment());
								}
								dataUpdate.setStage(stageAuto);
								dataUpdate.setError(errorAuto);
								rabbitMQService.send("tpf-service-automation",
										Map.of("func", "updateAppError", "body", dataUpdate));
							}

						} else {//fico tra comment
							try {
								if (item.getResponse().getComment() == null || item.getResponse().getComment().equals("")) {
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
										if (itemComment.getResponse() == null) {
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

											responseCommnentToDigiTexDuplicate = true;
										}
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
				report.setCreatedBy(token.path("user_name").textValue());
				report.setCreatedDate(new Date());
				if (dataFullApp != null) {
					report.setPartnerId(dataFullApp.getPartnerId());
					report.setPartnerName(dataFullApp.getPartnerName());
				}
				mongoTemplate.save(report);
			}

			if (responseCommnentToDigiTex) {
				if (responseCommnentToDigiTexDuplicate) {
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
//							apiService.callApiPartner(resubmitCommentApi, dataSend, tokenPartner, partnerId);
							apiService.callApiDigitexx(resubmitCommentApi, dataSend);
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
					report.setCreatedBy(token.path("user_name").textValue());
					report.setCreatedDate(new Date());

					report.setPartnerId(partnerId);
					report.setPartnerName(partnerName);

					mongoTemplate.save(report);
				} else {
					responseModel.setRequest_id(requestId);
					responseModel.setReference_id(referenceId);
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("1");
					responseModel.setMessage("Không thể trả thêm comment!");
					return Map.of("status", 200, "data", responseModel);
				}
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
					if(StringUtils.isEmpty(partner.get("data"))){
						return Map.of("result_code", 3, "message","Not found partner");
					}
					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					String cmInfoApi = (String) mapper.convertValue(url, Map.class).get("cmInfoApi");

					if(partnerId.equals("1")){
						String tokenPartner = (String) (mapper.convertValue(partner.get("data"), Map.class).get("token"));
//						apiService.callApiPartner(cmInfoApi, dataSend, tokenPartner, partnerId);
						apiService.callApiDigitexx(cmInfoApi, dataSend);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return Map.of("result_code", 3, "message","Not get token saigon-bpo");
						}
						apiService.callApiPartner(cmInfoApi, dataSend, tokenPartner, partnerId);
					}

					Application dataFullApp = mongoTemplate.findOne(query, Application.class);
					rabbitMQService.send("tpf-service-app",
							Map.of("func", "updateApp","reference_id", referenceId,
									"param", Map.of("project", "dataentry", "id", dataFullApp.getId()),"body", convertService.toAppDisplay(dataFullApp)));

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
			responseModel.setMessage(e.toString());
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
//						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
						apiService.callApiDigitexx(feedbackApi, dataSend);
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
									dataUpdateSendAuto.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
									break;
								}
							}
							rabbitMQService.send("tpf-service-automation",
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
//						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
						apiService.callApiDigitexx(feedbackApi, dataSend);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
						String tokenPartner = apiService.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return Map.of("result_code", 3, "message","Not get token saigon-bpo");
						}
						apiService.callApiPartner(feedbackApi, dataSend, tokenPartner, partnerId);
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
}