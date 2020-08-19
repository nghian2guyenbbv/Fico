package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import vn.com.tpf.microservices.models.ResponseModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Service
public class AutoRoutingService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	public Map<String, Object> check(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> setRouting(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getRouting(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> logRouting(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		return Map.of("status", 200, "data", responseModel);
	}
}