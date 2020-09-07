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
import vn.com.tpf.microservices.models.AutoCRM.CRM_ExistingCustomerDTO;
import vn.com.tpf.microservices.models.AutoCRM.CRM_SaleQueueDTO;
import vn.com.tpf.microservices.models.AutoField.RequestAutomationDTO;
import vn.com.tpf.microservices.models.AutoField.SubmitFieldDTO;
import vn.com.tpf.microservices.models.AutoField.WaiveFieldDTO;
import vn.com.tpf.microservices.models.Automation.LoginDTO;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDTO;
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
//			LoginDTO.builder().userName("momo_auto5").password("Hcm@12345").build(),
//            LoginDTO.builder().userName("momo_auto12").password("Hcm@12345").build()
////            LoginDTO.builder().userName("momo_auto2").password("Hcm@12345").build(),
////            LoginDTO.builder().userName("momo_auto3").password("Hcm@12345").build(),
////            LoginDTO.builder().userName("momo_auto4").password("Hcm@12345").build(),
//
//	);
//	final static Queue<LoginDTO> momo_loginDTOQueue = new LinkedBlockingQueue<>(momoAccounts);

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

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"momoCreateApp","MOMO");
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

	//------------------------ DE_RESPONSE_QUERY - FUNCTION -------------------------------------
	public Map<String, Object> DE_ResponseQuery(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		DEResponseQueryDTO deResponseQueryDTOList = mapper.treeToValue(request.path("body"), DEResponseQueryDTO.class);

		new Thread(() -> {
			try {
				runAutomationDE_ResponseQuery(deResponseQueryDTOList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, deResponseQueryDTOList);
	}

	private void runAutomationDE_ResponseQuery(DEResponseQueryDTO deResponseQueryDTOList) throws Exception {
		String browser = "chrome";
		String projectJson = deResponseQueryDTOList.getProject();
		Map<String, Object> mapValue = DataInitial.getDataFromDE_ResponseQuery(deResponseQueryDTOList);
		AutomationThreadService automationThreadService = null;
        if("smartnet".equals(projectJson)) {
            automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomationDE_ResponseQuery","RETURN");
        }else{
            automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomationDE_ResponseQuery",projectJson.toUpperCase());
        }


        applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);
	}
	//------------------------ END - DE_RESPONSE_QUERY - FUNCTION -------------------------------------

	//------------------------ DE_SALE_QUEUE - FUNCTION -------------------------------------
	public Map<String, Object> DE_SaleQueue(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		DESaleQueueDTO deSaleQueueDTOList = mapper.treeToValue(request.path("body"), DESaleQueueDTO.class);

		new Thread(() -> {
			try {
				runAutomationDE_SaleQueue(deSaleQueueDTOList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, deSaleQueueDTOList);
	}

	private void runAutomationDE_SaleQueue(DESaleQueueDTO deSaleQueueDTOList) throws Exception {
		String browser = "chrome";
		String projectJson = deSaleQueueDTOList.getProject();
		Map<String, Object> mapValue = DataInitial.getDataFromDE_SaleQueue(deSaleQueueDTOList);
		AutomationThreadService automationThreadService = null;
        if(projectJson.equals("smartnet")) {
            automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomationDE_SaleQueue","RETURN");
        }else{
            automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomationDE_SaleQueue",projectJson.toUpperCase());
        }

		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);
	}
	//------------------------ END - DE_SALE_QUEUE - FUNCTION -------------------------------------


	//------------------------ SMARTNET - FUNCTION -----------------------------------------
	public Map<String, Object> SN_quickLeadApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		Application application = mapper.treeToValue(request.path("body"), Application.class);

		new Thread(() -> {
			try {
				SN_runAutomation_QL(application);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, application.getQuickLead());
	}

	private void SN_runAutomation_QL(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE_QL(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"SN_quickLead","RETURN");

		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}

	//------------------------ END - SMARTNET - FUNCTION -------------------------------------


	//------------------------ MOBILITY - FUNCTION -----------------------------------------

	//------------------------ QUICKLEAD  -------------------------------------
	public Map<String, Object> MOBILITY_quickLeadApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		Application application = mapper.treeToValue(request.path("body"), Application.class);

		new Thread(() -> {
			try {
				MOBILITY_runAutomation_QL(application);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, application.getQuickLead());
	}

	private void MOBILITY_runAutomation_QL(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE_QL(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"MOBILITY_quickLead","MOBILITY");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);

		//awaitTerminationAfterShutdown(workerThreadPool);
	}
	//------------------------ END - QUICKLEAD  -------------------------------------

	//------------------------ WAIVE_FIELD  --------------------------------------
	public Map<String, Object> Waive_Field(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		String reference_id = request.path("reference_id").asText();
		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		RequestAutomationDTO requestWaiveField = mapper.convertValue(request.path("body"), new TypeReference<RequestAutomationDTO>(){});
		List<WaiveFieldDTO> waiveFieldDTOList = mapper.convertValue(request.path("body").path("data"), new TypeReference<List<WaiveFieldDTO>>(){});
		requestWaiveField.setReference_id(reference_id);
		requestWaiveField.setWaiveFieldDTO(waiveFieldDTOList);

		new Thread(() -> {
			try {
				runAutomation_Waive_Field(requestWaiveField);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, requestWaiveField);
	}

	private void runAutomation_Waive_Field(RequestAutomationDTO waiveFieldDTOList) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFrom_Waive_Field(waiveFieldDTOList);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomation_Waive_Field","MOBILITY");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);
	}

	//------------------------ END - WAIVE_FIELD  ----------------------------------

	//------------------------ SUBMIT_FIELD  -------------------------------------
	public Map<String, Object> Submit_Field(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		String reference_id = request.path("reference_id").asText();
		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		RequestAutomationDTO requestSubmitField = mapper.convertValue(request.path("body"), new TypeReference<RequestAutomationDTO>(){});
		List<SubmitFieldDTO> submitFieldDTOList = mapper.convertValue(request.path("body").path("data"), new TypeReference<List<SubmitFieldDTO>>(){});
		requestSubmitField.setReference_id(reference_id);
		requestSubmitField.setSubmitFieldDTO(submitFieldDTOList);

		new Thread(() -> {
			try {
				runAutomation_Submit_Field(requestSubmitField);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, requestSubmitField);
	}

	private void runAutomation_Submit_Field(RequestAutomationDTO submitFieldDTOList) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFrom_Submit_Field(submitFieldDTOList);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomation_Submit_Field","MOBILITY");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);
	}

	//------------------------ END - SUBMIT_FIELD  -------------------------------

	//------------------------ END - MOBILITY - FUNCTION -----------------------------------------

	//------------------------ PROJECT CRM  -------------------------------------

	//------------------------ QUICKLEAD -------------------------------------
	public Map<String, Object> CRM_quickLeadApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		String project = request.path("body").path("project").textValue();
		CRM_ExistingCustomerDTO application = mapper.treeToValue(request.path("body"), CRM_ExistingCustomerDTO.class);

		new Thread(() -> {
			try {
				CRM_runAutomation_QL(application, project);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, application.getQuickLead());
	}

	private void CRM_runAutomation_QL(CRM_ExistingCustomerDTO application, String project) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromCRM_QL(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"CRM_quickLead", project);
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);
	}

	public Map<String, Object> CRM_quickLeadAppWithCustID(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		CRM_ExistingCustomerDTO existingCustomerDTOList = mapper.treeToValue(request.path("body"), CRM_ExistingCustomerDTO.class);

		new Thread(() -> {
			try {
				CRM_runAutomation_QLWithCustID(existingCustomerDTOList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, existingCustomerDTOList);
	}

	private void CRM_runAutomation_QLWithCustID(CRM_ExistingCustomerDTO existingCustomerDTOList) throws Exception {
		String browser = "chrome";
		String projectJson = existingCustomerDTOList.getProject();
		Map<String, Object> mapValue = DataInitial.getDataFromCRM_QLWithCustID(existingCustomerDTOList);
		AutomationThreadService automationThreadService = new AutomationThreadService(loginDTOQueue, browser, mapValue,"CRM_quickLead_With_CustID",projectJson);

		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);
	}
	//------------------------ END - QUICKLEAD

	//------------------------ EXISTING_CUSTOMER -------------------------------------
	public Map<String, Object> Existing_Customer(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		CRM_ExistingCustomerDTO existingCustomerDTOList = mapper.treeToValue(request.path("body"), CRM_ExistingCustomerDTO.class);

		new Thread(() -> {
			try {
				runAutomation_Existing_Customer(existingCustomerDTOList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, existingCustomerDTOList);
	}

	private void runAutomation_Existing_Customer(CRM_ExistingCustomerDTO existingCustomerDTOList) throws Exception {
		String browser = "chrome";
		String projectJson = existingCustomerDTOList.getProject();
		Map<String, Object> mapValue = DataInitial.getDataFrom_Existing_Customer(existingCustomerDTOList);
		AutomationThreadService automationThreadService = new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomation_Existing_Customer",projectJson);

		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);
	}
	//------------------------ END - EXISTING_CUSTOMER -------------------------------------


	//------------------------ SALE_QUEUE -------------------------------------
	public Map<String, Object> Sale_Queue_With_FullInfo(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		CRM_SaleQueueDTO saleQueueDTOList = mapper.treeToValue(request.path("body"), CRM_SaleQueueDTO.class);

		new Thread(() -> {
			try {
				runAutomation_Sale_Queue_With_FullInfo(saleQueueDTOList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, saleQueueDTOList);
	}

	private void runAutomation_Sale_Queue_With_FullInfo(CRM_SaleQueueDTO saleQueueDTOList) throws Exception {
		String browser = "chrome";
		String projectJson = saleQueueDTOList.getProject();
		Map<String, Object> mapValue = DataInitial.getDataFrom_Sale_Queue_FullInfo(saleQueueDTOList);
		AutomationThreadService automationThreadService = new AutomationThreadService(loginDTOQueue, browser, mapValue,"runAutomation_Sale_Queue_With_FullInfo",projectJson);

		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);
	}
	//------------------------ END - SALE_QUEUE -------------------------------------


	//------------------------ END - PROJECT CRM  -------------------------------------


	//------------------------ PROJECT CRM  -------------------------------------

	//------------------------ QUICK LEAD WITH ASSIGN POOL -------------------------------------------
	public Map<String, Object> quickLeadAppAssignPool(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		System.out.println(request);
		Assert.notNull(request.get("body"), "no body");
		Application application = mapper.treeToValue(request.path("body"), Application.class);

		new Thread(() -> {
			try {
				runAutomation_QLAssignPool(application);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return response(0, body, application.getQuickLead());
	}

	private void runAutomation_QLAssignPool(Application application) throws Exception {
		String browser = "chrome";
		Map<String, Object> mapValue = DataInitial.getDataFromDE_QL(application);

		AutomationThreadService automationThreadService= new AutomationThreadService(loginDTOQueue, browser, mapValue,"quickLead_Assign_Pool","DATAENTRY");
		applicationContext.getAutowireCapableBeanFactory().autowireBean(automationThreadService);
		workerThreadPool.submit(automationThreadService);
	}

	//------------------------ END - QUICK LEAD WITH ASSIGN POOL -------------------------------------

	//------------------------ END - PROJECT CRM  -------------------------------
}