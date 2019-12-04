package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import vn.com.tpf.microservices.models.AutoAssign.AutoAssignDTO;
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
//            LoginDTO.builder().userName("auto_data_entry_1").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("auto_data_entry_2").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("auto_data_entry_3").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("auto_data_entry_4").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("auto_data_entry_5").password("Hcm@12345").build(),
			LoginDTO.builder().userName("anhdlh").password("Tpf@1234").build()
	);
	final static Queue<LoginDTO> loginDTOQueue = new LinkedBlockingQueue<>(accounts);

//	final static List<LoginDTO> momoAccounts= Arrays.asList(
//            LoginDTO.builder().userName("momo_auto1").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("momo_auto2").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("momo_auto3").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("momo_auto4").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("momo_auto5").password("Hcm@12345").build()
//	);
//	final static Queue<LoginDTO> momo_loginDTOQueue = new LinkedBlockingQueue<>(momoAccounts);
//
	final static List<LoginDTO> momoAccountsPro= Arrays.asList(
			LoginDTO.builder().userName("momo_auto1").password("Tpf@12345").build(),
			LoginDTO.builder().userName("momo_auto2").password("Tpf@12345").build(),
			LoginDTO.builder().userName("momo_auto3").password("Tpf@12345").build()
	);
	final static Queue<LoginDTO> momo_loginDTOQueue = new LinkedBlockingQueue<>(momoAccountsPro);


	final static List<LoginDTO> fptAccounts= Arrays.asList(
			LoginDTO.builder().userName("auto_1").password("Hcm@12345").build()
//            LoginDTO.builder().userName("momo_auto2").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("momo_auto3").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("momo_auto4").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("momo_auto5").password("Hcm@12345").build()
	);
	final static Queue<LoginDTO> fpt_loginDTOQueue = new LinkedBlockingQueue<>(fptAccounts);

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

	//------------------------ DATA ENTRY - FUNCTION -------------------------------------

	public Map<String, Object> quickLeadApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		System.out.println(request);
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

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"quickLead","DATAENTRY");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	private void runAutomationDE_UpdateInfomation(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomation_UpdateInfo","DATAENTRY");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	private void runAutomationDE_UpdateAppError(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomation_UpdateAppError","DATAENTRY");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	//------------------------- END DATA ENTRY - FUNCTION ---------------------------------

	//------------------------ MOMO - FUNCTION -------------------------------------

	public Map<String, Object> momoCreateApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		String reference_id = request.path("reference_id").asText();

		System.out.println(request);

		Assert.notNull(request.get("body"), "no body");
		Application application = mapper.treeToValue(request.path("body"), Application.class);
		application.setReference_id(reference_id);

		System.out.println(mapper.writeValueAsString(application));

		new Thread(() -> {
			try {
				runAutomationMomo_CreateApp(application);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, application);
	}

	private void runAutomationMomo_CreateApp(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataMomo(application);
//
//		//get list account finone available
//		Query query = new Query();
//		query.addCriteria(Criteria.where("active").is(0));
//		List<AccountFinOneDTO> accountFinOneDTOList=mongoTemplate.find(query, AccountFinOneDTO.class);
//		if(accountFinOneDTOList ==null || accountFinOneDTOList.size()==0)
//		{
//
//		}

		AutomationThreadService automationThreadService= new AutomationThreadService(momo_loginDTOQueue, browser, mapValue,"momoCreateApp","MOMO");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	public Map<String, Object> fptCreateApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		String reference_id = request.path("reference_id").asText();

		System.out.println(request);

		Assert.notNull(request.get("body"), "no body");
		Application application = mapper.treeToValue(request.path("body"), Application.class);
		application.setReference_id(reference_id);

		System.out.println(mapper.writeValueAsString(application));

		new Thread(() -> {
			try {
				runAutomationFpt_CreateApp(application);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, application);
	}

	private void runAutomationFpt_CreateApp(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFpt(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(fpt_loginDTOQueue, browser, mapValue,"fptCreateApp","FPT");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	//------------------------- END MOMO - FUNCTION ---------------------------------

	//------------------------ AUTO ASSIGN - FUNCTION -------------------------------------
	public Map<String, Object> DE_AutoAssign(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		String reference_id = request.path("reference_id").asText();

		System.out.println(request);

		Assert.notNull(request.get("body"), "no body");
		List<AutoAssignDTO> autoAssignDTOList = mapper.convertValue(request.path("body").path("data"), new TypeReference<List<AutoAssignDTO>>(){});

		new Thread(() -> {
			try {
				runAutomationDE_AutoAssign(autoAssignDTOList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, autoAssignDTOList);
	}

	private void runAutomationDE_AutoAssign(List<AutoAssignDTO> autoAssignDTOList) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE_AutoAssign(autoAssignDTOList);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomationDE_AutoAssign","DE");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}
	//------------------------ END AUTO ASSIGN - FUNCTION -------------------------------------
}