package vn.com.tpf.microservices.services;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
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

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RestTemplate restTemplate;

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

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
			responseModel.setData(resultConfigInDay);
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

						inputDataInsert.add(createAssign);
					}
					List<AutoAssignConfigure> result = autoAssignConfigureDAO.saveAll(inputDataInsert);

					AutoAssignConfigureHistory autoAssignConfigureHistory = new AutoAssignConfigureHistory();
					autoAssignConfigureHistory.setCreatedDate(new Timestamp(new Date().getTime()));
					autoAssignConfigureHistory.setQuote(new Timestamp(new Date().getTime()));
					autoAssignConfigureHistory.setData(inputDataInsert.toString());
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

	@Transactional
	public Map<String, Object> createdConfigure(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			RequestModel requestModel = mapper.treeToValue(request, RequestModel.class);

			if (requestModel.getData() == null){
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(0);
				responseModel.setMessage("data null!");
				return Map.of("status", 200, "data", responseModel);
			}

			List<AutoAssignConfigure> inputRequest = requestModel.getData();
			List<AutoAssignConfigure> inputDataInsert = new ArrayList<>();
			DateTimeFormatter formatterParseInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");

//			List<AutoAssignConfigure> resultConfigurePreOne = autoAssignConfigureDAO.findConfigureByCreatedDate(formatterParseInput.format(LocalDate.now().plusDays(-1)));
			List<AutoAssignConfigure> resultConfigure = autoAssignConfigureDAO.findConfigureByCreatedDate(formatterParseInput.format(LocalDate.now()));

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

					inputDataInsert.add(createAssign);
				}
				List<AutoAssignConfigure> result = autoAssignConfigureDAO.saveAll(inputDataInsert);
			}

			responseModel.setRequest_id(requestModel.getRequest_id());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);

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
	public Map<String, Object> updatedConfigure(JsonNode request) {
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
				return Map.of("status", 200, "data", responseModel);
			}

			List<AutoAssignConfigure> inputData = requestModel.getData();
			for (AutoAssignConfigure item :	inputData) {
				AutoAssignConfigure resultConfigById = autoAssignConfigureDAO.findConfigureById(item.getId());
				if (resultConfigById != null) {
					if (formatterParseInput.format(resultConfigById.getCreatedDate().toLocalDateTime()).equals(formatterParseInput.format(LocalDate.now())) &&
							resultConfigById.getVendorId() != 3) {
						if (item.getTotalQuanlity() >= resultConfigById.getActualTotalQuanlity()) {
							if (item.getQuanlityInDay() >= (item.getTotalQuanlity() - resultConfigById.getActualTotalQuanlity())) {
								responseModel.setRequest_id(requestModel.getRequest_id());
								responseModel.setReference_id(UUID.randomUUID().toString());
								responseModel.setDate_time(new Timestamp(new Date().getTime()));
								responseModel.setResult_code(3);
								responseModel.setMessage("Quanlity In Day not large than Total Quanlity - Actual Total Quanlity");
								return Map.of("status", 200, "data", responseModel);
							}
						} else {
							responseModel.setRequest_id(requestModel.getRequest_id());
							responseModel.setReference_id(UUID.randomUUID().toString());
							responseModel.setDate_time(new Timestamp(new Date().getTime()));
							responseModel.setResult_code(2);
							responseModel.setMessage("Total Quanlity not less than Actual Total Quanlity");
							return Map.of("status", 200, "data", responseModel);
						}
					}

					if (item.getQuanlityInDay() < resultConfigById.getActualQuanlityInDay()){
						responseModel.setRequest_id(requestModel.getRequest_id());
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(1);
						responseModel.setMessage("Quanlity In Day not less than Actual Quanlity In Day");
						return Map.of("status", 200, "data", responseModel);
					}

					if (listPriority.contains(item.getPriority())) {
						responseModel.setRequest_id(requestModel.getRequest_id());
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(4);
						responseModel.setMessage("Duplicate Priority");
						return Map.of("status", 200, "data", responseModel);
					}
					listPriority.add(item.getPriority());
				}else {
					responseModel.setRequest_id(requestModel.getRequest_id());
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(1);
					responseModel.setMessage("config_id not found!");
					return Map.of("status", 200, "data", responseModel);
				}
			}

//			List<AutoAssignConfigure> inputData = requestModel.getData();
			for (AutoAssignConfigure item :	inputData) {
				AutoAssignConfigure resultConfigById = autoAssignConfigureDAO.findConfigureById(item.getId());
				if (formatterParseInput.format(resultConfigById.getCreatedDate().toLocalDateTime()).equals(formatterParseInput.format(LocalDate.now())) &&
						resultConfigById.getVendorId() != 3){
					if (item.getTotalQuanlity() >= resultConfigById.getActualTotalQuanlity() &&
							item.getQuanlityInDay() >= resultConfigById.getActualQuanlityInDay()){
						int result = autoAssignConfigureDAO.updateConfigure(item.getTotalQuanlity(), item.getQuanlityInDay(),
								item.getPriority(), true, item.getId(), resultConfigById.getIdSeq());
					}
				}
			}

			AutoAssignConfigureHistory autoAssignConfigureHistory = new AutoAssignConfigureHistory();
			autoAssignConfigureHistory.setCreatedDate(new Timestamp(new Date().getTime()));
			autoAssignConfigureHistory.setQuote(new Timestamp(new Date().getTime()));
			autoAssignConfigureHistory.setData(inputData.toString());
			autoAssignConfigureHistory.setCreatedBy(token.path("user_name").textValue());
			AutoAssignConfigureHistory resultHistory = autoAssignConfigureHistoryDAO.save(autoAssignConfigureHistory);

			responseModel.setRequest_id(requestModel.getRequest_id());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);
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
			boolean exist = autoAssignConfigureApplicationDAO.existsAutoAssignConfigureApplicationByReferenceId(reference_id);
			if (exist){
				reference_id += "-" + new Date().getTime();
			}
			List<Object> resultGetVendorConfig = autoAssignConfigureDAO.getVendorConfigApplication(request_id, reference_id);
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
}