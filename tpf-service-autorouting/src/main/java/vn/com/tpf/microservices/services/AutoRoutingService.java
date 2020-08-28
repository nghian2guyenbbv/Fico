package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import vn.com.tpf.microservices.dao.ConfigRoutingDAO;
import vn.com.tpf.microservices.dao.HistoryConfigDAO;
import vn.com.tpf.microservices.dao.LogChoiceRoutingDAO;
import vn.com.tpf.microservices.dao.ScheduleRoutingDAO;
import vn.com.tpf.microservices.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.*;

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

	@Autowired
	private ScheduleRoutingDAO scheduleRoutingDAO;

	@Autowired
	private HistoryConfigDAO historyConfigDAO;

	@Autowired
	private GetDatAutoRoutingService getDatAutoRoutingService;

	/**
	 * To check rule and return config rule to inhouse service
	 * @param request
	 * @return 0: Robot, 1: API
	 */
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
		try {
			ConfigRouting configRoutingUpdated = mapper.readValue(request.get("body").toString(),
					new TypeReference<ConfigRouting>(){});
			boolean isNew = true;
			List<ScheduleRoute> scheduleRouteList = scheduleRoutingDAO.findByIdConfig(configRoutingUpdated.getIdConfig());
			if (scheduleRouteList.size() > 0) {
				saveHistoryConfig(scheduleRouteList, configRoutingUpdated.getIdConfig());
				isNew = false;
			}
			changeInfoScheduleRoute(configRoutingUpdated.getScheduleRoutes(), configRoutingUpdated.getIdConfig(), isNew);
			List<ScheduleRoute> scheduleRouteListNew = configRoutingUpdated.getScheduleRoutes();
			Iterable<ScheduleRoute> scheduleRouteIterable = scheduleRouteListNew;
			scheduleRoutingDAO.saveAll(scheduleRouteIterable);
			// Update Cache
			List<Map<String, Object>> configRoutingList = (List<Map<String, Object>>) redisService.getObjectValueFromCache("routingConfigInfo", "CONFIG_ROUTING_KEY");
			for (Map<String, Object> stringObjectMap : configRoutingList) {
				if (stringObjectMap.get("idConfig").equals(configRoutingUpdated.getIdConfig())) {
					stringObjectMap.put("chanelConfig", configRoutingUpdated.getChanelConfig());
					stringObjectMap.put("quota", configRoutingUpdated.getQuota());
					stringObjectMap.put("scheduleRoutes", configRoutingUpdated.getScheduleRoutes());
				}
			}
			getDatAutoRoutingService.updateRoutingConfig(configRoutingList);

			responseModel.setData("Save Success");

		} catch (Exception e) {
			log.info("Error: " + e);
		}

		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getRouting(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			Object configRouting = redisService.getObjectValueFromCache("routingConfigInfo", "CONFIG_ROUTING_KEY");
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Success");
			responseModel.setData(configRouting);

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
	/**
	 * To save log when inhouse call this service
	 * @param request
	 * @return
	 */
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

	/**
	 * To check rule and return config rule to inhouse service
	 * @param request
	 * @return
	 */
	public String checkRule (LogChoiceRouting request) {
		String chanelNumber = "0";
		String chanelConfig  = redisService.getValueFromCache("chanelConfig",request.getChanelId()+"Config" );
		if (!chanelConfig.isEmpty() && !chanelConfig.equals("0")) {
			String timeStart  = redisService.getValueFromCache("chanelTimeStart",request.getChanelId()+"TimeStart" );
			String timeEnd  = redisService.getValueFromCache("chanelTimeEnd",request.getChanelId()+"TimeEnd" );
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

	public Map<String, Object> getHistoryConfig(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			int page = request.path("body").path("page").asInt();
			String idConfig = request.path("body").path("idConfig").asText();
			int limit = request.path("body").path("limit").asInt();
			Pageable pagination = PageRequest.of(page - 1 , limit);
			List<HistoryConfig> historyConfigList = historyConfigDAO.findAllByIdConfig(idConfig, pagination);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Success");
			responseModel.setData(historyConfigList);
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

	public void saveHistoryConfig(List<ScheduleRoute> scheduleRouteList, String idConfig) {
		List<HistoryConfig> historyConfigs = new ArrayList<>();

		for (ScheduleRoute scheduleRoute : scheduleRouteList) {
			HistoryConfig historyConfig = new HistoryConfig();
			historyConfig.setQuota(scheduleRoute.getQuota());
			historyConfig.setTimeStart(scheduleRoute.getTimeStart());
			historyConfig.setTimeEnd(scheduleRoute.getTimeEnd());
			historyConfig.setDayId(scheduleRoute.getDayId());
			historyConfig.setChanelConfig(scheduleRoute.getChanelConfig());
			historyConfig.setDayName(scheduleRoute.getDayName());
			historyConfig.setCreateDate(scheduleRoute.getCreateDate());
			historyConfig.setUpdateDate(scheduleRoute.getUpdateDate());
			historyConfig.setUserUpdated(scheduleRoute.getUserUpdated());
			historyConfig.setUserUpdateDate(scheduleRoute.getUserUpdateDate());
			historyConfig.setIdConfig(idConfig);
			historyConfigs.add(historyConfig);
		}
		Iterable<HistoryConfig> historyConfigIterable = historyConfigs;
		historyConfigDAO.saveAll(historyConfigIterable);

	}

	public void changeInfoScheduleRoute(List<ScheduleRoute> scheduleRouteList, String idConfig, boolean isNew) {
		Date dateToday = new Date();
		Timestamp tsToDay = new Timestamp(dateToday.getTime());
		for (ScheduleRoute scheduleRoute : scheduleRouteList) {
			ConfigRouting configRouting = new ConfigRouting();
			configRouting.setIdConfig(idConfig);
			scheduleRoute.setConfigRouting(configRouting);
			if (!isNew) {
				scheduleRoute.setUpdateDate(tsToDay);
				scheduleRoute.setUserUpdateDate(tsToDay);
			}
		}
	}

}