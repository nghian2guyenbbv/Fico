package vn.com.tpf.microservices.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Mobility;
import vn.com.tpf.microservices.utils.Utils;

@Service
public class MobilityService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final String STAGE_PRECHECK1_DONE = "PRECHECK1ED";
	private final String STAGE_PRECHECK2_CHECIKING = "PRECHECK2_CHECKING";
	private final String STAGE_PRECHECK2_DONE = "PRECHECK2_DONE";
	private final String STAGE_UPLOADED = "UPLOADED";
	private final String STAGE_LEAD_DETAILS = "LEAD_DETAILS";
	private final String STAGE_LOGIN_ACCEPTANCE = "LOGIN_ACCEPTANCE";
	private final List<String> LIST_STATUS_COMPLETE = Arrays.asList("Cancellation".toUpperCase().trim(), "Rejection".toUpperCase().trim());
	private final String STAGE_QUICKLEAD_FAILED_AUTOMATION = "QUICKLEAD_FAILED_AUTOMATION";

	private final String AUTOMATION_QUICKLEAD_PASS = "QUICKLEAD_PASS";
	private final String AUTOMATION_QUICKLEAD_FAILED = "QUICKLEAD_FAILED";

	private final String AUTOMATION_RETURNQUERY_PASS = "RESPONSEQUERY_PASS";
	private final String AUTOMATION_RETURNQUERY_FAILED = "RESPONSEQUERY_FAILED";

	private final String AUTOMATION_SALEQUEUE_PASS = "SALEQUEUE_PASS";
	private final String AUTOMATION_SALEQUEUE_FAILED = "SALEQUEUE_FAILED";

	private final String STAGE_SALES_QUEUE = "SALES_QUEUE";
	private final String STAGE_SALES_QUEUE_UPLOADING_HAS_ACCA = "SALES_QUEUE_UPLOADING_HAS_ACCA";
	private final String STAGE_SALES_QUEUE_UPLOADING = "SALES_QUEUE_UPLOADING";
	private final String STAGE_SALES_QUEUE_FAILED = "SALES_QUEUE_FAILED_AUTOMATION";
	private final String STAGE_SALES_QUEUE_HAS_ACCA_FAILED = "SALES_QUEUE_HAS_ACCA_FAILED_AUTOMATION";
	private final String STAGE_RAISE_QUERY_FAILED = "RAISE_QUERY_FAILED_AUTOMATION";

	private final String STATUS_T_RETURN = "T_RETURN";
	private final String STATUS_PRE_APPROVAL = "PRE_APPROVAL";
	private final String STATUS_RETURNED = "RETURNED";

	private final String STATUS_RESUBMITING = "RESPONSE_QUERY_UPLOADING";
//	private final String STATUS_RESUBMIT_FAILED = "RESPONSE_QUERY_FAILED_AUTOMATION";
	
	private final String KEY_LAST_UPDATE_DATE = "lastUpdateDate";
	private final String KEY_USER_NAME = "userName";
	private final String KEY_STAGE = "stage";
	private final String KEY_STATUS = "status";
	

	@Value("${document-code-acca:tpf_application_cum_credit_contract_(acca)}")
	private String DOCUMENT_CODE_ACCA;

	@Autowired
	private Utils utils;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ApiService apiService;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private MongoTemplate mobilityTemplate;

	@Autowired
	private ConvertService convertService;
	

	public JsonNode getPreCheck1(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");
		final long leadId = data.path("leadId").asLong(0);
		if (leadId == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.leadId not null"));
		Mobility mobility = mobilityTemplate.findOne(Query.query(Criteria.where("leadId").is(leadId)), Mobility.class);
		if (mobility != null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s exits", leadId)));
		if (data.path("firstName").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.firstName not blank"));
		if (data.path("lastName").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.lastName not blank"));
		if (!data.path("nationalId").asText().matches("^(?=(?:.{9}|.{12})$)[0-9]*$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.nationalId not valid"));
		if (!data.path("dateOfBirth").asText()
				.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.dateOfBirth not blank"));
		if (data.path("dsaCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.dsaCode not blank"));
		if (data.path("district").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.district not blank"));
		if (data.path("city").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode());
		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddressFinnOne", "reference_id", body.path("reference_id"), "param",
						Map.of("areaCode", data.path("district").asText())));

		if (address.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.district not found"));
		if (!address.path("data").path("postCode").asText().equals(data.path("city").asText()))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.city not found"));

		mobility = Mobility.builder().leadId(leadId).firstName(data.path("firstName").asText())
				.lastName(data.path("lastName").asText()).nationalId(data.path("nationalId").asText())
				.dateOfBirth(data.path("dateOfBirth").asText()).dsaCode(data.path("dsaCode").asText())
				.city(address.path("data").path("postCode").asText())
				.cityFinnOne(address.path("data").path("cityName").asText())
				.district(address.path("data").path("areaCode").asText())
				.districtFinnOne(address.path("data").path("areaName").asText()).build();

		

		JsonNode preCheckResult = rabbitMQService.sendAndReceive("tpf-service-esb",
				Map.of("func", "getPrecheckList", "reference_id", body.path("reference_id"), "body",
						Map.of("bankCardNumber", "", "dsaCode", data.path("dsaCode").asText(), "areaCode",
								address.path("data").path("f1AreaCode").asText(), "nationalId",
								data.path("nationalId").asText())));
		if (preCheckResult.path("status").asInt(0) != 200) {
			return utils.getJsonNodeResponse(500, body, preCheckResult.path("data"));
		}
		mobility.setPreChecks(Map.of("preCheck1",
				Map.of("createdAt", new Date(), "data", mapper.convertValue(preCheckResult.path("data"), Map.class))));
		mobility.setStage(STAGE_PRECHECK1_DONE);
		mobility.setStatus(STATUS_PRE_APPROVAL);
		mobilityTemplate.save(mobility);
		return utils.getJsonNodeResponse(0, body, preCheckResult.path("data"));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonNode getPreCheck2(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");
		final long leadId = data.path("leadId").asLong(0);
		
		if (leadId == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.leadId not null"));
		if (!data.path("bankCardNumber").asText().matches("[0-9]{16}"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.bankCardNumber not valid"));
		
		Query query = Query.query(Criteria.where("leadId").is(leadId));
		Mobility mobility = mobilityTemplate.findOne(query, Mobility.class);
		if (mobility == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s not exits", leadId)));
		JsonNode precheks = mapper.convertValue(mobility.getPreChecks(), JsonNode.class);

		if (!precheks.hasNonNull("preCheck1") || precheks.path("preCheck1").path("data").path("result").asInt() != 0)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s precheck1 not pass - %s",
							leadId, precheks.path("preCheck1").path("data").path("description").asText())));
		if (precheks.hasNonNull("preCheck2") && (mapper.convertValue(precheks.path("preCheck2"), ArrayNode.class))
				.get(0).path("data").path("result").asInt() == 0)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.leadId %s preCheck2 passed at %s", leadId,
									(mapper.convertValue(precheks.path("preCheck2"), ArrayNode.class)).get(0)
											.path("createdAt").asText())));

		JsonNode preCheckResult = rabbitMQService.sendAndReceive("tpf-service-esb",
				Map.of("func", "getPrecheckList", "reference_id", body.path("reference_id"), "body",
						Map.of("bankCardNumber", data.path("bankCardNumber").asText(), "dsaCode", "", "areaCode", "",
								"nationalId", mobility.getNationalId())));
		if (preCheckResult.path("status").asInt(0) != 200)
			return utils.getJsonNodeResponse(500, body, preCheckResult.path("data"));

		HashMap<String, Object> preCheck2 = new HashMap<>();
		preCheck2.put("createdAt", new Date());
		preCheck2.put("bankCardNumber", data.path("bankCardNumber").asText());
		preCheck2.put("data", mapper.convertValue(preCheckResult.path("data"), Map.class));

		LinkedList<Map> preCheck2New = mapper
				.convertValue(mapper.convertValue(precheks.path("preCheck2"), ArrayNode.class), LinkedList.class);
		if (preCheck2New == null)
			preCheck2New = new LinkedList<Map>();
		preCheck2New.push(preCheck2);
		Update update = new Update().set("updatedAt", new Date())
				.set("bankCardNumber", data.path("bankCardNumber").asText()).set("preChecks", Map.of("preCheck1",
						mapper.convertValue(precheks.path("preCheck1"), Map.class), "preCheck2", preCheck2New));
		if (preCheckResult.path("data").path("result").asInt(-1) == 0)
			update.set("stage", STAGE_PRECHECK2_DONE);
		else
			update.set("stage", STAGE_PRECHECK2_CHECIKING);
		mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Mobility.class);
		return utils.getJsonNodeResponse(0, body,
				((ArrayNode) mapper.convertValue(mobility.getPreChecks(), JsonNode.class).path("preCheck2")).get(0)
						.path("data"));
	}

	public JsonNode getAppInfo(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");
		final String appId = data.path("appId").asText();
		if (appId.isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.appId not null"));
		Query query = Query.query(Criteria.where("appId").is(appId));
		Mobility mobility = mobilityTemplate.findOne(query, Mobility.class);
		if (mobility == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));
		
		if (LIST_STATUS_COMPLETE.contains(mobility.getStatus().toUpperCase().trim()))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s complete with status %s", appId,mobility.getStatus().toUpperCase().trim())));
		JsonNode item = rabbitMQService.sendAndReceive("tpf-service-esb", Map.of("func", "getAppInfo", "body", body));

		if (item.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message", String.format("data.appId %s waiting sync data", appId)));
		
		Update update = new Update().set("updatedAt", new Date());
		final boolean updateStageAndStatus = (mobility.getViewLastUpdated() == null || !item.path("data").path(KEY_LAST_UPDATE_DATE).asText().trim().toUpperCase().equals(mobility.getViewLastUpdated().trim().toUpperCase()));
		if (updateStageAndStatus) 
			update.set("viewLastUpdated", item.path("data").path(KEY_LAST_UPDATE_DATE).asText().trim().toUpperCase()).set("stage", item.path("data").path(KEY_STAGE).asText().toUpperCase().trim()).set("status", item.path("data").path(KEY_STATUS).asText().toUpperCase().trim());
		
		if(!item.path("data").path(KEY_STAGE).asText().toUpperCase().trim().equals(STAGE_SALES_QUEUE))
			update.set("userCreatedQueue", item.path("data").path(KEY_USER_NAME).asText());
		
		
		if (updateStageAndStatus)  {
			mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Mobility.class);
			rabbitMQService.send("tpf-service-app",
					Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
							Map.of("project", "mobility", "id", mobility.getId()), "body",
							convertService.toAppStatus(mobility)));
		}
		((ObjectNode)item.path("data")).remove(KEY_USER_NAME);
		return utils.getJsonNodeResponse(0, body, item.path("data"));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonNode addDocuments(JsonNode request) throws Exception {
		
		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");

		final long leadId = data.path("leadId").asLong(0);
		
		if (leadId == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.leadId not null"));

		if (data.path("productCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.productCode not blabnk"));
		if (data.path("schemeCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.schemeCode not blabnk"));
		if (!data.hasNonNull("documents") || mapper.convertValue(data.path("documents"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.documents array required"));

		Query query = Query.query(Criteria.where("leadId").is(leadId));
		Mobility mobility = mobilityTemplate.findOne(query, Mobility.class);
		if (mobility == null)
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s not exits", data.path("leadId").asInt())));

		if (mobility.getStage().equals(STAGE_PRECHECK1_DONE))
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s precheck2 not exits", data.path("leadId").asInt())));

		if (mobility.getStage().equals(STAGE_PRECHECK2_CHECIKING)) {
			JsonNode lastPreCheck2 = mapper.convertValue(mobility.getPreChecks().get("preCheck2"), ArrayNode.class)
					.get(0);
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.leadId %s precheck2 %s  %s", data.path("leadId").asInt(),
									lastPreCheck2.path("bankCardNumber").asText(),
									lastPreCheck2.path("data").path("description").asText())));
		}

		if (mobility.getStage().equals(STAGE_UPLOADED) || mobility.getFilesUpload() != null )
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s uploaded document at %s ",
							data.path("leadId").asInt(), mobility.getUpdatedAt())));
		final String productCode = data.path("productCode").asText().replace(" ", "_").trim().toLowerCase();
		final String schemeCode = data.path("schemeCode").asText().replace(" ", "_").trim().toLowerCase();
		
		JsonNode documentFinnOne = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getListDocuments", "reference_id", request.path("reference_id"), "body",
						Map.of("productCode", data.path("productCode").asText(), "schemeCode",
								data.path("schemeCode").asText())));

		if (documentFinnOne.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("%s %s not found", productCode, schemeCode)));

		ObjectNode documentsDb = mapper.convertValue(documentFinnOne.path("data").path("documents"), ObjectNode.class);
		ArrayNode documents = mapper.convertValue(data.path("documents"), ArrayNode.class);
		for (int index = 0; index < documents.size(); index++) {
			if (documents.get(index).path("documentMd5").asText().isBlank()
					|| documents.get(index).path("documentCode").asText().isBlank()
					|| documents.get(index).path("documentFileExtension").asText().isBlank()
					|| documents.get(index).path("documentUrlDownload").asText().isBlank())
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message", String.format("document %s not valid", index)));
			if (!documentsDb.hasNonNull(documents.get(index).path("documentCode").asText()))
				return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
						String.format("%s not found", documents.get(index).path("documentCode").asText())));
			ObjectNode document = ((ObjectNode) documentsDb.path(documents.get(index).path("documentCode").asText()));
			document.put("required", false);
			if (document.hasNonNull("documentsReplace")) {
				List<String> documentsReplace = mapper.convertValue(document.path("documentsReplace"), List.class);
				for (String docReplace : documentsReplace)
					if (documentsDb.hasNonNull(docReplace))
						((ObjectNode) documentsDb.path(docReplace)).put("required", false);
			}
		}

		for (JsonNode document : documentsDb)
			if (document.path("required").asBoolean())
				return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
						String.format("document %s requied", document.path("code").asText())));

		List<HashMap> filesUpload = new ArrayList<HashMap>();
		for (JsonNode document : documents) {
			JsonNode documentDbInfo = documentsDb.path(document.path("documentCode").asText());
			JsonNode uploadResult = apiService.uploadDocumentInternal(document, documentDbInfo);
			if (uploadResult.path("resultCode").asInt() != 200)
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message", uploadResult.path("message").asText()));
			if (!uploadResult.path("data").path("md5").asText().toLowerCase()
					.equals(document.path("documentMd5").asText().toLowerCase()))
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message",
								String.format("document %s md5 %s not valid", document.path("documentCode").asText(),
										uploadResult.path("md5").asText().toLowerCase())));
			HashMap<String, String> docUpload = new HashMap<>();
			docUpload.put("documentCode", document.path("documentCode").asText());
			docUpload.put("documentFileExtension", document.path("documentFileExtension").asText());
			docUpload.put("documentUrlDownload", document.path("documentUrlDownload").asText());
			docUpload.put("documentMd5", document.path("documentMd5").asText());
			docUpload.put("type", documentDbInfo.path("valueFinnOne").asText());
			docUpload.put("originalname", document.path("documentCode").asText().concat(".")
					.concat(document.path("documentFileExtension").asText()));
			docUpload.put("filename", uploadResult.path("data").path("filename").asText());
			filesUpload.add(docUpload);
		}
		Update update = new Update().set("updatedAt", new Date()).set("stage", STAGE_UPLOADED)
				.set("status", STATUS_PRE_APPROVAL).set("scheme", data.path("schemeCode").asText())
				.set("product", data.path("productCode").asText())
				.set("schemeFinnOne", documentFinnOne.path("data").path("valueShemeFinnOne").asText())
				.set("productFinnOne", documentFinnOne.path("data").path("valueProductFinnOne").asText())
				.set("filesUpload", filesUpload);
		mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Mobility.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "createQuickLeadApp", "body",
				convertService.toAppFinnone(mobility).put("reference_id", body.path("reference_id").asText())));

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(mobility).put("reference_id", body.path("reference_id").asText())));
		
		return utils.getJsonNodeResponse(0, body, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonNode updateAutomation(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		if (!request.hasNonNull("body"))
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "body not null"));
		if (body.path("transaction_id").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "body.transaction_id not null"));
		if (body.path("app_id").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "body.app_id not null"));
		if (body.path("automation_result").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "body.automation_result not null"));

		String automationResult = body.path("automation_result").asText().toUpperCase().replace(" ", "_").trim();
		boolean updateStatus = false;

		Query query = Query.query(Criteria.where("_id").is(body.path("transaction_id").asText()));
		Mobility mobility = mobilityTemplate.findOne(query, Mobility.class);
		if (mobility == null)
			return utils.getJsonNodeResponse(500, body, mapper.createObjectNode().put("message",
					String.format("id %s mobility not exits", body.path("transaction_id").asText())));

		HashMap<String, Object> automationResultObject = new HashMap<>();

		automationResultObject.put("createdAt", new Date());

		automationResultObject.put("automationResult", automationResult);

		LinkedList<Map> automationResultsNew = mapper.convertValue(mobility.getAutomationResults(), LinkedList.class);
		if (automationResultsNew == null)
			automationResultsNew = new LinkedList<Map>();
		automationResultsNew.push(automationResultObject);
		Update update = new Update().set("updatedAt", new Date());
		update.set("automationResults", automationResultsNew);

		if (automationResult.contains(AUTOMATION_QUICKLEAD_PASS)) {
			update.set("appId", body.path("app_id").asText());
			update.set("stage", STAGE_LEAD_DETAILS);
			mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
					Mobility.class);
			final Mobility mobilitySender = Mobility.builder().leadId(mobility.getLeadId()).appId(mobility.getAppId())
					.build();
			new Thread(() -> {
				try {
					int sendCount = 0;
					do {
						JsonNode result = apiService.pushAppIdOfLeadId(mobilitySender);
						if (result.path("resultCode").asText().equals("200")) 
							return;					
						sendCount++;
						Thread.sleep(5 * 60 * 1000);
					} while (sendCount <= 2);

				} catch (Exception e) {
					log.info("{}", utils.getJsonNodeResponse(0, body,
							mapper.createObjectNode().put("message", e.getMessage())));
				}
			}).start();
		}
		if (automationResult.contains(AUTOMATION_QUICKLEAD_FAILED)) {
			update.set("stage", STAGE_QUICKLEAD_FAILED_AUTOMATION);
		}

		if (automationResult.contains(AUTOMATION_RETURNQUERY_PASS)) {
			JsonNode returns = mapper.convertValue(mobility.getReturns(), JsonNode.class);

			if (returns.hasNonNull("returnQueries")
					&& (mapper.convertValue(returns.path("returnQueries"), ArrayNode.class)).get(0).path("isComplete")
							.asBoolean())
				return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
						String.format("data.appId %s returnQuery is complete", mobility.getAppId())));

			LinkedList<Map> returnQueriesNew = mapper.convertValue(
					mapper.convertValue(returns.path("returnQueries"), ArrayNode.class), LinkedList.class);
			if (returnQueriesNew == null)
				returnQueriesNew = new LinkedList<Map>();
			returnQueriesNew.get(0).put("isComplete", true);
			returnQueriesNew.get(0).put("updatedAt", new Date());
			update.set("updatedAt", new Date());
			update.set("returns.returnQueries", returnQueriesNew);
			update.set("status", STATUS_RETURNED);
			update.set("stage", STAGE_LEAD_DETAILS);
			updateStatus = true;
		}
		if (automationResult.contains(AUTOMATION_RETURNQUERY_FAILED)) 
			update.set("stage", STAGE_RAISE_QUERY_FAILED);
		

		if (automationResult.contains(AUTOMATION_SALEQUEUE_PASS)) {
			JsonNode returns = mapper.convertValue(mobility.getReturns(), JsonNode.class);

			if (returns.hasNonNull("returnQueues")
					&& (mapper.convertValue(returns.path("returnQueues"), ArrayNode.class)).get(0).path("isComplete")
							.asBoolean())
				return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
						String.format("data.appId %s returnQueue is complete", mobility.getAppId())));

			LinkedList<Map> returnQueuesNew = mapper
					.convertValue(mapper.convertValue(returns.path("returnQueues"), ArrayNode.class), LinkedList.class);
			if (returnQueuesNew == null)
				returnQueuesNew = new LinkedList<Map>();
			returnQueuesNew.get(0).put("isComplete", true);
			returnQueuesNew.get(0).put("updatedAt", new Date());
			update.set("updatedAt", new Date());
			update.set("returns.returnQueues", returnQueuesNew);

			final String currentStage = mobility.getStage();
			if (currentStage.equals(STAGE_SALES_QUEUE_UPLOADING_HAS_ACCA) || currentStage.equals(STAGE_SALES_QUEUE_HAS_ACCA_FAILED))
				update.set("stage", STAGE_LEAD_DETAILS);
			if (currentStage.equals(STAGE_SALES_QUEUE_UPLOADING) || currentStage.equals(STAGE_SALES_QUEUE_FAILED))
				update.set("stage", STAGE_LOGIN_ACCEPTANCE);
		}

		if (automationResult.contains(AUTOMATION_SALEQUEUE_FAILED)) {
			final String currentStage = mobility.getStage();
			if (currentStage.equals(STAGE_SALES_QUEUE_UPLOADING_HAS_ACCA))
				update.set("stage", STAGE_SALES_QUEUE_HAS_ACCA_FAILED);
			if (currentStage.equals(STAGE_SALES_QUEUE_UPLOADING))
				update.set("stage", STAGE_SALES_QUEUE_FAILED);
		}
			
		mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Mobility.class);
		if(!automationResult.isBlank()) 
			rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "mobility", "id", mobility.getId()), "body",
						convertService.toAppAutomation(mobility,updateStatus)));
		return utils.getJsonNodeResponse(0, body, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonNode returnQuery(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		ObjectNode data = (ObjectNode) request.path("body").path("data");
		final String appId = data.path("appId").asText();
		if (appId.isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.appId not null"));
		if (data.path("comment").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.comment not null"));
		if (!data.hasNonNull("document") && (data.path("document").path("documentCode").asText().isBlank()
				|| data.path("document").path("documentFileExtension").asText().isBlank()
				|| data.path("document").path("documentMd5").asText().isBlank()))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.document not valid"));
		if (!data.path("document").path("documentUrlDownload").asText().matches(
				"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.document.documentUrlDownload not valid"));
		if (!data.path("document").path("documentCode").asText().toLowerCase().trim().replace(" ", "_")
				.equals(DOCUMENT_CODE_ACCA))
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("data.document.documentCode required %s", DOCUMENT_CODE_ACCA)));

		Query query = Query.query(Criteria.where("appId").is(appId));
		Mobility mobility = mobilityTemplate.findOne(query, Mobility.class);
		if (mobility == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));

		if (!mobility.getStatus().equals(STATUS_T_RETURN))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.appId %s not request resubmit query. current stage %s status %s", appId,
									mobility.getStage(), mobility.getStatus())));

		final String productCode = mobility.getProduct().replace(" ", "_").trim().toLowerCase();
		final String schemeCode = mobility.getScheme().replace(" ", "_").trim().toLowerCase();

		JsonNode documentFinnOne = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getListDocuments", "reference_id", request.path("reference_id"), "body",
						Map.of("productCode", productCode, "schemeCode", schemeCode)));

		if (documentFinnOne.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("%s %s not found", productCode, schemeCode)));

		HashMap<String, Object> returnQuery = new HashMap<>();
		JsonNode returns = mapper.convertValue(mobility.getReturns(), JsonNode.class);
		if (returns.hasNonNull("returnQueries")
				&& !(mapper.convertValue(returns.path("returnQueries"), ArrayNode.class)).get(0).path("isComplete")
						.asBoolean())
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.appId %s returnQuery not complete", mobility.getAppId())));

		JsonNode document = data.path("document");
		if (document != null) {
			ObjectNode documentsDb = mapper.convertValue(documentFinnOne.path("data").path("documents"),
					ObjectNode.class);
			if (!documentsDb.hasNonNull(document.path("documentCode").asText()))
				return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
						String.format("%s not found", document.path("documentCode").asText())));
			JsonNode documentDbInfo = documentsDb.path(document.path("documentCode").asText());
			JsonNode uploadResult = apiService.uploadDocumentInternal(document, documentDbInfo);
			if (uploadResult.path("resultCode").asInt() != 200)
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message", uploadResult.path("message").asText()));
			if (!uploadResult.path("data").path("md5").asText().toLowerCase()
					.equals(document.path("documentMd5").asText().toLowerCase()))
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message",
								String.format("document %s md5 %s not valid", document.path("documentCode").asText(),
										uploadResult.path("md5").asText().toLowerCase())));
			HashMap<String, String> docUpload = new HashMap<>();
			docUpload.put("documentCode", document.path("documentCode").asText());
			docUpload.put("documentFileExtension", document.path("documentFileExtension").asText());
			docUpload.put("documentUrlDownload", document.path("documentUrlDownload").asText());
			docUpload.put("documentMd5", document.path("documentMd5").asText());
			docUpload.put("type", documentDbInfo.path("valueFinnOne").asText());
			docUpload.put("originalname", document.path("documentCode").asText().concat(".")
					.concat(document.path("documentFileExtension").asText()));
			docUpload.put("filename", uploadResult.path("data").path("filename").asText());
			data.set("document", mapper.convertValue(docUpload, JsonNode.class));
			returnQuery.put("data", mapper.convertValue(docUpload, Map.class));
		}

		returnQuery.put("createdAt", new Date());
		returnQuery.put("updatedAt", new Date());
		returnQuery.put("comment", data.path("comment").asText());
		returnQuery.put("isComplete", false);

		LinkedList<Map> returnQueriesNew = mapper
				.convertValue(mapper.convertValue(returns.path("returnQueries"), ArrayNode.class), LinkedList.class);
		if (returnQueriesNew == null)
			returnQueriesNew = new LinkedList<Map>();
		returnQueriesNew.push(returnQuery);
		Update update = new Update().set("updatedAt", new Date());
		if (returns.hasNonNull("returnQueues"))
			update.set("returns",
					Map.of("returnQueries", returnQueriesNew, "returnQueues", returns.path("returnQueues")));
		else
			update.set("returns", Map.of("returnQueries", returnQueriesNew));
		update.set("status", STATUS_RESUBMITING);

		mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Mobility.class);


		rabbitMQService.send("tpf-service-esb", Map.of("func", "deResponseQuery", "body",
				convertService.toReturnQueryFinnone(mobility).put("reference_id", body.path("reference_id").asText())));
		
		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "mobility", "id", mobility.getId()), "body",
						convertService.toAppStatus(mobility)));
		
		return utils.getJsonNodeResponse(0, body, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonNode returnQueue(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		boolean hasDocumentCodeACCA = false;
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		ObjectNode data = (ObjectNode) request.path("body").path("data");
		final String appId = data.path("appId").asText();
		if (appId.isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.appId not null"));
		if (data.path("comment").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.comment not null"));
		final boolean hasDocuments = data.hasNonNull("documents");
		if (hasDocuments) {
			ArrayNode documents = mapper.convertValue(data.path("documents"), ArrayNode.class);
			if (documents != null && documents.size() == 0)
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message", "data.documents not empty"));
			for (int index = 0; index < documents.size(); index++) {
				JsonNode document = documents.get(index);
				if (document.path("documentCode").asText().isBlank()
						|| document.path("documentFileExtension").asText().isBlank()
						|| document.path("documentMd5").asText().isBlank())
					return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
							String.format("data.documents[%s]  not valid", index)));
				if (!document.path("documentUrlDownload").asText().matches(
						"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"))
					return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
							String.format("data.documents[%s].documentUrlDownload not valid", index)));
				if (document.path("documentCode").asText().toLowerCase().trim().replace(" ", "_")
						.equals(DOCUMENT_CODE_ACCA))
					hasDocumentCodeACCA = true;

			}
		}

		Query query = Query.query(Criteria.where("appId").is(appId));
		Mobility mobility = mobilityTemplate.findOne(query, Mobility.class);
		if (mobility == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));

		if (!mobility.getStage().equals(STAGE_SALES_QUEUE))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.appId %s not request resubmit queue. current stage %s status %s", appId,
									mobility.getStage(), mobility.getStatus())));

		final String productCode = mobility.getProduct().replace(" ", "_").trim().toLowerCase();
		final String schemeCode = mobility.getScheme().replace(" ", "_").trim().toLowerCase();

		JsonNode documentFinnOne = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getListDocuments", "reference_id", request.path("reference_id"), "body",
						Map.of("productCode", productCode, "schemeCode", schemeCode)));

		if (documentFinnOne.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("%s %s not found", productCode, schemeCode)));

		JsonNode returns = mapper.convertValue(mobility.getReturns(), JsonNode.class);
		if (returns.hasNonNull("returnQueues") && !(mapper.convertValue(returns.path("returnQueues"), ArrayNode.class))
				.get(0).path("isComplete").asBoolean())
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.appId %s returnQueue not complete", mobility.getAppId())));

		ObjectNode documentsDb = mapper.convertValue(documentFinnOne.path("data").path("documents"), ObjectNode.class);
		HashMap<String, Object> returnQueue = new HashMap<>();
		if (hasDocuments) {
			ArrayNode documents = mapper.convertValue(data.path("documents"), ArrayNode.class);

			for (int index = 0; index < documents.size(); index++) {
				if (documents.get(index).path("documentMd5").asText().isBlank()
						|| documents.get(index).path("documentCode").asText().isBlank()
						|| documents.get(index).path("documentFileExtension").asText().isBlank()
						|| documents.get(index).path("documentUrlDownload").asText().isBlank())
					return utils.getJsonNodeResponse(499, body,
							mapper.createObjectNode().put("message", String.format("document %s not valid", index)));
				if (!documentsDb.hasNonNull(documents.get(index).path("documentCode").asText()))
					return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
							String.format("%s not found", documents.get(index).path("documentCode").asText())));
			}

			List<HashMap> filesUpload = new ArrayList<HashMap>();
			for (JsonNode document : documents) {
				JsonNode documentDbInfo = documentsDb.path(document.path("documentCode").asText());
				JsonNode uploadResult = apiService.uploadDocumentInternal(document, documentDbInfo);
				if (uploadResult.path("resultCode").asInt() != 200)
					return utils.getJsonNodeResponse(499, body,
							mapper.createObjectNode().put("message", uploadResult.path("message").asText()));
				if (!uploadResult.path("data").path("md5").asText().toLowerCase()
						.equals(document.path("documentMd5").asText().toLowerCase()))
					return utils.getJsonNodeResponse(499, body,
							mapper.createObjectNode().put("message",
									String.format("document %s md5 %s not valid",
											document.path("documentCode").asText(),
											uploadResult.path("md5").asText().toLowerCase())));
				HashMap<String, String> docUpload = new HashMap<>();
				docUpload.put("documentCode", document.path("documentCode").asText());
				docUpload.put("documentFileExtension", document.path("documentFileExtension").asText());
				docUpload.put("documentUrlDownload", document.path("documentUrlDownload").asText());
				docUpload.put("documentMd5", document.path("documentMd5").asText());
				docUpload.put("type", documentDbInfo.path("valueFinnOne").asText());
				docUpload.put("originalname", document.path("documentCode").asText().concat(".")
						.concat(document.path("documentFileExtension").asText()));
				docUpload.put("filename", uploadResult.path("data").path("filename").asText());
				filesUpload.add(docUpload);
			}

			returnQueue.put("data", filesUpload);
		}

		returnQueue.put("createdAt", new Date());
		returnQueue.put("updatedAt", new Date());
		returnQueue.put("comment", data.path("comment").asText());
		returnQueue.put("isComplete", false);

		LinkedList<Map> returnQueuesNew = mapper
				.convertValue(mapper.convertValue(returns.path("returnQueues"), ArrayNode.class), LinkedList.class);
		if (returnQueuesNew == null)
			returnQueuesNew = new LinkedList<Map>();
		returnQueuesNew.push(returnQueue);

		Update update = new Update().set("updatedAt", new Date());
		update.set("returns.returnQueues", returnQueuesNew);

		if (hasDocumentCodeACCA)
			update.set("stage", STAGE_SALES_QUEUE_UPLOADING_HAS_ACCA);
		else
			update.set("stage", STAGE_SALES_QUEUE_UPLOADING);

		mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Mobility.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "deSaleQueue", "body",
				convertService.toSaleQueueFinnone(mobility).put("reference_id", body.path("reference_id").asText())));
		
		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "mobility", "id", mobility.getId()), "body",
						convertService.toAppStage(mobility)));
		
		return utils.getJsonNodeResponse(0, body, null);
	}

}