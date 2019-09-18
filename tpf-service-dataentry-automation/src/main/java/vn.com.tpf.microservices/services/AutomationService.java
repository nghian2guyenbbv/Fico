package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import vn.com.tpf.microservices.models.Automation.LoginDTO;
import vn.com.tpf.microservices.models.QuickLead.Application;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.DataInitial;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class AutomationService {

	private ObjectNode error;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private AutomationHandlerService automationHandlerService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ApplicationContext applicationContext;

	private WebDriver driver;
	final static ExecutorService workerThreadPool = Executors.newFixedThreadPool(Constant.THREAD_NUM);
	final static List<LoginDTO> accounts= Arrays.asList(
//            LoginDTO.builder().userName("vin_auto1").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("vin_auto2").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("vin_auto3").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("vin_auto4").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("vin_auto5").password("Hcm@12345").build()
			LoginDTO.builder().userName("anhdlh").password("Tpf@1234").build()
	);
	final static Queue<LoginDTO> loginDTOQueue = new LinkedBlockingQueue<>(accounts);

	@PostConstruct
	private void init() {

	}

	private Map<String, Object> response(int code, JsonNode body, Object data) {
		Map<String, Object> res = new HashMap<>();
		res.put("result_code", code);
		res.put("request_id", body.path("request_id").asText());
		res.put("date_time", body.path("date_time").asText());
		res.put("reference_id", body.path("reference_id").asText());
		if (code == 0) {
			res.put("data", mapper.convertValue(data, JsonNode.class));
		} else {
			res.put("message", mapper.convertValue(data, JsonNode.class).get("message"));
		}
		return Map.of("status", 200, "data", res);
	}

	//------------------------ FUNCTION -------------------------------------

	public Map<String, Object> quickLeadApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Assert.notNull(request.get("body"), "no body");
		Application application = mapper.treeToValue(request.path("body"), Application.class);

		runAutomationDE_QL(application);

		return response(0, body, application.getQuickLead());
	}

	public Map<String, Object> fullInfoApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		return response(0, body, null);
	}

	public Map<String, Object> updateInfoApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		return response(0, body, null);
	}

	private JsonNode validation(JsonNode body, List<String> fields) {
		for (String field : fields) {
			if (field.equals("request_id")
					&& (!body.path("request_id").isTextual() || body.path("request_id").asText().isEmpty())) {
				return error.get("requestId");
			}
			if (field.equals("date_time")
					&& (!body.path("date_time").isTextual() || body.path("date_time").asText().isEmpty())) {
				return error.get("dateTime");
			}
			if (field.equals("data.phone_number")
					&& !body.path("data").path("phone_number").asText().matches("^(\\+84)(?=(?:.{9}|.{10})$)[0-9]*$")) {
				return error.get("phoneNumber");
			}
			if (field.equals("data.national_id")
					&& !body.path("data").path("national_id").asText().matches("^(?=(?:.{9}|.{12})$)[0-9]*$")) {
				return error.get("nationalId");
			}
			if (field.equals("data.middle_name") && !body.path("data").path("middle_name").isTextual()) {
				return error.get("middleName");
			}
			if (field.equals("data.first_name") && (!body.path("data").path("first_name").isTextual()
					|| body.path("data").path("first_name").asText().isEmpty())) {
				return error.get("firstName");
			}
			if (field.equals("data.last_name") && (!body.path("data").path("last_name").isTextual()
					|| body.path("data").path("last_name").asText().isEmpty())) {
				return error.get("lastName");
			}
			if (field.equals("data.address_no") && (!body.path("data").path("address_no").isTextual()
					|| body.path("data").path("address_no").asText().isEmpty())) {
				return error.get("addressNo");
			}
			if (field.equals("data.dob") && !body.path("data").path("dob").asText()
					.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$")) {
				return error.get("dob");
			}
			if (field.equals("data.gender")
					&& !body.path("data").path("gender").asText().toLowerCase().matches("^(male|female)$")) {
				return error.get("gender");
			}
			if (field.equals("data.province_code") && !body.path("data").path("province_code").isInt()) {
				return error.get("provinceCode");
			}
			if (field.equals("data.district_code") && !body.path("data").path("district_code").isInt()) {
				return error.get("districtCode");
			}
			if (field.equals("data.ts_lead_id") && (!body.path("data").path("ts_lead_id").isTextual()
					|| body.path("data").path("ts_lead_id").asText().isEmpty())) {
				return error.get("tsLeadId");
			}
			if (field.equals("data.product_code") && (!body.path("data").path("product_code").isTextual()
					|| body.path("data").path("product_code").asText().isEmpty())) {
				return error.get("productCode");
			}
			if (field.equals("data.score_range") && (!body.path("data").path("score_range").isTextual()
					|| body.path("data").path("score_range").asText().isEmpty())) {
				return error.get("scoreRange");
			}
			if (field.equals("data.dsa_code") && (!body.path("data").path("dsa_code").isTextual()
					|| body.path("data").path("dsa_code").asText().isEmpty())) {
				return error.get("dsaCode");
			}
			if (field.equals("data.tsa_code") && (!body.path("data").path("tsa_code").isTextual()
					|| body.path("data").path("tsa_code").asText().isEmpty())) {
				return error.get("tsaCode");
			}
			if (field.equals("data.ward")
					&& (!body.path("data").path("ward").isTextual() || body.path("data").path("ward").asText().isEmpty())) {
				return error.get("ward");
			}
			if (field.equals("data.stage")
					&& (!body.path("data").path("stage").isTextual() || body.path("data").path("stage").asText().isEmpty())) {
				return error.get("stage");
			}
			if (field.equals("data.documents")
					&& (!body.path("data").path("documents").isArray() || body.path("data").path("documents").size() == 0)) {
				return error.get("documents");
			}
		}
		return null;
	}

	private void runAutomationDE_QL(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE_QL(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"quickLead");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	//------------------------- END FUNCTION ---------------------------------
}