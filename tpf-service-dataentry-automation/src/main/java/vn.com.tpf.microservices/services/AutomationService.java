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

		new Thread(() -> {
			try {
				runAutomationDE_QL(application);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, application.getQuickLead());
	}

	public Map<String, Object> fullInfoApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		System.out.println(request);

		Assert.notNull(request.get("body"), "no body");
		Application application = mapper.treeToValue(request.path("body"), Application.class);

		new Thread(() -> {
			try {
				runAutomationDE_UpdateInfomation(application);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, application);
	}

	public Map<String, Object> updateAppError(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		System.out.println(request);

		Assert.notNull(request.get("body"), "no body");
		Application application = mapper.treeToValue(request.path("body"), Application.class);

		new Thread(() -> {
			try {
				runAutomationDE_UpdateAppError(application);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, application);
	}

	private void runAutomationDE_QL(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE_QL(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"quickLead");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	private void runAutomationDE_UpdateInfomation(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomation_UpdateInfo");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	private void runAutomationDE_UpdateAppError(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomation_UpdateAppError");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	//------------------------- END FUNCTION ---------------------------------
}