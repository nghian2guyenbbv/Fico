package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import vn.com.tpf.microservices.dao.ConfigRoutingDAO;
import vn.com.tpf.microservices.dao.LogChoiceRoutingDAO;
import vn.com.tpf.microservices.models.ConfigRouting;
import vn.com.tpf.microservices.models.LogChoiceRouting;
import vn.com.tpf.microservices.models.RequestModel;
import vn.com.tpf.microservices.models.ResponseModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AutoRoutingService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired
	private ConfigRoutingDAO configRoutingDAO;

	@Autowired
	private LogChoiceRoutingDAO logChoiceRoutingDAO;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RedisService redisService;

	public Map<String, Object> checkRouting(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			if (requestModel.getData() == null ) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Chanel Name is mandatory");
			} else {
				if (requestModel.getData().getChanelId() == null) {
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(500);
					responseModel.setMessage("Chanel Name is mandatory");
				} else {
					String chanelConfig = checkRule(requestModel.getData());

					LogChoiceRouting logChoiceRouting = new LogChoiceRouting();
					logChoiceRouting.setChanelId(requestModel.getData().getChanelId());
					logChoiceRouting.setCreateDate(new Timestamp(new Date().getTime()));
					logChoiceRouting.setRoutingNumber(chanelConfig);

					logChoiceRoutingDAO.save(logChoiceRouting);

					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(200);
					responseModel.setData(logChoiceRouting);
				}
			}
		} catch (Exception e) {
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

	public Map<String, Object> setRouting(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getRouting(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			List<ConfigRouting> configRoutingList = configRoutingDAO.findAll();

			if (configRoutingList.size() > 0) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(200);
				responseModel.setMessage("Success");
				responseModel.setData(configRoutingList);
			}

		} catch (Exception e) {
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

	public Map<String, Object> logRouting(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			LogChoiceRouting logChoiceRouting = (LogChoiceRouting) requestModel.getData();
			LogChoiceRouting logChoiceRoutingInsert = new LogChoiceRouting();

			if (logChoiceRouting.getAppNumber() != null) {
				logChoiceRoutingInsert = logChoiceRoutingDAO.findByIdLog(logChoiceRouting.getIdLog());
				if (logChoiceRoutingInsert == null) {
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(200);
					responseModel.setMessage("IdLod isn't exits");
					responseModel.setData(logChoiceRouting);
				} else {
					logChoiceRoutingInsert.setAppNumber(logChoiceRouting.getAppNumber());

					logChoiceRoutingDAO.save(logChoiceRoutingInsert);

					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(200);
					responseModel.setMessage("Success");
					responseModel.setData(logChoiceRoutingInsert);
				}
			} else {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(200);
				responseModel.setMessage("App Number is mandatory");
				responseModel.setData(logChoiceRouting);
			}
		} catch (Exception e) {
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

	public String checkRule (LogChoiceRouting request) {
		String chanelNumber = "0";
		String chanelConfig  = redisService.getValueFromCache("chanelConfig",request.getChanelId()+"Config" );
		if (!chanelConfig.equals("0")) {
			String timeStart  = redisService.getValueFromCache("chanelTimeStart",request.getChanelId()+"TimeStart" );
			String timeEnd  = redisService.getValueFromCache("chanelTimeEnd",request.getChanelId()+"TimeStart" );
			long timeLocal = 	System.currentTimeMillis();
			if (timeLocal > Long.parseLong(timeStart) || timeLocal < Long.parseLong(timeEnd)) {
				String quota  = redisService.getValueFromCache("chanelQuota",request.getChanelId()+"Quota" );
				if (Integer.parseInt(quota) > 0) {
					chanelNumber = "1";
				}
			}
		}
		return chanelNumber;
	}
}