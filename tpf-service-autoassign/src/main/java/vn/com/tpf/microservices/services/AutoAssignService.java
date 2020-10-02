package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.dao.*;
import vn.com.tpf.microservices.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.sound.midi.Sequence;
import java.net.URI;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AutoAssignService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${security.oauth2.resource.token-info-uri}")
	private String tokenUri;

	@Value("${security.oauth2.client.client-id}")
	private String clientId;

	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private VendorDAO vendorDAO;

	@Autowired
	private AutoAssignConfigureDAO autoAssignConfigureDAO;

	@Autowired
	private AutoAssignConfigureHistoryDAO autoAssignConfigureHistoryDAO;

	@Autowired
	private AutoAssignConfigureApplicationDAO autoAssignConfigureApplicationDAO;

	@Autowired
	private AutoAssignLogDAO autoAssignLogDAO;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired
	private AutoAssignConfigureSchemeDAO autoAssignConfigureSchemeDAO;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	public Map<String, Object> getVendor() {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			List<Vendor> getAllVendor = new ArrayList<>();

			getAllVendor = vendorDAO.findAll();

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setData(getAllVendor);

		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getConfigInDay(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			List<AutoAssignConfigure> resultConfigInDay = new ArrayList<>();
			resultConfigInDay = autoAssignConfigureDAO.findConfigureByCreatedDate(request.path("data").path("searchDay").textValue());

			if(resultConfigInDay.size()==0)
			{
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("Không có config");

				return Map.of("status", 200, "data", responseModel);
			}

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setMessage("Thành công");
			responseModel.setData(resultConfigInDay);
		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

	@Scheduled(cron="0 0 0 * * *", zone="Asia/Saigon")
	public void createdConfigureAuto() {
		try{
			try{
				List<AutoAssignConfigure> inputDataInsert = new ArrayList<>();
				DateTimeFormatter formatterParseInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				List<AutoAssignConfigure> resultConfigurePreOne = autoAssignConfigureDAO.findConfigureByCreatedDate(formatterParseInput.format(LocalDate.now().plusDays(-1)));
				List<AutoAssignConfigure> resultConfigure = autoAssignConfigureDAO.findConfigureByCreatedDate(formatterParseInput.format(LocalDate.now()));

				if (resultConfigure.size() <= 0) {
					for (AutoAssignConfigure item : resultConfigurePreOne) {

						AutoAssignConfigure createAssign = new AutoAssignConfigure();
						createAssign.setCreatedDate(new Timestamp(new Date().getTime()));
						createAssign.setQuote(new Timestamp(new Date().getTime()));
						createAssign.setTotalQuanlity(item.getTotalQuanlity());
						createAssign.setActualTotalQuanlity(item.getActualTotalQuanlity());

						//check dieu kien ngay dau tien cua thang, reset ActualTotalQuanlity
						LocalDate localDate=LocalDate.now();
						if(localDate.getDayOfMonth()==1)
						{
							createAssign.setActualTotalQuanlity(0);
							createAssign.setQuanlityInDay(item.getQuanlityInDay());
						}else
						{
							if (item.getActualTotalQuanlity() < item.getTotalQuanlity()){
								if (item.getQuanlityInDay() < (item.getTotalQuanlity() - item.getActualTotalQuanlity())){
									createAssign.setQuanlityInDay(item.getQuanlityInDay());
								}else{
									createAssign.setQuanlityInDay(item.getTotalQuanlity() - item.getActualTotalQuanlity());
								}
							}else{
								createAssign.setQuanlityInDay(0);
							}
						}

						createAssign.setActualQuanlityInDay(0);
						createAssign.setVendorId(item.getVendorId());
						createAssign.setVendorName(item.getVendorName());
						createAssign.setPriority(item.getPriority());
						createAssign.setAssign(true);
						createAssign.setCreatedBy("system");

						inputDataInsert.add(createAssign);
					}
					List<AutoAssignConfigure> result = autoAssignConfigureDAO.saveAll(inputDataInsert);

					//ghi log
					AutoAssignConfigureHistory autoAssignConfigureHistory = new AutoAssignConfigureHistory();
					autoAssignConfigureHistory.setCreatedDate(new Timestamp(new Date().getTime()));
					autoAssignConfigureHistory.setQuote(new Timestamp(new Date().getTime()));
					autoAssignConfigureHistory.setData( mapper.writeValueAsString(inputDataInsert));
					autoAssignConfigureHistory.setCreatedBy("system");
					AutoAssignConfigureHistory resultHistory = autoAssignConfigureHistoryDAO.save(autoAssignConfigureHistory);
				}
				int result = autoAssignConfigureDAO.updateSeq();
			}
			catch (Exception e) {
				log.info("Error_Insert: " + e.toString());
			}
			return;
		}
		catch (Exception e) {
			log.info("Error_Insert_Auto: " + e.toString());
			return;
		}
	}

	@Scheduled(cron="${spring.cronJob.syncStatus}", zone="Asia/Saigon")
	public void updateTotalQuantityInDayFromDE() {
		String logs="updateTotalQuantityInDayFromDE: ";
		try{

			//get all app from Portal today
			DateTimeFormatter formatterParseInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			String toDay=formatterParseInput.format(LocalDate.now());
			Timestamp fromDate = Timestamp.valueOf(toDay + " 00:00:00");
			Timestamp toDate = Timestamp.valueOf(toDay + " 23:23:59");

			Criteria criteria=new Criteria();
			criteria=Criteria.where("createdDate").gte(fromDate).lte(toDate);
//			criteria.and("applicationId").ne(null);
			criteria.andOperator(new Criteria().orOperator(Criteria.where("applicationId").ne(null),Criteria.where("status").is("NEW")));

			List<Application> resultData=new ArrayList<Application>();

			MatchOperation matchOperation= Aggregation.match(criteria);
			Aggregation aggregation = Aggregation.newAggregation(matchOperation);

			AggregationResults<Application> output = mongoTemplate.aggregate(aggregation, Application.class,Application.class);
			resultData = output.getMappedResults();

			logs +=", resultData: " + resultData.size();

			if(resultData.size()>0) {
				//get configauto today
				List<AutoAssignConfigure> resultConfigure = autoAssignConfigureDAO.findConfigureByCreatedDate(formatterParseInput.format(LocalDate.now()));
				if (resultConfigure.size() > 0) {
					long total = 0;
					long diff = 0;

					for (AutoAssignConfigure autoAssignConfigure : resultConfigure) {
						if (!autoAssignConfigure.getVendorName().equals("IN-HOUSE")) {
							total = resultData.stream().filter(x -> Long.valueOf(x.getPartnerId()) == autoAssignConfigure.getVendorId()).count();
							diff = autoAssignConfigure.getActualQuanlityInDay() - total;

							logs +=",partner: " + autoAssignConfigure.getVendorId()+ ", total: " + total + ", diff: " + diff;

							autoAssignConfigure.setActualQuanlityInDay(total);
							autoAssignConfigure.setActualTotalQuanlity(autoAssignConfigure.getActualTotalQuanlity() - diff);
							autoAssignConfigureDAO.save(autoAssignConfigure);
						}
					}

//					//ghi log
//					AutoAssignConfigureHistory autoAssignConfigureHistory = new AutoAssignConfigureHistory();
//					autoAssignConfigureHistory.setCreatedDate(new Timestamp(new Date().getTime()));
//					autoAssignConfigureHistory.setQuote(new Timestamp(new Date().getTime()));
//					autoAssignConfigureHistory.setData(mapper.writeValueAsString(resultConfigure));
//					autoAssignConfigureHistory.setCreatedBy("system");
//					AutoAssignConfigureHistory resultHistory = autoAssignConfigureHistoryDAO.save(autoAssignConfigureHistory);
				}
			}
		}
		catch (Exception e) {
			logs+="; Error updateTotalQuantityInDayFromDE: " + e.toString();
		}
		finally {
			log.info(logs);
		}
	}

	@Transactional
	public Map<String, Object> createdConfigure(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			RequestModel requestModel = mapper.treeToValue(request, RequestModel.class);
			JsonNode token = checkToken(request.path("token").asText("Bearer ").split(" "));

			if (requestModel.getData() == null){
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("data null!");
				return Map.of("status", 200, "data", responseModel);
			}

			List<AutoAssignConfigure> inputRequest = requestModel.getData();
			List<AutoAssignConfigure> inputDataInsert = new ArrayList<>();
			DateTimeFormatter formatterParseInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			//List<AutoAssignConfigure> resultConfigurePreOne = autoAssignConfigureDAO.findConfigureByCreatedDate(formatterParseInput.format(LocalDate.now().plusDays(-1)));
			List<AutoAssignConfigure> resultConfigure = autoAssignConfigureDAO.findConfigureByCreatedDate(formatterParseInput.format(LocalDate.now()));

			if(resultConfigure.size()>0)
			{
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(201);
				responseModel.setMessage("Đã tồn tại config");

				return Map.of("status", 200, "data", responseModel);
			}
			if (resultConfigure.size() <= 0) {
				for (AutoAssignConfigure item : requestModel.getData()) {

					AutoAssignConfigure createAssign = new AutoAssignConfigure();
					createAssign.setCreatedDate(new Timestamp(new Date().getTime()));
					createAssign.setQuote(new Timestamp(new Date().getTime()));
					createAssign.setTotalQuanlity(item.getTotalQuanlity());
					createAssign.setActualTotalQuanlity(item.getActualTotalQuanlity());
					if (item.getActualTotalQuanlity() < item.getTotalQuanlity()){
						if (item.getQuanlityInDay() < (item.getTotalQuanlity() - item.getActualTotalQuanlity())){
							createAssign.setQuanlityInDay(item.getQuanlityInDay());
						}else{
							createAssign.setQuanlityInDay(item.getTotalQuanlity() - item.getActualTotalQuanlity());
						}
					}else{
						createAssign.setQuanlityInDay(0);
					}
					createAssign.setActualQuanlityInDay(0);
					createAssign.setVendorId(item.getVendorId());
					createAssign.setVendorName(item.getVendorName());
					createAssign.setPriority(item.getPriority());
					createAssign.setAssign(true);
					createAssign.setCreatedBy(token.path("user_name").asText(""));

					inputDataInsert.add(createAssign);
				}
				List<AutoAssignConfigure> result = autoAssignConfigureDAO.saveAll(inputDataInsert);

				//ghi log
				AutoAssignConfigureHistory autoAssignConfigureHistory = new AutoAssignConfigureHistory();
				autoAssignConfigureHistory.setCreatedDate(new Timestamp(new Date().getTime()));
				autoAssignConfigureHistory.setQuote(new Timestamp(new Date().getTime()));
				autoAssignConfigureHistory.setData( mapper.writeValueAsString(inputDataInsert));
				AutoAssignConfigureHistory resultHistory = autoAssignConfigureHistoryDAO.save(autoAssignConfigureHistory);
			}

			responseModel.setRequest_id(requestModel.getRequest_id());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setMessage("Thành công");

		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	@Transactional
	public Map<String, Object> updatedConfigure(JsonNode request) throws JsonProcessingException {
		String logs="updatedConfigure: " + mapper.writeValueAsString(request);
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			RequestModel requestModel = mapper.treeToValue(request, RequestModel.class);
			DateTimeFormatter formatterParseInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			List<Long> listPriority = new ArrayList<Long>();

			JsonNode token = checkToken(request.path("token").asText("Bearer ").split(" "));

			if (requestModel.getData() == null){
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("Please input required field ");

				logs +="; Please input required field";

				return Map.of("status", 200, "data", responseModel);
			}

			List<AutoAssignConfigure> inputData = requestModel.getData();
			for (AutoAssignConfigure item :	inputData) {
				AutoAssignConfigure resultConfigById = autoAssignConfigureDAO.findConfigureById(item.getId());
				if (resultConfigById != null) {

					//kiem tra neu ko cap nhat gì thì continue
					if(item.getTotalQuanlity()==resultConfigById.getTotalQuanlity()&&
							item.getPriority()==resultConfigById.getPriority()&&
					item.getQuanlityInDay()==resultConfigById.getQuanlityInDay())
					{
						continue;
					}

					if (formatterParseInput.format(resultConfigById.getCreatedDate().toLocalDateTime()).equals(formatterParseInput.format(LocalDate.now())) &&
							resultConfigById.getVendorId() != 3) {
						if (item.getTotalQuanlity() >= resultConfigById.getActualTotalQuanlity()) {
							if (item.getQuanlityInDay() > (item.getTotalQuanlity() - resultConfigById.getActualTotalQuanlity())) {
								responseModel.setRequest_id(requestModel.getRequest_id());
								responseModel.setReference_id(UUID.randomUUID().toString());
								responseModel.setDate_time(new Timestamp(new Date().getTime()));
								responseModel.setResult_code(3);
								responseModel.setMessage("Quanlity In Day not large than Total Quanlity - Actual Total Quanlity");

								logs +="; Quanlity In Day not large than Total Quanlity - Actual Total Quanlity";

								return Map.of("status", 200, "data", responseModel);
							}
						} else {
							responseModel.setRequest_id(requestModel.getRequest_id());
							responseModel.setReference_id(UUID.randomUUID().toString());
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code(2);
							responseModel.setMessage("Total Quanlity not less than Actual Total Quanlity");

							logs +="; Total Quanlity not less than Actual Total Quanlity";

							return Map.of("status", 200, "data", responseModel);
						}
					}

					if (item.getQuanlityInDay() < resultConfigById.getActualQuanlityInDay()){
						responseModel.setRequest_id(requestModel.getRequest_id());
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(1);
						responseModel.setMessage("Quanlity In Day not less than Actual Quanlity In Day");

						logs +="; Quanlity In Day not less than Actual Quanlity In Day";

						return Map.of("status", 200, "data", responseModel);
					}

					if (listPriority.contains(item.getPriority())) {
						responseModel.setRequest_id(requestModel.getRequest_id());
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(4);
						responseModel.setMessage("Duplicate Priority");

						logs +="; Duplicate Priority";

						return Map.of("status", 200, "data", responseModel);
					}
					listPriority.add(item.getPriority());
				}else {
					responseModel.setRequest_id(requestModel.getRequest_id());
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(1);
					responseModel.setMessage("config_id not found!");

					logs +="; config_id not found!";

					return Map.of("status", 200, "data", responseModel);
				}
			}

//			List<AutoAssignConfigure> inputData = requestModel.getData();
			for (AutoAssignConfigure item :	inputData) {
				AutoAssignConfigure resultConfigById = autoAssignConfigureDAO.findConfigureById(item.getId());
				if (formatterParseInput.format(resultConfigById.getCreatedDate().toLocalDateTime()).equals(formatterParseInput.format(LocalDate.now()))){
					if (item.getTotalQuanlity() >= resultConfigById.getActualTotalQuanlity() &&
							item.getQuanlityInDay() >= resultConfigById.getActualQuanlityInDay()){
						int result = autoAssignConfigureDAO.updateConfigure(item.getTotalQuanlity(), item.getQuanlityInDay(),
								item.getPriority(), true, item.getId(), resultConfigById.getIdSeq(),token.path("user_name").textValue());
					}
				}
			}

			AutoAssignConfigureHistory autoAssignConfigureHistory = new AutoAssignConfigureHistory();
			autoAssignConfigureHistory.setCreatedDate(new Timestamp(new Date().getTime()));
			autoAssignConfigureHistory.setQuote(new Timestamp(new Date().getTime()));
			autoAssignConfigureHistory.setData(mapper.writeValueAsString(inputData));
			autoAssignConfigureHistory.setCreatedBy(token.path("user_name").asText(""));
			AutoAssignConfigureHistory resultHistory = autoAssignConfigureHistoryDAO.save(autoAssignConfigureHistory);

			responseModel.setRequest_id(requestModel.getRequest_id());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
		}
		catch (Exception e) {
			logs +=";Error" + e.toString();

			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			log.info("{}", e);
		}
		finally {
			log.info(logs);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getConfigureById(long id) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			AutoAssignConfigure resultConfig = autoAssignConfigureDAO.findConfigureById(id);

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setData(resultConfig);
		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	@Transactional
	public Map<String, Object> configureApplication(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = "";
		String reference_id = "";
		long vendorId = 0;
		String vendorName = "";
		String vendorQueue = "";
		Long routingId = -1l;
		try{
			RequestApplicationModel requestApplicationModel = mapper.treeToValue(request, RequestApplicationModel.class);
			request_id = requestApplicationModel.getRequest_id();
			reference_id = requestApplicationModel.getReference_id();

			//get scheme
			String scheme= request.hasNonNull("schemeCode")?request.path("schemeCode").asText():"";

			if (StringUtils.isEmpty(reference_id)){
				reference_id = UUID.randomUUID().toString();
			}
			boolean exist = autoAssignConfigureApplicationDAO.existsAutoAssignConfigureApplicationByReferenceId(reference_id);
			if (exist){
				reference_id += "-" + new Date().getTime();
			}
			List<Object> resultGetVendorConfig = autoAssignConfigureDAO.getVendorConfigApplicationNew(request_id, reference_id,scheme);
			if (resultGetVendorConfig != null){
				vendorId = Integer.parseInt(resultGetVendorConfig.get(0).toString());

				Vendor vendor = vendorDAO.findVendorById(vendorId);
				vendorName = vendor.getName();
				vendorQueue = vendor.getQueue();
			}
			AutoAssignConfigureApplication autoAssignConfigureApplication = autoAssignConfigureApplicationDAO.findAutoAssignConfigureApplicationByReferenceId(reference_id);
			routingId = autoAssignConfigureApplication.getId();

//			AutoAssignLog inputLog = new AutoAssignLog();
//			inputLog.setRequestId(request_id);
//			inputLog.setReferenceId(reference_id);
//			inputLog.setData(request.path("data").toString());
//			inputLog.setCreatedDate(new Timestamp(new Date().getTime()));
//			AutoAssignLog result = autoAssignLogDAO.save(inputLog);

			responseModel.setRequest_id(requestApplicationModel.getRequest_id());
			responseModel.setReference_id(reference_id);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setData(Map.of("vendorName", vendorName, "vendorId", vendorId));

			ObjectNode dataLog = mapper.createObjectNode();
			dataLog.put("type", "configureApplication");
			dataLog.set("request", mapper.convertValue(request, JsonNode.class));
			dataLog.set("result", mapper.convertValue(responseModel, JsonNode.class));
			log.info("{}", dataLog);
		}
		catch (Exception e) {
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(reference_id);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			ObjectNode dataLog = mapper.createObjectNode();
			dataLog.put("type", "[==configureApplication==]");
			dataLog.set("request", mapper.convertValue(request, JsonNode.class));
			dataLog.set("error", mapper.convertValue(e, JsonNode.class));
			log.info("{}", dataLog);
		}
		return Map.of("status", 200, "data", responseModel, "vendorId", vendorId, "vendorName", vendorName, "vendorQueue", vendorQueue, "routingId", routingId);
	}

	public JsonNode checkToken(String[] token) {
		try {
			if (token.length == 2) {
				URI url = URI.create(tokenUri + "?token=" + token[1]);
				HttpMethod method = HttpMethod.GET;
				HttpHeaders headers = new HttpHeaders();
				headers.setBasicAuth(clientId, clientSecret);
				HttpEntity<?> entity = new HttpEntity<>(headers);
				ResponseEntity<?> res = restTemplate.exchange(url, method, entity, Map.class);
				return mapper.valueToTree(res.getBody());
			}
		} catch (HttpClientErrorException e) {
			System.err.println(e.getResponseBodyAsString());
		}

		return null;
	}

	//---------------- UPDATE ------------------------------------
	public Map<String, Object> getHistory(Map<String, String> param) {
		int page = Integer.parseInt(param.getOrDefault("page","1"));
		int limit = Integer.parseInt(param.getOrDefault("limit","10"));
		String[] sort = param.getOrDefault("sort","created_date,desc").split(",");

		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			List<AutoAssignConfigureHistory> resultConfig = new ArrayList<>();

			Pageable paging = PageRequest.of(page, limit, Sort.by(sort[1].equals("desc")? Sort.Direction.DESC: Sort.Direction.ASC,sort[0]));
			resultConfig = autoAssignConfigureHistoryDAO.getHistory(paging);

			if(resultConfig.size()==0) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("Không có danh sách");

				return Map.of("status", 200, "data", responseModel);
			}

			int total=autoAssignConfigureHistoryDAO.totalRow();
			int totalPage= total / limit;

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setMessage("Thành công");
			responseModel.setData(Map.of("result",resultConfig,"total", total,"totalpage",totalPage));
		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getQuickReport() {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			DateTimeFormatter formatterParseInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			List<AutoAssignConfigure> resultConfigInDay = new ArrayList<>();
			resultConfigInDay = autoAssignConfigureDAO.findConfigureByCreatedDate(formatterParseInput.format(LocalDate.now()));

			if(resultConfigInDay.size()==0)
			{
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("Không có config");

				return Map.of("status", 200, "data", responseModel);
			}

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setMessage("Thành công");
			responseModel.setData(resultConfigInDay.stream()
					.map(temp->{
						return QuickReportModel.builder().actualQuanlityInDay(temp.getActualQuanlityInDay())
								.actualTotalQuanlity(temp.getActualTotalQuanlity())
								.vendorId(temp.getVendorId())
								.vendorName(temp.getVendorName()).build();
					})
					.collect(Collectors.toList()));
		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}


	//---------------- UPDATE SCHEME ------------------------------
	public Map<String, Object> getScheme() {

		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			List<AutoAssignScheme> resultConfig = new ArrayList<>();

			resultConfig = autoAssignConfigureSchemeDAO.findAll();

			if(resultConfig.size()==0) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("Không có danh sách scheme");

				return Map.of("status", 200, "data", responseModel);
			}


			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setMessage("Thành công");
			responseModel.setData(Map.of("result",resultConfig));
		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getSchemeById(Map<String, String> param) {
		int id = Integer.parseInt(param.getOrDefault("id","1"));
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			AutoAssignScheme resultConfig = autoAssignConfigureSchemeDAO.findAutoAssignSchemeById(id);

			if(resultConfig==null) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("Không có kết quả");

				return Map.of("status", 200, "data", responseModel);
			}


			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setMessage("Thành công");
			responseModel.setData(Map.of("result",resultConfig));
		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

	@Transactional
	public Map<String, Object> insertScheme(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			RequestSchemeModel requestModel = mapper.treeToValue(request, RequestSchemeModel.class);
			JsonNode token = checkToken(request.path("token").asText("Bearer ").split(" "));

			if (requestModel.getData() == null){
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("data null!");
				return Map.of("status", 200, "data", responseModel);
			}

			List<AutoAssignScheme> list=requestModel.getData();
			//list.stream().forEach(u->u.setCreateddate(new Timestamp(new Date().getTime())));
			list.stream().map(autoAssignScheme -> {
				autoAssignScheme.setCreateddate(new Timestamp(new Date().getTime()));
				autoAssignScheme.setCreatedby(token.path("user_name").asText(""));
				return autoAssignScheme;
			}).collect(Collectors.toList());


			autoAssignConfigureSchemeDAO.saveAll(list);

			responseModel.setRequest_id(requestModel.getRequest_id());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setMessage("Thành công");

		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	@Transactional
	public Map<String, Object> updateScheme(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			JsonNode token = checkToken(request.path("token").asText("Bearer ").split(" "));

			int id=request.path("data").path("id").asInt();

			AutoAssignScheme autoAssignScheme=autoAssignConfigureSchemeDAO.findAutoAssignSchemeById(id);

			if(autoAssignScheme==null)
			{
				responseModel.setRequest_id(request.path("request_id").asText());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("data null!");
				return Map.of("status", 200, "data", responseModel);
			}

			AutoAssignScheme scheme=mapper.treeToValue(request.get("data"), AutoAssignScheme.class);

			autoAssignScheme.setScheme(scheme.getScheme());
			autoAssignScheme.setStatus(scheme.getStatus());
			autoAssignScheme.setLastdate(new Timestamp(new Date().getTime()));
			autoAssignScheme.setCreatedby(token.path("user_name").asText(""));

			autoAssignConfigureSchemeDAO.save(autoAssignScheme);

			responseModel.setRequest_id(request.path("request_id").asText());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setMessage("Thành công");

		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> deleteSchemeById(Map<String, String> param) {
		int id = Integer.parseInt(param.getOrDefault("id","1"));
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			AutoAssignScheme resultConfig = autoAssignConfigureSchemeDAO.findAutoAssignSchemeById(id);

			if(resultConfig==null) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("Không có kết quả");

				return Map.of("status", 200, "data", responseModel);
			}

			autoAssignConfigureSchemeDAO.deleteById(id);

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setMessage("Thành công");
			responseModel.setData(Map.of("result",resultConfig));
		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}
	//---------------- END ----------------------------------------
}