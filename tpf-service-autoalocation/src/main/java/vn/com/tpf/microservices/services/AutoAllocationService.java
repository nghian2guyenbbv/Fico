package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.models.ResponseModel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class AutoAllocationService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;


	/**
	 * To check rule and return config rule to inhouse service
	 * @param request
	 * @return 0: Robot, 1: API
	 */
	public Map<String, Object> getAppAutoAllocation(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
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

	public Map<String, Object> getUserToAssign(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		try {
		} catch (Exception e) {
			log.info("Error: " + e);
		}

		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getHistoryApp(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
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

	public Map<String, Object> importUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
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
	public Map<String, Object> sendAppFromF1(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
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
}