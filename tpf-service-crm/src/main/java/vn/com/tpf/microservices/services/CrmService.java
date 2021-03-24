package vn.com.tpf.microservices.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Crm;
import vn.com.tpf.microservices.models.CrmField;
import vn.com.tpf.microservices.models.CrmWaiveField;
import vn.com.tpf.microservices.models.CrmFieldKH;
import vn.com.tpf.microservices.utils.Utils;

@Service
public class CrmService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final String STAGE_PRECHECK1_DONE = "PRECHECK1ED";
	private final String STAGE_PRECHECK2_CHECIKING = "PRECHECK2_CHECKING";
	private final String STAGE_PRECHECK2_DONE = "PRECHECK2_DONE";
	private final String STAGE_UPLOADED = "UPLOADED";
	private final String STAGE_LEAD_DETAILS = "LEAD_DETAILS";
	private final String STAGE_LOGIN_ACCEPTANCE = "LOGIN_ACCEPTANCE";
	private final List<String> LIST_STATUS_COMPLETE = Arrays.asList("Cancellation".toUpperCase().trim(),
			"Rejection".toUpperCase().trim());
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

	private final String STAGE_SUBMIT = "CREDIT_APPROVAL";
	private final String STATUS_SUBMIT = "PRE_APPROVAL";

	private final String STATUS_RESUBMITING = "RESPONSE_QUERY_UPLOADING";

	private final String KEY_LAST_UPDATE_DATE = "lastUpdateDate";
	private final String KEY_USER_NAME = "userName";
	private final String KEY_STAGE = "stage";
	private final String KEY_STATUS = "status";
	private final String STATUS_SEND_BACK = "SEND_BACK";
	private final String STAGE_SEND_BACK_UPLOADING_HAS_ACCA = "SEND_BACK_UPLOADING_HAS_ACCA";
	private final String STAGE_SEND_BACK_UPLOADING = "SEND_BACK_UPLOADING";
	private final int At_Company = 1;
	private final int At_House = 2;
	private final int ALL = 3;
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
	private MongoTemplate crmTemplate;

	@Autowired
	private MongoTemplate crmwaivefieldTemplate;

	@Autowired
	private MongoTemplate crmfieldTemplate;

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
		Crm crm = crmTemplate.findOne(Query.query(Criteria.where("leadId").is(leadId)), Crm.class);
		if (crm != null)
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
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode());
		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddressFinnOne", "reference_id", body.path("reference_id"), "param",
						Map.of("areaCode", data.path("district").asText())));

		if (address.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.district not found"));
		if (!address.path("data").path("postCode").asText().equals(data.path("city").asText()))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.city not found"));

		crm = Crm.builder().leadId(leadId).firstName(data.path("firstName").asText())
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
		System.out.println(preCheckResult.path("data"));
		

		crm.setPreChecks(Map.of("preCheck1",
				Map.of("createdAt", new Date(), "data", mapper.convertValue(preCheckResult.path("data"), Map.class))));
		crm.setStage(STAGE_PRECHECK1_DONE);
		crm.setStatus(STATUS_PRE_APPROVAL);
		crmTemplate.save(crm);
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
		Crm crm = crmTemplate.findOne(query, Crm.class);
		if (crm == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s not exits", leadId)));
		JsonNode precheks = mapper.convertValue(crm.getPreChecks(), JsonNode.class);

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
								"nationalId", crm.getNationalId())));
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
		crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Crm.class);
		return utils.getJsonNodeResponse(0, body,
				((ArrayNode) mapper.convertValue(crm.getPreChecks(), JsonNode.class).path("preCheck2")).get(0)
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
		Crm crm = crmTemplate.findOne(query, Crm.class);
		if (crm == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));

		if (LIST_STATUS_COMPLETE.contains(crm.getStatus().toUpperCase().trim()))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s complete with status %s",
							appId, crm.getStatus().toUpperCase().trim())));
		JsonNode item = rabbitMQService.sendAndReceive("tpf-service-esb", Map.of("func", "getAppInfo", "body", body));

		if (item.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s waiting sync data", appId)));

		Update update = new Update().set("updatedAt", new Date());
		final boolean updateStageAndStatus = (crm.getViewLastUpdated() == null
				|| !item.path("data").path(KEY_LAST_UPDATE_DATE).asText().trim().toUpperCase()
						.equals(crm.getViewLastUpdated().trim().toUpperCase())
				|| !item.path("data").path(KEY_STATUS).asText().trim().toUpperCase()
						.equals(crm.getStatus().trim().toUpperCase())
				|| !item.path("data").path(KEY_STAGE).asText().trim().toUpperCase()
				.equals(crm.getStage().trim().toUpperCase()));

		if (updateStageAndStatus)
			update.set("viewLastUpdated", item.path("data").path(KEY_LAST_UPDATE_DATE).asText().trim().toUpperCase())
					.set("stage", item.path("data").path(KEY_STAGE).asText().toUpperCase().trim())
					.set("status", item.path("data").path(KEY_STATUS).asText().toUpperCase().trim());

		if (!item.path("data").path(KEY_STAGE).asText().toUpperCase().trim().equals(STAGE_SALES_QUEUE))
			update.set("userCreatedQueue", item.path("data").path(KEY_USER_NAME).asText());

		if (updateStageAndStatus) {
			crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
					Crm.class);
			rabbitMQService.send("tpf-service-app",
					Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
							Map.of("project", "crm", "id", crm.getId()), "body",
							convertService.toAppStatus(crm)));
		}
		((ObjectNode) item.path("data")).remove(KEY_USER_NAME);

		return utils.getJsonNodeResponse(0, body, item.path("data"));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonNode addDocumentsWithCustId(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");

		final long leadId = data.path("leadId").asLong(0);

		if (data.path("branch").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.branch not null"));

		if (data.path("chanel").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.chanel not null"));

		if (data.path("productCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.productCode not blank"));
		if (data.path("schemeCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.schemeCode not blank"));
		if (data.path("custId").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.custId not blank"));
		if (!data.hasNonNull("documents") || mapper.convertValue(data.path("documents"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.documents array required"));

		Query query = Query.query(Criteria.where("leadId").is(leadId));
		Crm crm = crmTemplate.findOne(query, Crm.class);
		if (crm == null)
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s not exits", data.path("leadId").asInt())));

		if (crm.getStage().equals(STAGE_PRECHECK1_DONE))
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s precheck2 not exits", data.path("leadId").asInt())));

		if (crm.getStage().equals(STAGE_PRECHECK2_CHECIKING)) {
			JsonNode lastPreCheck2 = mapper.convertValue(crm.getPreChecks().get("preCheck2"), ArrayNode.class)
					.get(0);
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.leadId %s precheck2 %s  %s", data.path("leadId").asInt(),
									lastPreCheck2.path("bankCardNumber").asText(),
									lastPreCheck2.path("data").path("description").asText())));
		}

		if (crm.getStage().equals(STAGE_UPLOADED) || crm.getFilesUpload() != null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s uploaded document at %s ",
							data.path("leadId").asInt(), crm.getUpdatedAt())));
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
						String.format("document %s required", document.path("code").asText())));
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
				.set("product", data.path("productCode").asText()).set("chanel", data.path("chanel").asText()).set("branch", data.path("branch").asText())
				.set("schemeFinnOne", documentFinnOne.path("data").path("valueShemeFinnOne").asText())
				.set("productFinnOne", documentFinnOne.path("data").path("valueProductFinnOne").asText())
				.set("filesUpload", filesUpload)
				.set("neoCustID", data.path("custId").asText())
				.set("cifNumber", data.path("cifNumber").asText())
				.set("idNumber", data.path("idNumber").asText());
		crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Crm.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "createQuickLeadAppWithCustId", "body",
				convertService.toAppFinnone(crm).put("reference_id", body.path("reference_id").asText())));

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(crm).put("reference_id", body.path("reference_id").asText())));

		return utils.getJsonNodeResponse(0, body, null);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonNode addDocumentsWithCustIdAndFullApp(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");

		final long leadId = data.path("leadId").asLong(0);

		if (data.path("branch").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.branch not null"));

		if (data.path("chanel").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.chanel not null"));

		if (data.path("productCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.productCode not blank"));
		if (data.path("schemeCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.schemeCode not blank"));
		if (data.path("custId").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.custId not blank"));
		if (!data.hasNonNull("documents") || mapper.convertValue(data.path("documents"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.documents array required"));
		if (!data.path("fullInfoApp").hasNonNull("references") || mapper.convertValue(data.path("fullInfoApp").path("references"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.references array required"));
		if (data.path("fullInfoApp").path("primaryAddress").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.primaryAddress not blank"));
		if (data.path("fullInfoApp").path("phoneNumber").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.phoneNumber not blank"));
		if (data.path("fullInfoApp").path("incomeExpense").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.incomeExpense not blank"));
		if (data.path("fullInfoApp").path("modeOfPayment").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.modeOfPayment not blank"));
		if (data.path("fullInfoApp").path("dayOfSalaryPayment").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.dayOfSalaryPayment not blank"));
		if (data.path("fullInfoApp").path("loanApplicationType").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanApplicationType not blank"));
		if (data.path("fullInfoApp").path("loanAmountRequested").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanAmountRequested not blank"));
		if (data.path("fullInfoApp").path("requestedTenure").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.requestedTenure not blank"));
		if (data.path("fullInfoApp").path("interestRate").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.interestRate not blank"));
		if (data.path("fullInfoApp").path("saleAgentCodeLoanDetails").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.saleAgentCodeLoanDetails not blank"));
//		if (data.path("fullInfoApp").path("vapProduct").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "data.vapProduct not blank"));
//		if (data.path("fullInfoApp").path("vapTreatment").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "data.vapTreatment not blank"));
//		if (data.path("fullInfoApp").path("insuranceCompany").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "data.insuranceCompany not blank"));
		if (data.path("fullInfoApp").path("loanPurpose").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanPurpose not blank"));
		if (data.path("fullInfoApp").path("numberOfDependents").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.numberOfDependents not blank"));
		if (data.path("fullInfoApp").path("monthlyRental").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.monthlyRental not blank"));
		if (data.path("fullInfoApp").path("houseOwnership").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.houseOwnership not blank"));
		if (data.path("fullInfoApp").path("newBankCardNumber").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.newBankCardNumber not blank"));
		if (data.path("fullInfoApp").path("saleAgentCodeDynamicForm").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.saleAgentCodeDynamicForm not blank"));
		if (data.path("fullInfoApp").path("courierCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.courierCode not blank"));
		if (data.path("fullInfoApp").path("maximumInterestedRate").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.maximumInterestedRate not blank"));
		if (!data.path("fullInfoApp").hasNonNull("identifications") || mapper.convertValue(data.path("fullInfoApp").path("identifications"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.identifications array required"));
		if (data.path("fullInfoApp").path("remarksDynamicForm").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.remarksDynamicForm not blank"));
		if (data.path("fullInfoApp").path("maritalStatus").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.maritalStatus not blank"));
		if (data.path("fullInfoApp").path("employmentName").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.employmentName not blank"));

		Query query = Query.query(Criteria.where("leadId").is(leadId));
		Crm crm = crmTemplate.findOne(query, Crm.class);
		if (crm == null)
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s not exits", data.path("leadId").asInt())));

		if (crm.getStage().equals(STAGE_PRECHECK1_DONE))
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s precheck2 not exits", data.path("leadId").asInt())));

		if (crm.getStage().equals(STAGE_PRECHECK2_CHECIKING)) {
			JsonNode lastPreCheck2 = mapper.convertValue(crm.getPreChecks().get("preCheck2"), ArrayNode.class)
					.get(0);
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.leadId %s precheck2 %s  %s", data.path("leadId").asInt(),
									lastPreCheck2.path("bankCardNumber").asText(),
									lastPreCheck2.path("data").path("description").asText())));
		}

		if (crm.getStage().equals(STAGE_UPLOADED) || crm.getFilesUpload() != null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s uploaded document at %s ",
							data.path("leadId").asInt(), crm.getUpdatedAt())));
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
						String.format("document %s required", document.path("code").asText())));
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
		ArrayNode addresses = mapper.convertValue(data.path("fullInfoApp").path("addresses"), ArrayNode.class);
		List<HashMap> addressesUpload = new ArrayList<HashMap>();
		if (addresses != null ) {
			if (addresses.size() != 0) {
				for(JsonNode address : addresses) {
					HashMap<String, String> addressUpload = new HashMap<>();
					addressUpload.put("addressType", address.path("addressType").asText());
					addressUpload.put("addressLine1", address.path("addressLine1").asText());
					addressUpload.put("addressLine2", address.path("addressLine2").asText());
					addressUpload.put("addressLine3", address.path("addressLine3").asText());
					addressUpload.put("area", address.path("area").asText());
					addressUpload.put("city", address.path("city").asText());
					addressUpload.put("phoneNumber", address.path("phoneNumber").asText());
					addressesUpload.add(addressUpload);
				}
			}
		}
		ArrayNode references = mapper.convertValue(data.path("fullInfoApp").path("references"), ArrayNode.class);
		List<HashMap> referencesUpload = new ArrayList<HashMap>();
		for(JsonNode reference : references) {
			HashMap<String, String> referenceUpload = new HashMap<>();
			referenceUpload.put("name", reference.path("name").asText());
			referenceUpload.put("relationship", reference.path("relationship").asText());
			referenceUpload.put("phoneNumber", reference.path("phoneNumber").asText());
			referencesUpload.add(referenceUpload);
		}

		ArrayNode familyes = mapper.convertValue(data.path("fullInfoApp").path("family"), ArrayNode.class);
		List<HashMap> familyesUpload = new ArrayList<HashMap>();
		if (familyes != null ) {
			if (familyes.size() != 0) {
				for(JsonNode family : familyes) {
					HashMap<String, String> familyUpload = new HashMap<>();
					familyUpload.put("memberName", family.path("memberName").asText());
					familyUpload.put("phoneNumber", family.path("phoneNumber").asText());
					familyUpload.put("relationship", family.path("relationship").asText());
					familyesUpload.add(familyUpload);
				}
			}
		}

		ArrayNode identifications = mapper.convertValue(data.path("fullInfoApp").path("identifications"), ArrayNode.class);
		List<HashMap> identificationsUpload = new ArrayList<HashMap>();
		if (identifications != null ) {
			if (identifications.size() != 0) {
				for(JsonNode identification : identifications) {
					HashMap<String, String> identificationUpload = new HashMap<>();
					identificationUpload.put("identificationType", identification.path("identificationType").asText());
					identificationUpload.put("identificationNumber", identification.path("identificationNumber").asText());
					identificationUpload.put("issuingCountry", identification.path("issuingCountry").asText());
					identificationUpload.put("placeOfIssue", identification.path("placeOfIssue").asText());
					identificationUpload.put("issueDate", identification.path("issueDate").asText());
					identificationUpload.put("expiryDate", identification.path("expiryDate").asText());
					identificationsUpload.add(identificationUpload);
				}
			}
		}

		JsonNode customerCategoryCode = rabbitMQService.sendAndReceive("tpf-service-finnone",
				Map.of("func", "getEducationField", "custid", data.path("custId").asText()));
		if (customerCategoryCode.path("status").asInt(0) != 200) {
			return utils.getJsonNodeResponse(500, body, customerCategoryCode.path("data"));
		}

		Update update = new Update().set("updatedAt", new Date()).set("stage", STAGE_UPLOADED)
				.set("status", STATUS_PRE_APPROVAL).set("scheme", data.path("schemeCode").asText())
				.set("product", data.path("productCode").asText()).set("chanel", data.path("chanel").asText()).set("branch", data.path("branch").asText())
				.set("schemeFinnOne", documentFinnOne.path("data").path("valueShemeFinnOne").asText())
				.set("productFinnOne", documentFinnOne.path("data").path("valueProductFinnOne").asText())
				.set("filesUpload", filesUpload)
				.set("neoCustID", data.path("custId").asText())
				.set("cifNumber", data.path("cifNumber").asText())
				.set("idNumber", data.path("idNumber").asText())
				.set("firstName", data.path("fullInfoApp").path("firstName").asText())
				.set("middleName", data.path("fullInfoApp").path("middleName").asText())
				.set("lastName", data.path("fullInfoApp").path("lastName").asText())
				.set("gender", data.path("fullInfoApp").path("gender").asText())
				.set("dateOfBirth", data.path("fullInfoApp").path("dateOfBirth").asText())
				.set("maritalStatus", data.path("fullInfoApp").path("maritalStatus").asText())
				.set("family", familyesUpload)
				.set("identifications", identificationsUpload)
				.set("primaryAddress", data.path("fullInfoApp").path("primaryAddress").asText())
				.set("phoneNumber", data.path("fullInfoApp").path("phoneNumber").asText())
				.set("employmentName", data.path("fullInfoApp").path("employmentName").asText())
				.set("incomeExpense", data.path("fullInfoApp").path("incomeExpense").asText())
				.set("amount", data.path("fullInfoApp").path("amount").asText())
				.set("modeOfPayment", data.path("fullInfoApp").path("modeOfPayment").asText())
				.set("dayOfSalaryPayment", data.path("fullInfoApp").path("dayOfSalaryPayment").asText())
				.set("loanApplicationType", data.path("fullInfoApp").path("loanApplicationType").asText())
				.set("loanAmountRequested", data.path("fullInfoApp").path("loanAmountRequested").asText())
				.set("requestedTenure", data.path("fullInfoApp").path("requestedTenure").asText())
				.set("interestRate", data.path("fullInfoApp").path("interestRate").asText())
				.set("saleAgentCodeLoanDetails", data.path("fullInfoApp").path("saleAgentCodeLoanDetails").asText())
				.set("vapProduct", data.path("fullInfoApp").path("vapProduct").asText())
				.set("vapTreatment", data.path("fullInfoApp").path("vapTreatment").asText())
				.set("insuranceCompany", data.path("fullInfoApp").path("insuranceCompany").asText())
				.set("loanPurpose", data.path("fullInfoApp").path("loanPurpose").asText())
				.set("loanPurposeOther", data.path("fullInfoApp").path("loanPurposeOther").asText())
				.set("numberOfDependents", data.path("fullInfoApp").path("numberOfDependents").asText())
				.set("monthlyRental", data.path("fullInfoApp").path("monthlyRental").asText())
				.set("houseOwnership", data.path("fullInfoApp").path("houseOwnership").asText())
				.set("newBankCardNumber", data.path("fullInfoApp").path("newBankCardNumber").asText())
				.set("remarksDynamicForm", data.path("fullInfoApp").path("remarksDynamicForm").asText())
				.set("saleAgentCodeDynamicForm", data.path("fullInfoApp").path("saleAgentCodeDynamicForm").asText())
				.set("courierCode", data.path("fullInfoApp").path("courierCode").asText())
				.set("maximumInterestedRate", data.path("fullInfoApp").path("maximumInterestedRate").asText())
				.set("addresses", addressesUpload)
				.set("references", referencesUpload)
				.set("customerCategoryCode", customerCategoryCode.path("data").path("description").asText())
				;
		crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Crm.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "createQuickLeadAppWithCustId", "body",
				convertService.toAppFinnoneWithFullApp(crm).put("reference_id", body.path("reference_id").asText())));

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(crm).put("reference_id", body.path("reference_id").asText())));

		return utils.getJsonNodeResponse(0, body, null);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonNode addDocuments(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");

		final long leadId = data.path("leadId").asLong(0);

		if (data.path("branch").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.branch not null"));

		if (data.path("chanel").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.chanel not null"));

		if (data.path("productCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.productCode not blank"));
		if (data.path("schemeCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.schemeCode not blank"));
		if (!data.hasNonNull("documents") || mapper.convertValue(data.path("documents"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.documents array required"));

		Query query = Query.query(Criteria.where("leadId").is(leadId));
		Crm crm = crmTemplate.findOne(query, Crm.class);
		if (crm == null)
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s not exits", data.path("leadId").asInt())));

		if (crm.getStage().equals(STAGE_PRECHECK1_DONE))
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s precheck2 not exits", data.path("leadId").asInt())));

		if (crm.getStage().equals(STAGE_PRECHECK2_CHECIKING)) {
			JsonNode lastPreCheck2 = mapper.convertValue(crm.getPreChecks().get("preCheck2"), ArrayNode.class)
					.get(0);
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.leadId %s precheck2 %s  %s", data.path("leadId").asInt(),
									lastPreCheck2.path("bankCardNumber").asText(),
									lastPreCheck2.path("data").path("description").asText())));
		}

		if (crm.getStage().equals(STAGE_UPLOADED) || crm.getFilesUpload() != null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s uploaded document at %s ",
							data.path("leadId").asInt(), crm.getUpdatedAt())));
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
						String.format("document %s required", document.path("code").asText())));
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
				.set("product", data.path("productCode").asText()).set("chanel", data.path("chanel").asText()).set("branch", data.path("branch").asText())
				.set("schemeFinnOne", documentFinnOne.path("data").path("valueShemeFinnOne").asText())
				.set("productFinnOne", documentFinnOne.path("data").path("valueProductFinnOne").asText())
				.set("filesUpload", filesUpload);
		crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Crm.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "createQuickLeadApp", "body",
				convertService.toAppFinnone(crm).put("reference_id", body.path("reference_id").asText())));

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(crm).put("reference_id", body.path("reference_id").asText())));

		return utils.getJsonNodeResponse(0, body, null);
	}

	public JsonNode updateAutomation(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		if (!request.hasNonNull("body"))

			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "body not null"));
		if (body.path("transaction_id").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "body.transaction_id not null"));
		switch (body.path("transaction_id").asText()) {
		case "transaction_waive_field":
			return updateAutomationWaveField(request);
		case "transaction_submit_field":
			return updateAutomationSubmitField(request);
		default:
			return updateAutomationCrm(request);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonNode updateAutomationWaveField(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		if (!body.path("data").isArray())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "body.data not null"));

		ArrayNode apps = mapper.convertValue(body.path("data"), ArrayNode.class);
		for (JsonNode app : apps) {
			if (app.path("appId").asText().isBlank())
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message", "app_id not null"));

			if (app.path("automation_result").asText().isBlank())
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message", "automation_result not null"));
			String automationResult = app.path("automation_result").asText().toUpperCase().replace(" ", "_").trim();

			Query query = Query.query(Criteria.where("appId").is(app.path("appId").asText()));

			CrmWaiveField crmWaiveField = crmwaivefieldTemplate.findOne(query, CrmWaiveField.class);
			if (crmWaiveField == null)
				return utils.getJsonNodeResponse(500, body, mapper.createObjectNode().put("message",
						String.format("id %s crmWaiveField not exits", app.path("appId").asText())));

			HashMap<String, Object> automationResultObject = new HashMap<>();

			automationResultObject.put("createdAt", new Date());

			automationResultObject.put("automationResult", automationResult);

			LinkedList<Map> automationResultsNew = mapper.convertValue(crmWaiveField.getAutomationResults(),
					LinkedList.class);
			if (automationResultsNew == null)
				automationResultsNew = new LinkedList<Map>();
			automationResultsNew.push(automationResultObject);
			Update update = new Update().set("updatedAt", new Date());
			update.set("automationResults", automationResultsNew);

			crmWaiveField = crmwaivefieldTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), CrmWaiveField.class);
			if (!automationResult.isBlank())
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
								Map.of("project", "crm", "id", crmWaiveField.getId()), "body",
								convertService.toAppAutomationField(crmWaiveField)));
		}

		return utils.getJsonNodeResponse(0, request, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonNode updateAutomationSubmitField(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		if (!body.path("data").isArray())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "body.data not null"));

		ArrayNode apps = mapper.convertValue(body.path("data"), ArrayNode.class);
		for (JsonNode app : apps) {
			if (app.path("appId").asText().isBlank())
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message", "app_id not null"));

			if (app.path("automation_result").asText().isBlank())
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message", "automation_result not null"));
			String automationResult = app.path("automation_result").asText().toUpperCase().replace(" ", "_").trim();

			Query query = Query.query(Criteria.where("appId").is(app.path("appId").asText()));

			CrmField crmfield = crmfieldTemplate.findOne(query, CrmField.class);
			if (crmfield == null)
				return utils.getJsonNodeResponse(500, body, mapper.createObjectNode().put("message",
						String.format("id %s not exits", app.path("appId").asText())));

			HashMap<String, Object> automationResultObject = new HashMap<>();

			automationResultObject.put("createdAt", new Date());

			automationResultObject.put("automationResult", automationResult);

			LinkedList<Map> automationResultsNew = mapper.convertValue(crmfield.getAutomationResults(),
					LinkedList.class);
			if (automationResultsNew == null)
				automationResultsNew = new LinkedList<Map>();
			automationResultsNew.push(automationResultObject);
			Update update = new Update().set("updatedAt", new Date());
			update.set("automationResults", automationResultsNew);

			crmfield = crmfieldTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), CrmField.class);
			if (!automationResult.isBlank())
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
								Map.of("project", "crm", "id", crmfield.getId()), "body",
								convertService.toAppAutomationSubmitField(crmfield)));
		}

		return utils.getJsonNodeResponse(0, request, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonNode updateAutomationCrm(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
//		if (body.path("app_id").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "body.app_id not null"));
//		if (body.path("app_id").asText().equals("UNKNOW"))
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "body.app_id not UNKNOW"));
		if (body.path("automation_result").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "body.automation_result not null"));
		String automationResult = body.path("automation_result").asText().toUpperCase().replace(" ", "_").trim();
		boolean updateStatus = false;

		Query query = Query.query(Criteria.where("_id").is(body.path("transaction_id").asText()));
		Crm crm = crmTemplate.findOne(query, Crm.class);
		if (crm == null)
			return utils.getJsonNodeResponse(500, body, mapper.createObjectNode().put("message",
					String.format("id %s crm not exits", body.path("transaction_id").asText())));

		HashMap<String, Object> automationResultObject = new HashMap<>();

		automationResultObject.put("createdAt", new Date());

		automationResultObject.put("automationResult", automationResult);

		LinkedList<Map> automationResultsNew = mapper.convertValue(crm.getAutomationResults(), LinkedList.class);
		if (automationResultsNew == null)
			automationResultsNew = new LinkedList<Map>();
		automationResultsNew.push(automationResultObject);
		Update update = new Update().set("updatedAt", new Date());
		update.set("automationResults", automationResultsNew);

		if (automationResult.contains(AUTOMATION_QUICKLEAD_PASS)) {
			update.set("appId", body.path("app_id").asText());
			update.set("stage", STAGE_LEAD_DETAILS);
			crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
					Crm.class);
			final Crm crmSender = Crm.builder().leadId(crm.getLeadId()).appId(crm.getAppId()).automationDescription("Success")
					.build();
			new Thread(() -> {
				try {
					int sendCount = 0;
					do {
						JsonNode result = apiService.pushAppIdOfLeadId(crmSender,
								request.path("reference_id").asText());
						if (result.path("resultCode").asText().equals("200")) {
							return;
						}
						sendCount++;
						Thread.sleep(5 * 60 * 1000);
					} while (sendCount >= 2);
				} catch (Exception e) {
					log.info("{}", utils.getJsonNodeResponse(0, body,
							mapper.createObjectNode().put("message", e.getMessage())));
				}
			}).start();
		}
		if (automationResult.contains(AUTOMATION_QUICKLEAD_FAILED)) {
			update.set("appId", body.path("app_id").asText());
			update.set("stage", STAGE_QUICKLEAD_FAILED_AUTOMATION);
			crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
					Crm.class);
			final Crm crmSender ;
			if (body.path("app_id").asText().isBlank() || body.path("app_id").asText().equals("UNKNOWN")) {
				 crmSender = Crm.builder().leadId(crm.getLeadId()).appId(crm.getAppId()).automationDescription("FAIELD - Mất kết nối, Vui lòng retry lại")
						.build();
			} else {
				 crmSender = Crm.builder().leadId(crm.getLeadId()).appId(crm.getAppId()).automationDescription("FAIELD - Vui lòng kiểm tra data và retry lại hoặc liên hệ IT để hỗ trợ")
						.build();
			}
			new Thread(() -> {
				try {
					int sendCount = 0;
					do {
						JsonNode result = apiService.pushAppIdOfLeadId(crmSender,
								request.path("reference_id").asText());
						if (result.path("resultCode").asText().equals("200")) {
							return;
						}
						sendCount++;
						Thread.sleep(5 * 60 * 1000);
					} while (sendCount >= 2);
				} catch (Exception e) {
					log.info("{}", utils.getJsonNodeResponse(0, body,
							mapper.createObjectNode().put("message", e.getMessage())));
				}
			}).start();
		}

		if (automationResult.contains(AUTOMATION_RETURNQUERY_PASS)) {
			JsonNode returns = mapper.convertValue(crm.getReturns(), JsonNode.class);

			if (returns.hasNonNull("returnQueries")
					&& (mapper.convertValue(returns.path("returnQueries"), ArrayNode.class)).get(0).path("isComplete")
							.asBoolean())
				return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
						String.format("data.appId %s returnQuery is complete", crm.getAppId())));

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
			JsonNode returns = mapper.convertValue(crm.getReturns(), JsonNode.class);

			if (returns.hasNonNull("returnQueues")
					&& (mapper.convertValue(returns.path("returnQueues"), ArrayNode.class)).get(0).path("isComplete")
							.asBoolean())
				return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
						String.format("data.appId %s returnQueue is complete", crm.getAppId())));

			LinkedList<Map> returnQueuesNew = mapper
					.convertValue(mapper.convertValue(returns.path("returnQueues"), ArrayNode.class), LinkedList.class);
			if (returnQueuesNew == null)
				returnQueuesNew = new LinkedList<Map>();
			returnQueuesNew.get(0).put("isComplete", true);
			returnQueuesNew.get(0).put("updatedAt", new Date());
			update.set("updatedAt", new Date());
			update.set("returns.returnQueues", returnQueuesNew);

			final String currentStage = crm.getStage();
			if (currentStage.equals(STAGE_SALES_QUEUE_UPLOADING_HAS_ACCA)
					|| currentStage.equals(STAGE_SALES_QUEUE_HAS_ACCA_FAILED))
				update.set("stage", STAGE_LEAD_DETAILS);
			if (currentStage.equals(STAGE_SALES_QUEUE_UPLOADING) || currentStage.equals(STAGE_SALES_QUEUE_FAILED))
				update.set("stage", STAGE_LOGIN_ACCEPTANCE);
		}

		if (automationResult.contains(AUTOMATION_SALEQUEUE_FAILED)) {
			final String currentStage = crm.getStage();
			if (currentStage.equals(STAGE_SALES_QUEUE_UPLOADING_HAS_ACCA))
				update.set("stage", STAGE_SALES_QUEUE_HAS_ACCA_FAILED);
			if (currentStage.equals(STAGE_SALES_QUEUE_UPLOADING))
				update.set("stage", STAGE_SALES_QUEUE_FAILED);
		}

		crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Crm.class);
		if (!automationResult.isBlank())
			rabbitMQService.send("tpf-service-app",
					Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
							Map.of("project", "crm", "id", crm.getId()), "body",
							convertService.toAppAutomation(crm, updateStatus)));
		return utils.getJsonNodeResponse(0, request, null);
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
				"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*(:[0-9]{1,5})?(\\/.*)?$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.document.documentUrlDownload not valid"));
		if (!data.path("document").path("documentCode").asText().toLowerCase().trim().replace(" ", "_")
				.equals(DOCUMENT_CODE_ACCA))
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("data.document.documentCode required %s", DOCUMENT_CODE_ACCA)));

		Query query = Query.query(Criteria.where("appId").is(appId));
		Crm crm = crmTemplate.findOne(query, Crm.class);
		if (crm == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));

		if (!crm.getStatus().equals(STATUS_T_RETURN))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.appId %s not request resubmit query. current stage %s status %s", appId,
									crm.getStage(), crm.getStatus())));

		final String productCode = crm.getProduct().replace(" ", "_").trim().toLowerCase();
		final String schemeCode = crm.getScheme().replace(" ", "_").trim().toLowerCase();

		JsonNode documentFinnOne = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getListDocuments", "reference_id", request.path("reference_id"), "body",
						Map.of("productCode", productCode, "schemeCode", schemeCode)));

		if (documentFinnOne.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("%s %s not found", productCode, schemeCode)));

		HashMap<String, Object> returnQuery = new HashMap<>();
		JsonNode returns = mapper.convertValue(crm.getReturns(), JsonNode.class);
		if (returns.hasNonNull("returnQueries")
				&& !(mapper.convertValue(returns.path("returnQueries"), ArrayNode.class)).get(0).path("isComplete")
						.asBoolean())
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.appId %s returnQuery not complete", crm.getAppId())));

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
					Map.of("returnQueries", returnQueriesNew, "returnQueues", mapper.convertValue(returns.path("returnQueues"), ArrayNode.class)));
		else
			update.set("returns", Map.of("returnQueries", returnQueriesNew));
		update.set("status", STATUS_RESUBMITING);

		crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Crm.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "deResponseQuery", "body",
				convertService.toReturnQueryFinnone(crm).put("reference_id", body.path("reference_id").asText())));

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "crm", "id", crm.getId()), "body",
						convertService.toAppStatus(crm)));

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
				// \\.[a-z]{2,5}
				if (!document.path("documentUrlDownload").asText().matches(
						"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*(:[0-9]{1,5})?(\\/.*)?$"))

					return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
							String.format("data.documents[%s].documentUrlDownload not valid", index)));
				if (document.path("documentCode").asText().toLowerCase().trim().replace(" ", "_")
						.equals(DOCUMENT_CODE_ACCA))
					hasDocumentCodeACCA = true;

			}
		}

		Query query = Query.query(Criteria.where("appId").is(appId));
		Crm crm = crmTemplate.findOne(query, Crm.class);
		if (crm == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));

		if (!crm.getStage().equals(STAGE_SALES_QUEUE))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.appId %s not request resubmit queue. current stage %s status %s", appId,
									crm.getStage(), crm.getStatus())));

		final String productCode = crm.getProduct().replace(" ", "_").trim().toLowerCase();
		final String schemeCode = crm.getScheme().replace(" ", "_").trim().toLowerCase();

		JsonNode documentFinnOne = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getListDocuments", "reference_id", request.path("reference_id"), "body",
						Map.of("productCode", productCode, "schemeCode", schemeCode)));

		if (documentFinnOne.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("%s %s not found", productCode, schemeCode)));

		JsonNode returns = mapper.convertValue(crm.getReturns(), JsonNode.class);
		if (returns.hasNonNull("returnQueues") && !(mapper.convertValue(returns.path("returnQueues"), ArrayNode.class))
				.get(0).path("isComplete").asBoolean())
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.appId %s returnQueue not complete", crm.getAppId())));

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

		crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Crm.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "deSaleQueue", "body",
				convertService.toSaleQueueFinnone(crm).put("reference_id", body.path("reference_id").asText())));

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "crm", "id", crm.getId()), "body",
						convertService.toAppStage(crm)));

		return utils.getJsonNodeResponse(0, body, null);
	}

	public JsonNode createField(JsonNode request) throws Exception {
		ArrayNode apps = mapper.convertValue(request.path("body").path("data"), ArrayNode.class);
		if (apps.isNull())
			return utils.getJsonNodeResponse(499, request.path("body").path("data"),
					mapper.createObjectNode().put("message", "data not null"));
		ArrayList<CrmField> crmFields = new ArrayList<CrmField>();
		Set<String> set = new HashSet<String>();
		for (JsonNode app : apps) {
			if (set.add(app.path("appId").asText()) == false) {
				return utils.getJsonNodeResponse(499, app, mapper.createObjectNode().put("message",
						String.format("data.appId duplicates %s", app.path("appId").asText())));
			}
		}

		for (JsonNode app : apps) {
			final String appId = app.path("appId").asText();

			if (appId.isBlank())
				return utils.getJsonNodeResponse(499, app,
						mapper.createObjectNode().put("message", "data.appId not null"));
			if (app.path("comment").asText().isBlank())
				return utils.getJsonNodeResponse(499, app,
						mapper.createObjectNode().put("message", "data.comment not null"));
			final int fieldType = app.path("fieldType").asInt();

			if (!(fieldType == At_Company || fieldType == (At_House) || fieldType == (ALL))) {
				return utils.getJsonNodeResponse(499, app,
						mapper.createObjectNode().put("message", "data.fieldType not valid"));
			}
			CrmField crmF = mapper.convertValue(app, CrmField.class);

//			mobilityF =	MobilityField.builder().appId(appId).build();

			crmFields.add(crmF);
		}

		JsonNode getDataFieldsDB = rabbitMQService.sendAndReceive("tpf-service-finnone", Map.of("func", "getDataFields",
				"reference_id", request.path("body").path("reference_id"), "body", Map.of("apps", crmFields)));

		if (getDataFieldsDB.path("status").asInt(0) != 200) {
			return utils.getJsonNodeResponse(1, request.path("body"), getDataFieldsDB.path("data"));
		}

		ArrayList<CrmFieldKH> crmfieldSender = new ArrayList<CrmFieldKH>();

		if (getDataFieldsDB.path("data").isArray()) {
			for (JsonNode item : getDataFieldsDB.path("data")) {
				for (CrmField crmField : crmFields) {
					if (crmField.getAppId().equals(item.path("appId").asText())) {
						CrmField convert = mapper.convertValue(item, CrmField.class);
						convert.setComment(crmField.getComment());
						convert.setKycNotes(crmField.getKycNotes());
						convert.setFieldType(crmField.getFieldType());
						if ((convert.getAppStage().toUpperCase().equals("FII"))) {
							Query queryMon = Query.query(Criteria.where("appId").is(convert.getAppId()));
							CrmField crmfieldMon = crmTemplate.findOne(queryMon, CrmField.class);
							Update update = new Update();
							if (crmfieldMon != null) {
								update.set("fullName", convert.getFullName());
								update.set("phone", convert.getPhone());
								update.set("dateOfBirth", convert.getDateOfBirth());
								update.set("sex", convert.getSex());
								update.set("nationalId", convert.getNationalId());
								update.set("bankCard", convert.getBankCard());
								update.set("spouseName", convert.getSpouseName());
								update.set("spouseIdCard", convert.getSpouseIdCard());
								update.set("spousePhone", convert.getSpousePhone());
								update.set("homeAddress", convert.getHomeAddress());
								update.set("cityCodeHome", convert.getCityCodeHome());
								update.set("districtCodeHome", convert.getDistrictCodeHome());
								update.set("homeCom", convert.getHomeCom());
								update.set("cityCodeCom", convert.getCityCodeHome());
								update.set("districtCodeCom", convert.getDistrictCodeCom());
								update.set("position", convert.getPosition());
								update.set("comName", convert.getComName());
								update.set("kycNotes", convert.getKycNotes());
								update.set("fieldType", convert.getFieldType());
								update.set("comment", convert.getComment());
								update.set("appStage", convert.getAppStage());
								update.set("appStatus", convert.getAppStatus());
								update.set("updatedAt", new Date());
								crmfieldMon = crmfieldTemplate.findAndModify(queryMon, update,
										new FindAndModifyOptions().returnNew(true), CrmField.class);

								CrmField crmfieldUpdate = crmTemplate.findOne(queryMon,
										CrmField.class);

								rabbitMQService.send("tpf-service-app", Map.of("func", "updateApp", "reference_id",
										request.path("reference_id"), "param",
										Map.of("project", "crm", "id", crmfieldUpdate.getId()), "body",
										convertService.toAppAutomationSubmitField(crmfieldUpdate)));
							} else {
								crmfieldTemplate.save(convert);
								Query queryCreate = Query.query(Criteria.where("appId").is(convert.getAppId()));
								CrmField crmfieldCreate = crmTemplate.findOne(queryCreate,
										CrmField.class);
								rabbitMQService.send("tpf-service-app",
										Map.of("func", "createApp", "reference_id",
												request.path("body").path("reference_id"), "body",
												convertService.toAppDisplay(crmfieldCreate).put("reference_id",
														request.path("body").path("reference_id").asText())));
								CrmFieldKH convertKH = mapper.convertValue(convert, CrmFieldKH.class);
								crmfieldSender.add(convertKH);

							}

						}
					}
				}
			}
		}

		if (crmfieldSender.size() > 0) {
			new Thread(() -> {
				try {
					int sendCount = 0;
					do {
						ArrayNode array = mapper.convertValue(crmfieldSender, ArrayNode.class);
						JsonNode result = apiService.pushCeateFieldEsb(array,
								request.path("body").path("request_id").asText());
						if (result.path("resultCode").asText().equals("200")) {
							return;
						}
						sendCount++;
						Thread.sleep(5 * 60 * 1000);
					} while (sendCount <= 2);
				} catch (Exception e) {
					log.info("{}", utils.getJsonNodeResponse(0, request.path("body"),
							mapper.createObjectNode().put("message", e.getMessage())));
				}
			}).start();

		}

		return utils.getJsonNodeResponse(0, request.path("body"), null);
	}

	public JsonNode waiveField(JsonNode request) throws Exception {
		ArrayNode apps = mapper.convertValue(request.path("body").path("data"), ArrayNode.class);

		if (!request.path("body").hasNonNull("data")
				|| mapper.convertValue(request.path("body").path("data"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, request.path("body"),
					mapper.createObjectNode().put("message", "data array required"));
		ArrayNode crmWaiveFields = JsonNodeFactory.instance.arrayNode();
		Set<JsonNode> set = new HashSet<JsonNode>();
		for (JsonNode app : apps) {
			if (set.add(app) == false) {
				return utils.getJsonNodeResponse(499, request.path("body"), mapper.createObjectNode().put("message",
						String.format("data.appId duplicates %s", app.path("appId").asText())));
			}
		}

		for (JsonNode app : apps) {

			final String appId = app.path("appId").asText();
			if (appId.isBlank())
				return utils.getJsonNodeResponse(499, app,
						mapper.createObjectNode().put("message", "data.appId not null"));
			CrmWaiveField crmWaiveField = mapper.convertValue(app, CrmWaiveField.class);

			Query queryMon = Query.query(Criteria.where("appId").is(crmWaiveField.getAppId()));
			CrmWaiveField crmWaiveFieldMon = crmwaivefieldTemplate.findOne(queryMon,
					CrmWaiveField.class);

			crmWaiveField.setProject("crm");
			crmWaiveFields.add(convertService.toESBCrmWaiveField(crmWaiveField));
			if (crmWaiveFieldMon != null) {
				Update update = new Update();
				if (crmWaiveFieldMon.getAutomationResults() != null) {
					var check_Automation = mapper.convertValue(crmWaiveFieldMon.getAutomationResults().get(0),
							JsonNode.class);
					var autoRun = (check_Automation.path("automationResult").asText().equals("WAIVE_FIELD_RUN"));
					if (check_Automation != null && autoRun) {
						return utils.getJsonNodeResponse(499, request.path("body"), mapper.createObjectNode().put(
								"message", String.format("data.appId %s WAIVE_FIELD_RUN", app.path("appId").asText())));
					}

					LinkedList<Map> autoResults = new LinkedList<Map>();
					HashMap<String, Object> autoUpdate = new HashMap<>();
					autoUpdate.put("createdAt", new Date());
					autoUpdate.put("automationResult", "WAIVE_FIELD_RUN");
					autoResults.push(autoUpdate);
					update.set("automationResults", autoResults);

				}
				update.set("updatedAt", new Date());
				crmWaiveFieldMon = crmwaivefieldTemplate.findAndModify(queryMon, update,
						new FindAndModifyOptions().returnNew(true), CrmWaiveField.class);

				Query queryUpdate = Query.query(Criteria.where("appId").is(crmWaiveField.getAppId()));
				CrmWaiveField crmWaiveFieldUpdate = crmwaivefieldTemplate.findOne(queryUpdate,
						CrmWaiveField.class);

				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
								Map.of("project", "crm", "id", crmWaiveFieldUpdate.getId()), "body",
								convertService.toAppAutomationField(crmWaiveFieldUpdate)));

			} else {
				HashMap<String, Object> automationResult = new HashMap<>();
				automationResult.put("createdAt", new Date());
				automationResult.put("automationResult", "WAIVE_FIELD_RUN");
				List<Object> list = new ArrayList<>();
				list.add(automationResult);
				crmWaiveField.setAutomationResults(list);

				crmwaivefieldTemplate.save(crmWaiveField);

				Query queryCreate = Query.query(Criteria.where("appId").is(crmWaiveField.getAppId()));

				CrmWaiveField crmWaiveFieldCreate = crmwaivefieldTemplate.findOne(queryCreate,
						CrmWaiveField.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "createApp", "reference_id", request.path("body").path("reference_id"), "body",
								convertService.toAppDisplay(crmWaiveFieldCreate).put("reference_id",
										request.path("body").path("reference_id").asText())));
			}

		}

		HashMap<String, Object> requestSend = new HashMap<>();
		requestSend.put("func", "waiveField");
		ObjectNode body = mapper.createObjectNode();
		body.put("project", "crm");
		body.put("transaction_id", "transaction_waive_field");
		body.set("data", crmWaiveFields);
		requestSend.put("body", body);
		requestSend.put("reference_id", request.path("body").path("reference_id"));
		rabbitMQService.send("tpf-service-esb", requestSend);
		return utils.getJsonNodeResponse(0, request.path("body"), null);
	}

	public JsonNode submitField(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");

		final String appId = data.path("appId").asText();

		if (appId.isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.appId not null"));
		Query query = Query.query(Criteria.where("appId").is(appId));
		CrmField crmfield = crmfieldTemplate.findOne(query, CrmField.class);
		if (crmfield == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));
		if (crmfield.getAutomationResults() != null && crmfield.getAutomationResults().size() > 0) {
			var check_Automation = mapper.convertValue(crmfield.getAutomationResults().get(0), JsonNode.class);
			var autoRun = (check_Automation.path("automationResult").asText().equals("WAIVE_FIELD_RUN"));

			if (autoRun)
				return utils.getJsonNodeResponse(1, body,
						mapper.createObjectNode().put("message", String.format("data.appId %s UPLOADING", appId)));
		}
		if ((crmfield.getAppStage() != null && crmfield.getAppStage().equals(STAGE_SUBMIT))
				|| (crmfield.getAppStatus() != null && crmfield.getAppStatus().equals(STATUS_SUBMIT)))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s UPLOADING", appId)));

		if (data.path("phoneConfirmed").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.phoneConfirmed not null"));

		if (data.path("resultHomeVisit").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.resultHomeVisit not null"));

		if (data.path("resultOfficeVisit").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.resultOfficeVisit not null"));

		if (data.path("result2ndHomeVisit").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.result2ndHomeVisit not null"));

		if (data.path("noOfAttempts").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.noOfAttempts not null"));

		if (data.path("remarksDecisionFic").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.remarksDecisionFic not null"));

		if (data.path("verificationAgent").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.verificationAgent not null"));

		if (data.path("resultDecisionFiv").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.resultDecisionFiv not null"));

		if (data.path("remarksDecisionFiv").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.remarksDecisionFiv not null"));

		if (data.path("timeOfVisit").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.timeOfVisit not null"));

		if (data.path("verificationDate").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.verificationDate not null"));

		if (data.path("resonDecisionFic").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.resonDecisionFic not null"));

		if (data.path("decisionFic").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.decisionFic not null"));

		if (!data.hasNonNull("files") || mapper.convertValue(data.path("files"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, request.path("body"),
					mapper.createObjectNode().put("message", "data.files array required"));
		ArrayNode documents = mapper.convertValue(data.path("files"), ArrayNode.class);
		for (int index = 0; index < documents.size(); index++) {
			if (documents.get(index).path("documentMd5").asText().isBlank()
					|| documents.get(index).path("documentFilename").asText().isBlank()
					|| documents.get(index).path("documentFileExtension").asText().isBlank()
					|| documents.get(index).path("documentUrlDownload").asText().isBlank())
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message", String.format("document %s not valid", index)));
		}

		ArrayList<JsonNode> crmFields = new ArrayList<JsonNode>();

		crmFields.add(mapper.createObjectNode().put("appId", appId));

		JsonNode getDataFieldsDB = rabbitMQService.sendAndReceive("tpf-service-finnone", Map.of("func", "getDataFields",
				"reference_id", request.path("body").path("reference_id"), "body", Map.of("apps", crmFields)));

		if (getDataFieldsDB.path("status").asInt(0) != 200) {
			return utils.getJsonNodeResponse(1, request.path("body"), getDataFieldsDB.path("data"));
		}

		if (getDataFieldsDB.path("data").isArray()) {
			for (JsonNode item : getDataFieldsDB.path("data")) {
				if (crmfield.getAppId().equals(item.path("appId").asText())) {
					CrmField check = mapper.convertValue(item, CrmField.class);
					if (!(check.getAppStage().toUpperCase().equals("FII"))) {

						return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
								String.format("data.appId %s %s", appId, check.getAppStage().toUpperCase())));
					}
				}
			}
		}

		JsonNode getListFinnOneFileds = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getListFinnOneFileds", "reference_id", request.path("reference_id"), "body",
						data.path("phoneConfirmed")));

		if (getListFinnOneFileds.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", String.format("data not found")));

		Update update = new Update();
		var a = 0;
		for (JsonNode i : getListFinnOneFileds.path("data").path("phoneConfirmed")) {
			if ((i.has(data.path("phoneConfirmed").asText()))) {
				update.set("phoneConfirmed", i.get(data.path("phoneConfirmed").asText()).asText());
				a++;
				break;
			}

		}
		if (a == 0) {
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.phoneConfirmed %s  not exits", data.path("phoneConfirmed").asText())));
		}
		var b = 0;
		for (JsonNode i : getListFinnOneFileds.path("data").path("resultHomeVisit")) {

			if ((i.has(data.path("resultHomeVisit").asText()))) {
				update.set("resultHomeVisit", i.get(data.path("resultHomeVisit").asText()).asText());
				b++;
				break;
			}

		}
		if (b == 0) {
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.resultHomeVisit %s  not exits", data.path("resultHomeVisit").asText())));
		}

		var c = 0;
		for (JsonNode i : getListFinnOneFileds.path("data").path("resultOfficeVisit")) {
			if ((i.has(data.path("resultOfficeVisit").asText())))
				update.set("resultOfficeVisit", i.get(data.path("resultOfficeVisit").asText()).asText());
			c++;
			break;
		}
		if (c == 0) {
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.resultOfficeVisit %s  not exits", data.path("resultOfficeVisit").asText())));
		}

		var d = 0;
		for (JsonNode i : getListFinnOneFileds.path("data").path("result2ndHomeVisit")) {
			if ((i.has(data.path("result2ndHomeVisit").asText()))) {
				update.set("result2ndHomeVisit", i.get(data.path("result2ndHomeVisit").asText()).asText());
				d++;
				break;
			}
		}
		if (d == 0) {
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.result2ndHomeVisit %s  not exits", data.path("result2ndHomeVisit").asText())));
		}

		var f = 0;
		for (JsonNode i : getListFinnOneFileds.path("data").path("verificationAgent")) {
			if ((i.has(data.path("verificationAgent").asText()))) {
				update.set("verificationAgent", i.get(data.path("verificationAgent").asText()).asText());
				f++;
				break;
			}
		}
		if (f == 0) {
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.verificationAgent %s  not exits", data.path("verificationAgent").asText())));
		}
		var g = 0;
		for (JsonNode i : getListFinnOneFileds.path("data").path("resultDecisionFiv")) {
			if ((i.has(data.path("resultDecisionFiv").asText()))) {
				update.set("resultDecisionFiv", i.get(data.path("resultDecisionFiv").asText()).asText());
				g++;
				break;
			}
		}
		if (g == 0) {
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.resultDecisionFiv %s  not exits", data.path("resultDecisionFiv").asText())));
		}

		var j = 0;
		for (JsonNode i : getListFinnOneFileds.path("data").path("resonDecisionFic")) {
			if ((i.has(data.path("resonDecisionFic").asText()))) {
				update.set("resonDecisionFic", i.get(data.path("resonDecisionFic").asText()).asText());
				j++;
				break;
			}
		}
		if (j == 0) {
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.resonDecisionFic %s  not exits", data.path("resonDecisionFic").asText())));
		}
		var m = 0;
		for (JsonNode i : getListFinnOneFileds.path("data").path("decisionFic")) {
			if ((i.has(data.path("decisionFic").asText()))) {
				update.set("decisionFic", i.get(data.path("decisionFic").asText()).asText());
				m++;
				break;
			}
		}
		if (m == 0) {
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.decisionFic %s  not exits", data.path("decisionFic").asText())));
		}

		if (!(getListFinnOneFileds.path("data").path("noOfAttempts").has(data.path("noOfAttempts").asText())))
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.noOfAttempts %s  not exits", data.path("noOfAttempts").asText())));
		update.set("noOfAttempts", getListFinnOneFileds.path("data").path("noOfAttempts")
				.path(data.path("noOfAttempts").asText()).asInt());

		List<HashMap> filesUpload = new ArrayList<HashMap>();
		ArrayNode filesUploadSender = JsonNodeFactory.instance.arrayNode();
		for (JsonNode document : documents) {
			if (!document.path("documentUrlDownload").asText().matches(
					"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*(:[0-9]{1,5})?(\\/.*)?$"))
				return utils.getJsonNodeResponse(499, body,
						mapper.createObjectNode().put("message",
								String.format("data.documents [%s].documentUrlDownload not valid",
										document.path("documentUrlDownload").asText())));

			JsonNode filesDbInfo = mapper.convertValue(getListFinnOneFileds.path("data").path("filesUpload"),
					JsonNode.class);
			JsonNode uploadResult = apiService.uploadDocumentInternal(document, filesDbInfo);
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
			docUpload.put("documentFilename", document.path("documentFilename").asText());
			docUpload.put("documentFileExtension", document.path("documentFileExtension").asText());
			docUpload.put("documentUrlDownload", document.path("documentUrlDownload").asText());
			docUpload.put("documentMd5", document.path("documentMd5").asText());

			docUpload.put("originalname", document.path("documentFilename").asText().concat(".")
					.concat(document.path("documentFileExtension").asText()));
			docUpload.put("filename", uploadResult.path("data").path("filename").asText());
			filesUpload.add(docUpload);
			ObjectNode item = mapper.createObjectNode();
			item.put("fileName", uploadResult.path("data").path("filename").asText());
			filesUploadSender.add(item);
		}

		update = update.set("updatedAt", new Date()).set("filesUpload", filesUpload)
				.set("timeOfVisit", data.path("timeOfVisit").asText())
				.set("verificationDate", data.path("verificationDate").asText())
				.set("remarksDecisionFic", data.path("remarksDecisionFic").asText())
				.set("remarksDecisionFiv", data.path("remarksDecisionFiv").asText()).set("appStatus", STATUS_SUBMIT)
				.set("appStage", STAGE_SUBMIT);

		crmfield = crmfieldTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				CrmField.class);

		CrmField crmfieldUpdate = crmfieldTemplate.findOne(query, CrmField.class);

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "crm", "id", crmfieldUpdate.getId()), "body",
						convertService.toAppAutomationSubmitField(crmfieldUpdate)));

		ArrayNode crmSubmitFields = JsonNodeFactory.instance.arrayNode();

		ObjectNode crmObjectNode = mapper.createObjectNode();
		crmObjectNode.put("appId", crmfield.getAppId());
		crmObjectNode.put("phoneConfirmed", crmfield.getPhoneConfirmed());
		crmObjectNode.put("resultHomeVisit", crmfield.getResultHomeVisit());
		crmObjectNode.put("resultOfficeVisit", crmfield.getResultOfficeVisit());
		crmObjectNode.put("result2ndHomeVisit", crmfield.getResult2ndHomeVisit());
		crmObjectNode.set("attachmentField", filesUploadSender);
		crmObjectNode.put("timeOfVisit", crmfield.getTimeOfVisit());
		crmObjectNode.put("verificationDate", crmfield.getVerificationDate());
		crmObjectNode.put("remarksDecisionFiv", crmfield.getRemarksDecisionFiv());
		crmObjectNode.put("remarksDecisionFic", crmfield.getRemarksDecisionFic());
		crmObjectNode.put("resonDecisionFic", crmfield.getResonDecisionFic());

		crmSubmitFields.add(crmObjectNode);

		HashMap<String, Object> requestSend = new HashMap<>();

		requestSend.put("func", "submitField");
		ObjectNode bodySender = mapper.createObjectNode();
		bodySender.put("project", "crm");
		bodySender.put("transaction_id", "transaction_submit_field");
		bodySender.set("data", crmSubmitFields);
		requestSend.put("body", bodySender);
		requestSend.put("reference_id", request.path("body").path("reference_id"));

		rabbitMQService.send("tpf-service-esb", requestSend);

		return utils.getJsonNodeResponse(0, request.path("body"), null);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonNode returnQueueAndSendBack(JsonNode request) throws Exception {
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
				// \\.[a-z]{2,5}
				if (!document.path("documentUrlDownload").asText().matches(
						"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*(:[0-9]{1,5})?(\\/.*)?$"))

					return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
							String.format("data.documents[%s].documentUrlDownload not valid", index)));
				if (document.path("documentCode").asText().toLowerCase().trim().replace(" ", "_")
						.equals(DOCUMENT_CODE_ACCA))
					hasDocumentCodeACCA = true;

			}
		}

		if (!data.path("fullInfoApp").hasNonNull("references") || mapper.convertValue(data.path("fullInfoApp").path("references"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.references array required"));
		if (data.path("fullInfoApp").path("primaryAddress").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.primaryAddress not blank"));
		if (data.path("fullInfoApp").path("phoneNumber").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.phoneNumber not blank"));
		if (data.path("fullInfoApp").path("incomeExpense").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.incomeExpense not blank"));
		if (data.path("fullInfoApp").path("modeOfPayment").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.modeOfPayment not blank"));
		if (data.path("fullInfoApp").path("dayOfSalaryPayment").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.dayOfSalaryPayment not blank"));
		if (data.path("fullInfoApp").path("loanApplicationType").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanApplicationType not blank"));
		if (data.path("fullInfoApp").path("loanAmountRequested").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanAmountRequested not blank"));
		if (data.path("fullInfoApp").path("requestedTenure").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.requestedTenure not blank"));
		if (data.path("fullInfoApp").path("interestRate").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.interestRate not blank"));
		if (data.path("fullInfoApp").path("saleAgentCodeLoanDetails").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.saleAgentCodeLoanDetails not blank"));
//		if (data.path("fullInfoApp").path("vapProduct").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "data.vapProduct not blank"));
//		if (data.path("fullInfoApp").path("vapTreatment").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "data.vapTreatment not blank"));
//		if (data.path("fullInfoApp").path("insuranceCompany").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "data.insuranceCompany not blank"));
		if (data.path("fullInfoApp").path("loanPurpose").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanPurpose not blank"));
		if (data.path("fullInfoApp").path("numberOfDependents").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.numberOfDependents not blank"));
		if (data.path("fullInfoApp").path("monthlyRental").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.monthlyRental not blank"));
		if (data.path("fullInfoApp").path("houseOwnership").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.houseOwnership not blank"));
		if (data.path("fullInfoApp").path("newBankCardNumber").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.newBankCardNumber not blank"));
		if (data.path("fullInfoApp").path("saleAgentCodeDynamicForm").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.saleAgentCodeDynamicForm not blank"));
		if (data.path("fullInfoApp").path("courierCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.courierCode not blank"));
		if (data.path("fullInfoApp").path("maximumInterestedRate").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.maximumInterestedRate not blank"));
		if (!data.path("fullInfoApp").hasNonNull("identifications") || mapper.convertValue(data.path("fullInfoApp").path("identifications"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.identifications array required"));
		if (data.path("fullInfoApp").path("remarksDynamicForm").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.remarksDynamicForm not blank"));
		if (data.path("fullInfoApp").path("maritalStatus").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.maritalStatus not blank"));
		if (data.path("fullInfoApp").path("employmentName").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.employmentName not blank"));
		Query query = Query.query(Criteria.where("appId").is(appId));
		Crm crm = crmTemplate.findOne(query, Crm.class);
		if (crm == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));

		if (!crm.getStatus().equals(STATUS_SEND_BACK))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.appId %s not request resubmit queue and send back. current stage %s status %s", appId,
									crm.getStage(), crm.getStatus())));
		if (!crm.getStage().equals(STAGE_LEAD_DETAILS))
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.appId %s not request resubmit queue and send back. current stage %s status %s", appId,
									crm.getStage(), crm.getStatus())));

		final String productCode = crm.getProduct().replace(" ", "_").trim().toLowerCase();
		final String schemeCode = crm.getScheme().replace(" ", "_").trim().toLowerCase();

		JsonNode documentFinnOne = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getListDocuments", "reference_id", request.path("reference_id"), "body",
						Map.of("productCode", productCode, "schemeCode", schemeCode)));

		if (documentFinnOne.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("%s %s not found", productCode, schemeCode)));

		JsonNode returns = mapper.convertValue(crm.getReturns(), JsonNode.class);
		if (returns.hasNonNull("returnQueues") && !(mapper.convertValue(returns.path("returnQueues"), ArrayNode.class))
				.get(0).path("isComplete").asBoolean())
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.appId %s returnQueue not complete", crm.getAppId())));

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
		returnQueue.put("firstName", data.path("fullInfoApp").path("firstName").asText());
		returnQueue.put("middleName", data.path("fullInfoApp").path("middleName").asText());
		returnQueue.put("lastName", data.path("fullInfoApp").path("lastName").asText());
		returnQueue.put("gender", data.path("fullInfoApp").path("gender").asText());
		returnQueue.put("dateOfBirth", data.path("fullInfoApp").path("dateOfBirth").asText());
		returnQueue.put("maritalStatus", data.path("fullInfoApp").path("maritalStatus").asText());
		returnQueue.put("primaryAddress", data.path("fullInfoApp").path("primaryAddress").asText());
		returnQueue.put("phoneNumber", data.path("fullInfoApp").path("phoneNumber").asText());
		returnQueue.put("employmentName", data.path("fullInfoApp").path("employmentName").asText());
		returnQueue.put("incomeExpense", data.path("fullInfoApp").path("incomeExpense").asText());
		returnQueue.put("amount", data.path("fullInfoApp").path("amount").asText());
		returnQueue.put("modeOfPayment", data.path("fullInfoApp").path("modeOfPayment").asText());
		returnQueue.put("dayOfSalaryPayment", data.path("fullInfoApp").path("dayOfSalaryPayment").asText());
		returnQueue.put("loanApplicationType", data.path("fullInfoApp").path("loanApplicationType").asText());
		returnQueue.put("loanAmountRequested", data.path("fullInfoApp").path("loanAmountRequested").asText());
		returnQueue.put("requestedTenure", data.path("fullInfoApp").path("requestedTenure").asText());
		returnQueue.put("interestRate", data.path("fullInfoApp").path("interestRate").asText());
		returnQueue.put("saleAgentCodeLoanDetails", data.path("fullInfoApp").path("saleAgentCodeLoanDetails").asText());
		returnQueue.put("vapProduct", data.path("fullInfoApp").path("vapProduct").asText());
		returnQueue.put("vapTreatment", data.path("fullInfoApp").path("vapTreatment").asText());
		returnQueue.put("insuranceCompany", data.path("fullInfoApp").path("insuranceCompany").asText());
		returnQueue.put("loanPurpose", data.path("fullInfoApp").path("loanPurpose").asText());
		returnQueue.put("loanPurposeOther", data.path("fullInfoApp").path("loanPurposeOther").asText());
		returnQueue.put("numberOfDependents", data.path("fullInfoApp").path("numberOfDependents").asText());
		returnQueue.put("monthlyRental", data.path("fullInfoApp").path("monthlyRental").asText());
		returnQueue.put("houseOwnership", data.path("fullInfoApp").path("houseOwnership").asText());
		returnQueue.put("newBankCardNumber", data.path("fullInfoApp").path("newBankCardNumber").asText());
		returnQueue.put("remarksDynamicForm", data.path("fullInfoApp").path("remarksDynamicForm").asText());
		returnQueue.put("saleAgentCodeDynamicForm", data.path("fullInfoApp").path("saleAgentCodeDynamicForm").asText());
		returnQueue.put("courierCode", data.path("fullInfoApp").path("courierCode").asText());
		returnQueue.put("maximumInterestedRate", data.path("fullInfoApp").path("maximumInterestedRate").asText());
		returnQueue.put("isComplete", false);

		ArrayNode addresses = mapper.convertValue(data.path("fullInfoApp").path("addresses"), ArrayNode.class);
		List<HashMap> addressesUpload = new ArrayList<HashMap>();
		if (addresses != null ) {
			if (addresses.size() != 0) {
				for(JsonNode address : addresses) {
					HashMap<String, String> addressUpload = new HashMap<>();
					addressUpload.put("addressType", address.path("addressType").asText());
					addressUpload.put("addressLine1", address.path("addressLine1").asText());
					addressUpload.put("addressLine2", address.path("addressLine2").asText());
					addressUpload.put("addressLine3", address.path("addressLine3").asText());
					addressUpload.put("area", address.path("area").asText());
					addressUpload.put("city", address.path("city").asText());
					addressUpload.put("phoneNumber", address.path("phoneNumber").asText());
					addressesUpload.add(addressUpload);
				}
			}
		}
		ArrayNode references = mapper.convertValue(data.path("fullInfoApp").path("references"), ArrayNode.class);
		List<HashMap> referencesUpload = new ArrayList<HashMap>();
		for(JsonNode reference : references) {
			HashMap<String, String> referenceUpload = new HashMap<>();
			referenceUpload.put("name", reference.path("name").asText());
			referenceUpload.put("relationship", reference.path("relationship").asText());
			referenceUpload.put("phoneNumber", reference.path("phoneNumber").asText());
			referencesUpload.add(referenceUpload);
		}

		ArrayNode familyes = mapper.convertValue(data.path("fullInfoApp").path("family"), ArrayNode.class);
		List<HashMap> familyesUpload = new ArrayList<HashMap>();
		if (familyes != null ) {
			if (familyes.size() != 0) {
				for(JsonNode family : familyes) {
					HashMap<String, String> familyUpload = new HashMap<>();
					familyUpload.put("memberName", family.path("memberName").asText());
					familyUpload.put("phoneNumber", family.path("phoneNumber").asText());
					familyUpload.put("relationship", family.path("relationship").asText());
					familyesUpload.add(familyUpload);
				}
			}
		}

		ArrayNode identifications = mapper.convertValue(data.path("fullInfoApp").path("identifications"), ArrayNode.class);
		List<HashMap> identificationsUpload = new ArrayList<HashMap>();
		if (identifications != null ) {
			if (identifications.size() != 0) {
				for(JsonNode identification : identifications) {
					HashMap<String, String> identificationUpload = new HashMap<>();
					identificationUpload.put("identificationType", identification.path("identificationType").asText());
					identificationUpload.put("identificationNumber", identification.path("identificationNumber").asText());
					identificationUpload.put("issuingCountry", identification.path("issuingCountry").asText());
					identificationUpload.put("placeOfIssue", identification.path("placeOfIssue").asText());
					identificationUpload.put("issueDate", identification.path("issueDate").asText());
					identificationUpload.put("expiryDate", identification.path("expiryDate").asText());
					identificationsUpload.add(identificationUpload);
				}
			}
		}
		returnQueue.put("addresses", addressesUpload);
		returnQueue.put("references", referencesUpload);
		returnQueue.put("family", familyesUpload);
		returnQueue.put("identifications", identificationsUpload);

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

		crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Crm.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "saleQueueWithFullInfo", "body",
				convertService.toSaleQueueFinnoneAndSenback(crm).put("reference_id", body.path("reference_id").asText())));

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "crm", "id", crm.getId()), "body",
						convertService.toAppStage(crm)));

		return utils.getJsonNodeResponse(0, body, null);
	}

	public JsonNode retryAddDocumentsWithCustIdAndFullApp(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");

		final long leadId = data.path("leadId").asLong(0);

		if (data.path("branch").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.branch not null"));

		if (data.path("chanel").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.chanel not null"));

		if (data.path("appId").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.appId not null"));

		if (data.path("productCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.productCode not blank"));
		if (data.path("schemeCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.schemeCode not blank"));
		if (data.path("custId").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.custId not blank"));
		if (!data.hasNonNull("documents") || mapper.convertValue(data.path("documents"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.documents array required"));
		if (!data.path("fullInfoApp").hasNonNull("references") || mapper.convertValue(data.path("fullInfoApp").path("references"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.references array required"));
		if (data.path("fullInfoApp").path("primaryAddress").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.primaryAddress not blank"));
		if (data.path("fullInfoApp").path("phoneNumber").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.phoneNumber not blank"));
		if (data.path("fullInfoApp").path("incomeExpense").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.incomeExpense not blank"));
		if (data.path("fullInfoApp").path("modeOfPayment").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.modeOfPayment not blank"));
		if (data.path("fullInfoApp").path("dayOfSalaryPayment").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.dayOfSalaryPayment not blank"));
		if (data.path("fullInfoApp").path("loanApplicationType").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanApplicationType not blank"));
		if (data.path("fullInfoApp").path("loanAmountRequested").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanAmountRequested not blank"));
		if (data.path("fullInfoApp").path("requestedTenure").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.requestedTenure not blank"));
		if (data.path("fullInfoApp").path("interestRate").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.interestRate not blank"));
		if (data.path("fullInfoApp").path("saleAgentCodeLoanDetails").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.saleAgentCodeLoanDetails not blank"));
//		if (data.path("fullInfoApp").path("vapProduct").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "data.vapProduct not blank"));
//		if (data.path("fullInfoApp").path("vapTreatment").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "data.vapTreatment not blank"));
//		if (data.path("fullInfoApp").path("insuranceCompany").asText().isBlank())
//			return utils.getJsonNodeResponse(499, body,
//					mapper.createObjectNode().put("message", "data.insuranceCompany not blank"));
		if (data.path("fullInfoApp").path("loanPurpose").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanPurpose not blank"));
		if (data.path("fullInfoApp").path("numberOfDependents").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.numberOfDependents not blank"));
		if (data.path("fullInfoApp").path("monthlyRental").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.monthlyRental not blank"));
		if (data.path("fullInfoApp").path("houseOwnership").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.houseOwnership not blank"));
		if (data.path("fullInfoApp").path("newBankCardNumber").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.newBankCardNumber not blank"));
		if (data.path("fullInfoApp").path("saleAgentCodeDynamicForm").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.saleAgentCodeDynamicForm not blank"));
		if (data.path("fullInfoApp").path("courierCode").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.courierCode not blank"));
		if (data.path("fullInfoApp").path("maximumInterestedRate").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.maximumInterestedRate not blank"));
		if (!data.path("fullInfoApp").hasNonNull("identifications") || mapper.convertValue(data.path("fullInfoApp").path("identifications"), ArrayNode.class).size() == 0)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.identifications array required"));
		if (data.path("fullInfoApp").path("remarksDynamicForm").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.remarksDynamicForm not blank"));
		if (data.path("fullInfoApp").path("maritalStatus").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.maritalStatus not blank"));
		if (data.path("fullInfoApp").path("employmentName").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.employmentName not blank"));

		Query query = Query.query(Criteria.where("leadId").is(leadId));
		Crm crm = crmTemplate.findOne(query, Crm.class);
//		if (crm == null)
//			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
//					String.format("data.leadId %s not exits", data.path("leadId").asInt())));

		if (crm.getStage().equals(STAGE_PRECHECK1_DONE))
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.leadId %s precheck2 not exits", data.path("leadId").asInt())));

		if (crm.getStage().equals(STAGE_PRECHECK2_CHECIKING)) {
			JsonNode lastPreCheck2 = mapper.convertValue(crm.getPreChecks().get("preCheck2"), ArrayNode.class)
					.get(0);
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message",
							String.format("data.leadId %s precheck2 %s  %s", data.path("leadId").asInt(),
									lastPreCheck2.path("bankCardNumber").asText(),
									lastPreCheck2.path("data").path("description").asText())));
		}

//		if (crm.getStage().equals(STAGE_UPLOADED) || crm.getFilesUpload() != null)
//			return utils.getJsonNodeResponse(1, body,
//					mapper.createObjectNode().put("message", String.format("data.leadId %s uploaded document at %s ",
//							data.path("leadId").asInt(), crm.getUpdatedAt())));
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
						String.format("document %s required", document.path("code").asText())));
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
		ArrayNode addresses = mapper.convertValue(data.path("fullInfoApp").path("addresses"), ArrayNode.class);
		List<HashMap> addressesUpload = new ArrayList<HashMap>();
		if (addresses != null ) {
			if (addresses.size() != 0) {
				for(JsonNode address : addresses) {
					HashMap<String, String> addressUpload = new HashMap<>();
					addressUpload.put("addressType", address.path("addressType").asText());
					addressUpload.put("addressLine1", address.path("addressLine1").asText());
					addressUpload.put("addressLine2", address.path("addressLine2").asText());
					addressUpload.put("addressLine3", address.path("addressLine3").asText());
					addressUpload.put("area", address.path("area").asText());
					addressUpload.put("city", address.path("city").asText());
					addressUpload.put("phoneNumber", address.path("phoneNumber").asText());
					addressesUpload.add(addressUpload);
				}
			}
		}
		ArrayNode references = mapper.convertValue(data.path("fullInfoApp").path("references"), ArrayNode.class);
		List<HashMap> referencesUpload = new ArrayList<HashMap>();
		for(JsonNode reference : references) {
			HashMap<String, String> referenceUpload = new HashMap<>();
			referenceUpload.put("name", reference.path("name").asText());
			referenceUpload.put("relationship", reference.path("relationship").asText());
			referenceUpload.put("phoneNumber", reference.path("phoneNumber").asText());
			referencesUpload.add(referenceUpload);
		}

		ArrayNode familyes = mapper.convertValue(data.path("fullInfoApp").path("family"), ArrayNode.class);
		List<HashMap> familyesUpload = new ArrayList<HashMap>();
		if (familyes != null ) {
			if (familyes.size() != 0) {
				for(JsonNode family : familyes) {
					HashMap<String, String> familyUpload = new HashMap<>();
					familyUpload.put("memberName", family.path("memberName").asText());
					familyUpload.put("phoneNumber", family.path("phoneNumber").asText());
					familyUpload.put("relationship", family.path("relationship").asText());
					familyesUpload.add(familyUpload);
				}
			}
		}

		ArrayNode identifications = mapper.convertValue(data.path("fullInfoApp").path("identifications"), ArrayNode.class);
		List<HashMap> identificationsUpload = new ArrayList<HashMap>();
		if (identifications != null ) {
			if (identifications.size() != 0) {
				for(JsonNode identification : identifications) {
					HashMap<String, String> identificationUpload = new HashMap<>();
					identificationUpload.put("identificationType", identification.path("identificationType").asText());
					identificationUpload.put("identificationNumber", identification.path("identificationNumber").asText());
					identificationUpload.put("issuingCountry", identification.path("issuingCountry").asText());
					identificationUpload.put("placeOfIssue", identification.path("placeOfIssue").asText());
					identificationUpload.put("issueDate", identification.path("issueDate").asText());
					identificationUpload.put("expiryDate", identification.path("expiryDate").asText());
					identificationsUpload.add(identificationUpload);
				}
			}
		}

		Update update = new Update().set("updatedAt", new Date()).set("stage", STAGE_UPLOADED)
				.set("status", STATUS_PRE_APPROVAL).set("scheme", data.path("schemeCode").asText())
				.set("product", data.path("productCode").asText()).set("chanel", data.path("chanel").asText()).set("branch", data.path("branch").asText())
				.set("schemeFinnOne", documentFinnOne.path("data").path("valueShemeFinnOne").asText())
				.set("productFinnOne", documentFinnOne.path("data").path("valueProductFinnOne").asText())
				.set("filesUpload", filesUpload)
				.set("neoCustID", data.path("custId").asText())
				.set("cifNumber", data.path("cifNumber").asText())
				.set("idNumber", data.path("idNumber").asText())
				.set("firstName", data.path("fullInfoApp").path("firstName").asText())
				.set("middleName", data.path("fullInfoApp").path("middleName").asText())
				.set("lastName", data.path("fullInfoApp").path("lastName").asText())
				.set("gender", data.path("fullInfoApp").path("gender").asText())
				.set("dateOfBirth", data.path("fullInfoApp").path("dateOfBirth").asText())
				.set("maritalStatus", data.path("fullInfoApp").path("maritalStatus").asText())
				.set("family", familyesUpload)
				.set("identifications", identificationsUpload)
				.set("primaryAddress", data.path("fullInfoApp").path("primaryAddress").asText())
				.set("phoneNumber", data.path("fullInfoApp").path("phoneNumber").asText())
				.set("employmentName", data.path("fullInfoApp").path("employmentName").asText())
				.set("incomeExpense", data.path("fullInfoApp").path("incomeExpense").asText())
				.set("amount", data.path("fullInfoApp").path("amount").asText())
				.set("modeOfPayment", data.path("fullInfoApp").path("modeOfPayment").asText())
				.set("dayOfSalaryPayment", data.path("fullInfoApp").path("dayOfSalaryPayment").asText())
				.set("loanApplicationType", data.path("fullInfoApp").path("loanApplicationType").asText())
				.set("loanAmountRequested", data.path("fullInfoApp").path("loanAmountRequested").asText())
				.set("requestedTenure", data.path("fullInfoApp").path("requestedTenure").asText())
				.set("interestRate", data.path("fullInfoApp").path("interestRate").asText())
				.set("saleAgentCodeLoanDetails", data.path("fullInfoApp").path("saleAgentCodeLoanDetails").asText())
				.set("vapProduct", data.path("fullInfoApp").path("vapProduct").asText())
				.set("vapTreatment", data.path("fullInfoApp").path("vapTreatment").asText())
				.set("insuranceCompany", data.path("fullInfoApp").path("insuranceCompany").asText())
				.set("loanPurpose", data.path("fullInfoApp").path("loanPurpose").asText())
				.set("loanPurposeOther", data.path("fullInfoApp").path("loanPurposeOther").asText())
				.set("numberOfDependents", data.path("fullInfoApp").path("numberOfDependents").asText())
				.set("monthlyRental", data.path("fullInfoApp").path("monthlyRental").asText())
				.set("houseOwnership", data.path("fullInfoApp").path("houseOwnership").asText())
				.set("newBankCardNumber", data.path("fullInfoApp").path("newBankCardNumber").asText())
				.set("remarksDynamicForm", data.path("fullInfoApp").path("remarksDynamicForm").asText())
				.set("saleAgentCodeDynamicForm", data.path("fullInfoApp").path("saleAgentCodeDynamicForm").asText())
				.set("courierCode", data.path("fullInfoApp").path("courierCode").asText())
				.set("maximumInterestedRate", data.path("fullInfoApp").path("maximumInterestedRate").asText())
				.set("appId", data.path("appId").asText())
				.set("addresses", addressesUpload)
				.set("references", referencesUpload)
				;
		crm = crmTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Crm.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "createQuickLeadAppWithCustId", "body",
				convertService.toRetryAppFinnoneWithFullApp(crm).put("reference_id", body.path("reference_id").asText())));

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(crm).put("reference_id", body.path("reference_id").asText())));

		return utils.getJsonNodeResponse(0, body, null);
	}
}