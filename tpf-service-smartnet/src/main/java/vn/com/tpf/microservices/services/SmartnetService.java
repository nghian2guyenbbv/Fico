package vn.com.tpf.microservices.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import vn.com.tpf.microservices.models.Smartnet;
import vn.com.tpf.microservices.utils.Utils;

@Service
public class SmartnetService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final String STAGE_PRECHECK1_DONE = "PRECHECK1ED";
	private final String STAGE_PRECHECK2_CHECIKING = "PRECHECK2_CHECKING";
	private final String STAGE_PRECHECK2_DONE = "PRECHECK2_DONE";
	private final String STAGE_UPLOADED = "UPLOADED";
	private final String STAGE_LEAD_DETAILS = "LEAD_DETAILS";
	private final List<String> LIST_STAGE_COMPLETE = Arrays.asList("SEND_TO_OPTS", "REJECTION", "CANCELLATION");
	private final String STAGE_QUICKLEAD_FAILED_AUTOMATION = "QUICKLEAD FAILED AUTOMATION";

	private final String AUTOMATION_QUICKLEAD_PASS = "QUICKLEAD_PASS";
	private final String AUTOMATION_QUICKLEAD_FAILED = "QUICKLEAD_FAILED";

	private final String AUTOMATION_RETURNQUERY_PASS = "RESPONSEQUERY_PASS";
	private final String AUTOMATION_RETURNQUERY_FAILED = "RESPONSEQUERY_FAILED";

	private final String AUTOMATION_RETURNQUEUE_PASS = "SALEQUEUE_PASS";
	private final String AUTOMATION_RETURNQUEUE_FAILED = "SALEQUEUE_FAILED";

	private final String STAGE_SALES_QUEUE = "SALES_QUEUE";
	private final String STATUS_DEFAULT = "STATUS_DEFAULT";
	private final String STATUS_T_RETURN = "T_RETURN";
	private final String STATUS_INTRODUCED = "INTRODUCED";

	@Autowired
	private Utils utils;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ApiService apiService;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private MongoTemplate smartnetTemplate;

	@Autowired
	private ConvertService convertService;

	@PostConstruct
	public void init() {
		System.out.println(LIST_STAGE_COMPLETE.contains("REJECTION"));

	}

	public JsonNode getPreCheck1(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");
		final long leadId = data.path("leadId").asLong(0);
		if (leadId == 0)
			return utils.getJsonNodeResponse(2, body, mapper.createObjectNode().put("message", "data.leadId not null"));
		Smartnet smartnet = smartnetTemplate.findOne(Query.query(Criteria.where("leadId").is(leadId)), Smartnet.class);
		if (smartnet != null)
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
					mapper.createObjectNode().put("message", "data.city not blank"));
		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddressFinnOne", "reference_id", body.path("reference_id"), "param",
						Map.of("areaCode", data.path("district").asText())));

		if (address.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.district not found"));
		if (!address.path("data").path("postCode").asText().equals(data.path("city").asText()))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.city not found"));

		smartnet = Smartnet.builder().leadId(leadId).firstName(data.path("firstName").asText())
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
		smartnet.setPreChecks(Map.of("preCheck1",
				Map.of("createdAt", new Date(), "data", mapper.convertValue(preCheckResult.path("data"), Map.class))));
		smartnet.setStage(STAGE_PRECHECK1_DONE);
		smartnet.setStatus(STATUS_DEFAULT);
		smartnetTemplate.save(smartnet);

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
		Query query = Query.query(Criteria.where("leadId").is(leadId));
		Smartnet smartnet = smartnetTemplate.findOne(query, Smartnet.class);
		if (smartnet == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s not exits", leadId)));

		if (!data.path("bankCardNumber").asText().matches("[0-9]{16}"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.bankCardNumber not valid"));

		JsonNode precheks = mapper.convertValue(smartnet.getPreChecks(), JsonNode.class);

		if (!precheks.hasNonNull("preCheck1") || precheks.path("preCheck1").path("data").path("result").asInt() != 0)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s precheck1 not pass - %s",
							leadId, precheks.path("preCheck1").path("data").path("description").asText())));
		if (precheks.hasNonNull("preCheck2") && (mapper.convertValue(precheks.path("preCheck2"), ArrayNode.class))
				.get(0).path("data").path("result").asInt() == 0)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s preCheck2 pass", leadId)));

		JsonNode preCheckResult = rabbitMQService.sendAndReceive("tpf-service-esb",
				Map.of("func", "getPrecheckList", "reference_id", body.path("reference_id"), "body",
						Map.of("bankCardNumber", data.path("bankCardNumber").asText(), "dsaCode", "", "areaCode", "",
								"nationalId", "")));
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
		smartnet = smartnetTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Smartnet.class);
		return utils.getJsonNodeResponse(0, body,
				((ArrayNode) mapper.convertValue(smartnet.getPreChecks(), JsonNode.class).path("preCheck2")).get(0)
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
		Smartnet smartnet = smartnetTemplate.findOne(query, Smartnet.class);
		if (smartnet == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));
		if (LIST_STAGE_COMPLETE.contains(smartnet.getStage().toUpperCase().trim()))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s complete stage", appId)));
		ObjectNode item = mapper.createObjectNode();
		item.put("appId", data.path("appId").asText() + "_string");
		item.put("stage", STAGE_LEAD_DETAILS);
		item.put("status", STATUS_T_RETURN);
		item.put("reasonReturn", "reason_return_string");
		item.put("reasonCancel", "reason_cancel_string");
		item.put("taxCode", "tax_code_string");
		item.put("finalProduct", "final_product_string");
		item.put("loanAmount", "loan_amount_number");
		item.put("tenor", "tenor_number");
		item.put("interestRate", "interest_rate_number");
		item.put("loanAccount", "loan_account_string");
		item.put("disbursementDate", "disbursement_date_dd/MM/yyyy");
		item.put("loanStatus", "loan_status_string");
		item.put("insurance", "insurance_boolean");
		item.put("courierCode", "courier_code_string");
		item.put("lastSubmitDate", "last_submit_date_dd/MM/yyyy");
		item.put("lastUpdateDate", "last_update_date_dd/MM/yyyy");
		item.put("remark", "remark_string");
		item.put("emi", "emi_number");

		Update update = new Update().set("updatedAt", new Date())
				.set("stage", item.get("stage").asText().toUpperCase().trim())
				.set("status", item.get("status").asText().toUpperCase().trim());

		smartnetTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Smartnet.class);

		return utils.getJsonNodeResponse(0, body, item);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonNode addDocuments(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");

		if (data.path("leadId").asInt(-1) == -1)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.leadId required"));

		if (data.path("productCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.productCode not blabnk"));
		if (data.path("schemeCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.schemeCode not blabnk"));
		if (!data.hasNonNull("documents") || mapper.convertValue(data.path("documents"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.documents array required"));
		Query query = Query.query(Criteria.where("leadId").is(data.path("leadId").asInt()));
		Smartnet smartnet = smartnetTemplate.findOne(query, Smartnet.class);
		if (smartnet == null)
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s not exits", data.path("leadId").asInt())));

		if (smartnet.getStage().equals(STAGE_PRECHECK1_DONE))
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s precheck2 not exits %s %s", data.path("leadId").asInt())));

		if (smartnet.getStage().equals(STAGE_PRECHECK2_CHECIKING)) {
			JsonNode lastPreCheck2 = mapper.convertValue(smartnet.getPreChecks().get("preCheck2"), ArrayNode.class)
					.get(0);
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.leadId %s precheck2 %s  %s", data.path("leadId").asInt(),
									lastPreCheck2.path("bankCardNumber").asText(),
									lastPreCheck2.path("data").path("description").asText())));
		}

		if (smartnet.getStage().equals(STAGE_UPLOADED))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s uploaded document at %s ",
							data.path("leadId").asInt(), smartnet.getUpdatedAt())));

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
				.set("status", STATUS_INTRODUCED).set("scheme", data.path("schemeCode").asText())
				.set("product", data.path("productCode").asText())
				.set("schemeFinnOne", documentFinnOne.path("data").path("valueShemeFinnOne").asText())
				.set("productFinnOne", documentFinnOne.path("data").path("valueProductFinnOne").asText())
				.set("filesUpload", filesUpload);
		smartnet = smartnetTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Smartnet.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "createQuickLeadApp", "body",
				convertService.toAppFinnone(smartnet).put("reference_id", body.path("reference_id").asText())));

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(smartnet).put("reference_id", body.path("reference_id").asText())));

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

		Query query = Query.query(Criteria.where("_id").is(body.path("transaction_id").asText()));
		Smartnet smartnet = smartnetTemplate.findOne(query, Smartnet.class);
		if (smartnet == null)
			return utils.getJsonNodeResponse(500, body, mapper.createObjectNode().put("message",
					String.format("id %s smartnet not exits", body.path("transaction_id").asText())));

		HashMap<String, Object> automationResultObject = new HashMap<>();

		automationResultObject.put("createdAt", new Date());

		automationResultObject.put("automationResult", automationResult);

		LinkedList<Map> automationResultsNew = mapper.convertValue(smartnet.getAutomationResults(), LinkedList.class);
		if (automationResultsNew == null)
			automationResultsNew = new LinkedList<Map>();
		automationResultsNew.push(automationResultObject);
		Update update = new Update().set("updatedAt", new Date());
		update.set("automationResults", automationResultsNew);

		if (automationResult.contains(AUTOMATION_QUICKLEAD_PASS)) {
			update.set("appId", body.path("app_id").asText());
			smartnet = smartnetTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
					Smartnet.class);
			final Smartnet smartnetSender = Smartnet.builder().leadId(smartnet.getLeadId()).appId(smartnet.getAppId())
					.build();
			new Thread(() -> {
				try {
					int sendCount = 0;
					do {
						JsonNode result = apiService.pushAppIdOfLeadId(smartnetSender);
						if (result.path("resultCode").asText().equals("200"))
							return;
						log.info("{}", utils.getJsonNodeResponse(1, body, result));
						sendCount++;
						Thread.sleep(5 * 60 * 1000);
					} while (sendCount <= 2);

				} catch (Exception e) {
					log.info("{}", utils.getJsonNodeResponse(0, body,
							mapper.createObjectNode().put("message", e.getMessage())));
				}
			}).start();
		} else {
			if (automationResult.contains(AUTOMATION_RETURNQUERY_PASS)) {
				JsonNode returns = mapper.convertValue(smartnet.getReturns(), JsonNode.class);

				if (returns.hasNonNull("returnQueries")
						&& (mapper.convertValue(returns.path("returnQueries"), ArrayNode.class)).get(0)
								.path("isComplete").asBoolean())
					return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
							String.format("data.appId %s returnQuery is complete", smartnet.getAppId())));

				LinkedList<Map> returnQueriesNew = mapper.convertValue(
						mapper.convertValue(returns.path("returnQueries"), ArrayNode.class), LinkedList.class);
				if (returnQueriesNew == null)
					returnQueriesNew = new LinkedList<Map>();
				returnQueriesNew.get(0).put("isComplete", true);
				returnQueriesNew.get(0).put("updatedAt", new Date());
				update = new Update().set("updatedAt", new Date());
				if (returns.hasNonNull("returnQueues"))
					update.set("returns",
							Map.of("returnQueries", returnQueriesNew, "returnQueues", returns.path("returnQueues")));
				else
					update.set("returns", Map.of("returnQueries", returnQueriesNew));

			}
			smartnet = smartnetTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
					Smartnet.class);
		}
		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "smartnet", "id", smartnet.getId()), "body",
						convertService.toAppDisplay(smartnet)));

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

		Query query = Query.query(Criteria.where("appId").is(appId));
		Smartnet smartnet = smartnetTemplate.findOne(query, Smartnet.class);
		if (smartnet == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));

		if (!smartnet.getStatus().equals(STATUS_T_RETURN))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.appId %s not request resubmit query. current stage %s status %s", appId,
									smartnet.getStage(), smartnet.getStatus())));

		final String productCode = smartnet.getProduct().replace(" ", "_").trim().toLowerCase();
		final String schemeCode = smartnet.getScheme().replace(" ", "_").trim().toLowerCase();

		JsonNode documentFinnOne = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getListDocuments", "reference_id", request.path("reference_id"), "body",
						Map.of("productCode", productCode, "schemeCode", schemeCode)));

		if (documentFinnOne.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("%s %s not found", productCode, schemeCode)));

		HashMap<String, Object> returnQuery = new HashMap<>();
		JsonNode returns = mapper.convertValue(smartnet.getReturns(), JsonNode.class);
		JsonNode document = data.path("document");
		if (document != null) {
			ObjectNode documentsDb = mapper.convertValue(documentFinnOne.path("data").path("documents"),
					ObjectNode.class);
			if (!documentsDb.hasNonNull(document.path("documentCode").asText()))
				return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
						String.format("%s not found", document.path("documentCode").asText())));

			if (returns.hasNonNull("returnQueries")
					&& !(mapper.convertValue(returns.path("returnQueries"), ArrayNode.class)).get(0).path("isComplete")
							.asBoolean())
				return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
						String.format("data.appId %s returnQuery not complete", smartnet.getAppId())));

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
		update.set("stage", STATUS_INTRODUCED);

		smartnet = smartnetTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Smartnet.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "deResponseQuery", "body",
				convertService.toReturnQueryFinnone(smartnet).put("reference_id", body.path("reference_id").asText())));

		return utils.getJsonNodeResponse(0, body, null);
	}

}