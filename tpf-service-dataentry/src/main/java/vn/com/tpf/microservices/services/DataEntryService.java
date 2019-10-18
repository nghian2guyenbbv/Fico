package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.operation.GroupOperation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
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

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Service
public class DataEntryService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

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



//			PGPHelper pgpHelper4 = new PGPHelper(PGPInfo.preshareKey_4,PGPInfo.publicKey_6,PGPInfo.privateKey_4);
//			ByteArrayOutputStream desStrea4 = new ByteArrayOutputStream();
//			pgpHelper4.decryptAndVerifySignature(request.path("body").path("data").textValue().getBytes(), desStrea4);

//			responseModel.setRequest_id(requestId);
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code("0");
//			responseModel.setData(desStrea4.toString());


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




////			PGPHelper pgpHelper3 = new PGPHelper(PGPInfo.preshareKey_6,PGPInfo.publicKey_4,PGPInfo.privateKey_6);
////			ByteArrayOutputStream encStream3 = new ByteArrayOutputStream();
////			pgpHelper3.encryptAndSign(request.path("body").path("data").toString().getBytes(), encStream3);
//
//
////			PGPHelper pgpHelper4 = new PGPHelper(PGPInfo.preshareKey_4,PGPInfo.publicKey_6,PGPInfo.privateKey_4);
////			ByteArrayOutputStream desStrea4 = new ByteArrayOutputStream();
////			pgpHelper4.decryptAndVerifySignature(encStream3.toString().getBytes(), desStrea4);
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
								Map.of("func", "fullInfoApp", "token",
										String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body", dataFullApp));

                        Report report = new Report();
                        report.setApplicationId(data.getApplicationId());
                        report.setFunction("SENDAPP");
                        report.setStatus("COMPLETED");
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

        boolean responseCommnentFullAPPFromDigiTex = false;
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
										Update update = new Update();
										update.set("comment.$.response", item.getResponse());
										Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);
									}
								}
							}
                            responseCommnentFullAPPFromDigiTex = true;
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
                Report report = new Report();
                report.setApplicationId(data.getApplicationId());
                report.setFunction("COMMENT");
                report.setStatus("RETURNED");
                report.setCreatedBy(token.path("user_name").textValue());
                report.setCreatedDate(new Date());
                mongoTemplate.save(report);
            }

            if (responseCommnentToDigiTex){
                Report report = new Report();
                report.setApplicationId(data.getApplicationId());
                report.setFunction("COMMENT");
                report.setStatus("PROCESSING");
                report.setCreatedBy(token.path("user_name").textValue());
                report.setCreatedDate(new Date());
                mongoTemplate.save(report);
            }

            if (responseCommnentFullAPPFromDigiTex){
                Report report = new Report();
                report.setApplicationId(data.getApplicationId());
                report.setFunction("COMMENT");
                report.setStatus("COMPLETED");
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
			responseModel.setMessage(e.getMessage());
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

				Application resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, Application.class);

				Report report = new Report();
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

				rabbitMQService.send("tpf-service-automation",
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
						//tesst
						item.setUrlid("001");
					}
				}
				//end

				QuickLead quickLead = new QuickLead();
				quickLead.setDocuments(dataUpload);

				Application app = new Application();
				app.setQuickLeadId(quickLeadId.toString());
				app.setQuickLead(quickLead);
				app.setUserName(token.path("user_name").textValue());
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
			log.info("ReferenceId : "+ referenceId + "Error: " + e);
			responseModel.setRequest_id(requestId);
			responseModel.setReference_id(referenceId);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code("1");
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);

//		return Map.of("status", 200, "data",
//				Map.of("status", apiService.uploadFile(request).equals("pass") ? "passed" : "rejected"));
	}

	public Map<String, Object> updateAutomation(JsonNode request, JsonNode token) {
		ResponseModel responseModel = new ResponseModel();
		String requestId = request.path("body").path("request_id").textValue();
		String referenceId = UUID.randomUUID().toString();
		try{
			Query query = new Query();
			query.addCriteria(Criteria.where("quickLeadId").is(request.path("body").path("quickLeadId").asText()));
			List<Application> checkExist = mongoTemplate.find(query, Application.class);
			if (checkExist.size() > 0){
				Update update = new Update();
				update.set("applicationId", request.path("body").path("applicationId").asText());
				Application resultUpdatetest = mongoTemplate.findAndModify(query, update, Application.class);

                Report report = new Report();
                report.setApplicationId(request.path("body").path("applicationId").asText());
                report.setFunction("QUICKLEAD");
                report.setStatus("PROCESSING");
                report.setCreatedBy("AUTOMATION");
                report.setCreatedDate(new Date());
                mongoTemplate.save(report);

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

                Report report = new Report();
                report.setApplicationId(request.path("body").path("applicationId").asText());
                report.setFunction("COMMENT");
                report.setStatus("RESPONSED");
                report.setCreatedBy("AUTOMATION");
                report.setCreatedDate(new Date());
                mongoTemplate.save(report);

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
            Timestamp fromDate = Timestamp.valueOf(request.path("body").path("data").path("fromDate").textValue() + " 00:00:00");
            Timestamp toDate = Timestamp.valueOf(request.path("body").path("data").path("toDate").textValue() + " 23:23:59");

            Query query = new Query();
            query.addCriteria(Criteria.where("createdDate").gte(fromDate).lte(toDate));

            List<Report> listData = mongoTemplate.find(query, Report.class);
            // export excel
//			Workbook workbook = new XSSFWorkbook();
//			Sheet sheet = workbook.createSheet("TATReport");
//			sheet.setColumnWidth(0, 6000);
//			sheet.setColumnWidth(1, 4000);
//
//			Row header = sheet.createRow(0);
//
//			CellStyle headerStyle = workbook.createCellStyle();
////			headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
////			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//			XSSFFont font = ((XSSFWorkbook) workbook).createFont();
//			font.setFontName("Arial");
////			font.setFontHeightInPoints((short) 16);
////			font.setBold(true);
//			headerStyle.setFont(font);
//
//			Cell headerCell = header.createCell(0);
//			headerCell.setCellValue("Seq");
//			headerCell.setCellStyle(headerStyle);
//
//			headerCell = header.createCell(1);
//			headerCell.setCellValue("App no.");
//			headerCell.setCellStyle(headerStyle);
//
//			headerCell = header.createCell(2);
//			headerCell.setCellValue("Create Date");
//			headerCell.setCellStyle(headerStyle);
//
//			headerCell = header.createCell(3);
//			headerCell.setCellValue("Create By");
//			headerCell.setCellStyle(headerStyle);
//
//			headerCell = header.createCell(4);
//			headerCell.setCellValue("Status");
//			headerCell.setCellStyle(headerStyle);
//
//			CellStyle style = workbook.createCellStyle();
//			style.setWrapText(true);
//
//			int i = 0;
//			Row row;
//			for (Report item : listData) {
//				i = i + 1;
//				row = sheet.createRow(i);
//				Cell cell = row.createCell(0);
//				cell.setCellValue(i);
//				cell.setCellStyle(style);
//
//				cell = row.createCell(1);
//				cell.setCellValue(item.getApplicationId());
//				cell.setCellStyle(style);
//
//				cell = row.createCell(2);
//				cell.setCellValue(item.getCreatedDate());
//				cell.setCellStyle(style);
//
//				cell = row.createCell(3);
//				cell.setCellValue(item.getCreatedBy());
//				cell.setCellStyle(style);
//
//				cell = row.createCell(4);
//				cell.setCellValue(item.getStatus());
//				cell.setCellStyle(style);
//			}
//
//			File currDir = new File(".");
//			String path = currDir.getAbsolutePath();
//			String fileLocation = path.substring(0, path.length() - 1) +"temp"+ new Date().getTime()+".xlsx";
//
//			FileOutputStream outputStream = new FileOutputStream(fileLocation);
//			workbook.write(outputStream);
//			workbook.close();

//			Base64.getEncoder().encodeToString(item.getBytes())
			in = tatReportToExcel(listData);

//			Base64.getEncoder().encodeToString(in.readAllBytes());

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
            Timestamp fromDate = Timestamp.valueOf(request.path("body").path("data").path("fromDate").textValue() + " 00:00:00");
            Timestamp toDate = Timestamp.valueOf(request.path("body").path("data").path("toDate").textValue() + " 23:23:59");

            ArrayList<String> inputQuery = new ArrayList<String>();
            inputQuery.add("COMPLETED");
            inputQuery.add("RETURNED");
            inputQuery.add("RESPONSED");
			inputQuery.add("PROCESSING");

            AggregationOperation match1 = Aggregation.match(Criteria.where("createdDate").gte(fromDate).lte(toDate).and("status").in(inputQuery));
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

}