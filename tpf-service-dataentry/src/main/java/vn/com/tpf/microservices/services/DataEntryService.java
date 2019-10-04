package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.models.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class DataEntryService {

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

	public Map<String, Object> addProduct(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
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
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getProductByName(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
		UUID quickLeadId = UUID.randomUUID();
		try{
//			Application app = new Application();
//			quickLeadId = UUID.randomUUID();
//			app.setQuickLeadId(quickLeadId);
//			mongoTemplate.save(app);
//			responseModel.setRequest_id(requestId);
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code("0");
//			responseModel.setData(quickLeadId);
//
//
//			responseModel.setData(quickLeadId);


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
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());


			responseModel.setData(quickLeadId);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getAll(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
		try{
			List<Application> checkExist = mongoTemplate.findAll(Application.class);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("0");
			responseModel.setData(checkExist);
		}
		catch (Exception e) {
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getByAppId(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
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
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getAddress(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
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
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getBranch(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
		try{
			List<Branch> resultData = mongoTemplate.findAll(Branch.class);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("0");
			responseModel.setData(resultData);
		}
		catch (Exception e) {
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getDetail(JsonNode request) {
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
			return Map.of("status", 200, "data", Map.of("message", "Not Found"));
		}
		return Map.of("status", 200, "data", app);
	}

	public Map<String, Object> firstCheck(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
		try{
//			responseModel.setRequest_id(requestId);
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code("0");
//			responseModel.setData(Map.of("first_check_result", "pass"));

			String resultFirstCheck = apiService.firstCheck(request);
			if (resultFirstCheck.equals("pass")){
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}else{
				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("1");
				responseModel.setMessage(resultFirstCheck);
			}
		}
		catch (Exception e) {
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> sendApp(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
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
						if (dataFullApp.getQuickLead().getDocumentsComment().size() > 0){
							dataFullApp.setDocuments(dataFullApp.getQuickLead().getDocumentsComment());
						}
						rabbitMQService.send("tpf-service-dataentry-automation",
								Map.of("func", "fullInfoApp", "token",
										String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body", dataFullApp));

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



//					responseModel.setRequest_id(requestId);
//					responseModel.setReference_id(UUID.randomUUID().toString());
//					responseModel.setDate_time(new Timestamp(new Date().getTime()));
//					responseModel.setResult_code("1");
//					responseModel.setMessage("applicationId invalid.");
				}else {
					responseModel.setRequest_id(requestId);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("1");
					responseModel.setMessage("applicationId not send again.");


//					//Create ValidatorFactory which returns validator
//					ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//					//It validates bean instances
//					Validator validator = factory.getValidator();
//					//Validate bean
//					Set<ConstraintViolation<Application>> constraintViolations = validator.validate(data);
//
//					if (constraintViolations.size() <= 0 || constraintViolations.isEmpty()){
//						Update update = new Update();
//						update.set("applicationInformation", data.getApplicationInformation());
//						update.set("loanDetails", data.getLoanDetails());
//						update.set("references", data.getReferences());
//						update.set("dynamicForm", data.getDynamicForm());
//						Application resultUpdate = mongoTemplate.findAndModify(query, update, Application.class);
//
////						--automation fullapp--
//						List<Application> dataFullApp = mongoTemplate.find(query, Application.class);
//						rabbitMQService.send("tpf-service-dataentry-automation",
//								Map.of("func", "fullInfoApp", "token",
//										String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body", dataFullApp.get(0)));
//
//						responseModel.setRequest_id(requestId);
//						responseModel.setReference_id(UUID.randomUUID().toString());
//						responseModel.setDate_time(new Timestamp(new Date().getTime()));
//						responseModel.setResult_code("0");
//					}else{
//						HashMap<String,String> map = new HashMap<String,String>();
//						for (ConstraintViolation<Application> violation : constraintViolations) {
//							map.put(violation.getPropertyPath().toString(), violation.getMessage());
//						}
//						responseModel.setRequest_id(requestId);
//						responseModel.setReference_id(UUID.randomUUID().toString());
//						responseModel.setDate_time(new Timestamp(new Date().getTime()));
//						responseModel.setResult_code("1");
//						responseModel.setMessage(map.toString());
//					}
				}
			}
		}
		catch (Exception e) {
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.toString());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> updateApp(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
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
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> commentApp(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
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
				for (CommentModel item : data.getComment()) {
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
							update.push("comment", item);
							Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, update, Application.class);
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
										Update update = new Update();
										update.set("comment.$.response", item.getResponse());
										Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
									}
								}
							}
						}else{//fico tra comment
							List<CommentModel> listComment = checkCommentExist.get(0).getComment();
							for (CommentModel itemComment : listComment) {
								if (itemComment.getCommentId().equals(item.getCommentId())) {
									if (itemComment.getResponse() == null) {
										if (item.getResponse().getDocuments().size() > 0){
											for (Document itemCommentFico : item.getResponse().getDocuments()) {
												Link link = new Link();
												link.setUrlFico(itemCommentFico.getFilename());
												link.setUrlPartner(itemCommentFico.getUrlId());
												itemCommentFico.setLink(link);
											}
										}

										Update update = new Update();
										update.set("comment.$.response", item.getResponse());
										Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
									}
								}
							}
						}
					}
				}

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}
		}
		catch (Exception e) {
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> cancelApp(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
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

				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
			}
		}
		catch (Exception e) {
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> quickLead(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
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
				Query queryUpdate = new Query();
				queryUpdate.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));

				Update update = new Update();
				update.set("quickLead.quickLeadId", data.getQuickLeadId());
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

				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

//				--automation QuickLead--
				Query queryGetApp = new Query();
				queryGetApp.addCriteria(Criteria.where("quickLeadId").is(data.getQuickLeadId()));
				List<Application> appData = mongoTemplate.find(queryGetApp, Application.class);

				rabbitMQService.send("tpf-service-dataentry-automation",
						Map.of("func", "quickLeadApp", "token",
								String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body",
								appData.get(0)));

				rabbitMQService.send("tpf-service-app",
				Map.of("func", "createApp", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"param", Map.of("project", "momo"), "body", convertService.toAppDisplay(appData.get(0))));

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
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> uploadFile(JsonNode request) throws Exception {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
		try{
			UUID quickLeadId = UUID.randomUUID();
			List<QLDocument> dataUpload = new ArrayList<>();
			Assert.notNull(request.get("body"), "no body");
			if (request.get("appId").isNull()){
				try {
					dataUpload = mapper.readValue(request.path("body").toString(), new TypeReference<List<QLDocument>>() {});
				} catch (IOException e) {
					e.printStackTrace();
				}

				//begin update urlid
				for (QLDocument item : dataUpload) {
					List<QLDocument> listDocumentPartner = mapper.readValue(request.path("body").toString(), new TypeReference<List<QLDocument>>() {});
					for (QLDocument item2 : listDocumentPartner) {
						if (item.getOriginalname().equals(item2.getOriginalname())){
							item.setUrlid(item2.getUrlid());
						}
					}
				}
				//end

				QuickLead quickLead = new QuickLead();
				quickLead.setDocuments(dataUpload);

				Application app = new Application();
				app.setQuickLeadId(quickLeadId);
				app.setQuickLead(quickLead);
				mongoTemplate.save(app);

				Map<String, Object> responseUI = new HashMap<>();
				responseUI.put("quickLeadId", quickLeadId);
				responseUI.put("documents", dataUpload);

//				responseModel.setRequest_id(requestId);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code("0");
				responseModel.setData(responseUI);
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

						//begin update urlid
						List<QLDocument> listDocumentPartner = mapper.readValue(request.path("body").toString(), new TypeReference<List<QLDocument>>() {});
						for (QLDocument item2 : listDocumentPartner) {
							if (item.getOriginalname().equals(item2.getOriginalname())){
								item.setUrlid(item2.getUrlid());
							}
						}
						//end

						if (checkCommentExist.size() <= 0){
							Query queryAddComment = new Query();
							queryAddComment.addCriteria(Criteria.where("applicationId").is(request.get("appId").asText()));

							Update update = new Update();
							update.addToSet("quickLead.documentsComment", item);
							Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, update, FindAndModifyOptions.options().upsert(true), Application.class);
						}else {
							Update update = new Update();
							update.set("quickLead.documentsComment.$.filename", item.getFilename());
							update.set("quickLead.documentsComment.$.urlId", item.getUrlid());
							Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
						}
					}


					Map<String, Object> responseUI = new HashMap<>();
					responseUI.put("quickLeadId", quickLeadId);
					responseUI.put("applicationd", request.get("appId").asText());
					responseUI.put("documents", dataUpload);

					//				responseModel.setRequest_id(requestId);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("0");
					responseModel.setData(responseUI);
				}else{
					//				responseModel.setRequest_id(requestId);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code("1");
					responseModel.setMessage("AppId not exist.");
				}
			}
		}
		catch (Exception e) {
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);

//		return Map.of("status", 200, "data",
//				Map.of("status", apiService.uploadFile(request).equals("pass") ? "passed" : "rejected"));
	}

	public Map<String, Object> updateAutomation(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
		try{
			Query query = new Query();
			query.addCriteria(Criteria.where("quickLeadId").is(UUID.fromString(request.path("body").path("quickLeadId").asText())));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				Update update = new Update();
				update.set("applicationId", request.path("body").path("applicationId").asText());
				Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

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
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> updateFullApp(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
		try{
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").asText()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				Update update = new Update();
				update.set("status", request.path("body").path("status").asText());
				update.set("description", request.path("body").path("description").asText());
				Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

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
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> updateAppError(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = null;
		String urlCommentDigiTex = "http://10.131.21.137:52233/sale_page/api_management/early_check/";
		try{
			Query query = new Query();
			query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").asText()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				Update update = new Update();
				update.set("status", request.path("body").path("status").asText());
				update.set("description", request.path("body").path("description").asText());
				update.set("stage", request.path("body").path("stage").asText());
				Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

				//save comment
				CommentModel commentModel = new CommentModel();
				commentModel.setCommentId(UUID.randomUUID().toString().substring(0,10));
				commentModel.setType("FICO");
				commentModel.setCode("FICO_ERR");
				commentModel.setState(request.path("body").path("stage").asText());
				commentModel.setRequest(request.path("body").path("description").asText());

				Query queryAddComment = new Query();
				queryAddComment.addCriteria(Criteria.where("applicationId").is(request.path("body").path("applicationId").asText()));
				Update updateComment = new Update();
				updateComment.push("comment", commentModel);
				Application resultUpdate = mongoTemplate.findAndModify(queryAddComment, updateComment, Application.class);

				//fico gui comment
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//				HttpEntity<?> entity = new HttpEntity<>(request.path("body").path("data"), headers);
//				ResponseEntity<?> res = restTemplate.postForEntity(urlCommentDigiTex, entity, Object.class);
//				JsonNode body = mapper.valueToTree(res.getBody());


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
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

}