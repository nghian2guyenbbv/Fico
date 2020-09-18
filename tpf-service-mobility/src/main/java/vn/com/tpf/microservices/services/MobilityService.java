package vn.com.tpf.microservices.services;

import java.io.Console;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Mobility;
import vn.com.tpf.microservices.models.MobilityField;
import vn.com.tpf.microservices.models.MobilityFieldKH;
import vn.com.tpf.microservices.models.MobilityFinnOneFiled;
import vn.com.tpf.microservices.models.MobilityWaiveField;
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
	private final List<String> LIST_STATUS_COMPLETE = Arrays.asList("Cancellation".toUpperCase().trim(),
			"Rejection".toUpperCase().trim());
	private final String STAGE_QUICKLEAD_FAILED_AUTOMATION = "QUICKLEAD_FAILED_AUTOMATION";

	private final String AUTOMATION_SUBMIT_RUN_FIELD = "SUBMIT_RUN_FIELD";
	private final String AUTOMATION_SUBMIT_FAILED_FIELD = "SUBMIT_FAILED_FIELD";
	private final String AUTOMATION_SUBMIT_PASS_FIELD = "SUBMIT_PASS_FIELD";

	private final String STAGE_SUBMIT_FIELD_RUN = "SUBMIT_RUN_FIELD";
	private final String STAGE_SUBMIT_FIELD_FAILED = "SUBMIT_FAILED_FIELD";

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
//	private final String STATUS_RESUBMIT_FAILED = "RESPONSE_QUERY_FAILED_AUTOMATION";

	private final String KEY_LAST_UPDATE_DATE = "lastUpdateDate";
	private final String KEY_USER_NAME = "userName";
	private final String KEY_STAGE = "stage";
	private final String KEY_STATUS = "status";
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
	private MongoTemplate mobilityTemplate;

	@Autowired
	private MongoTemplate mobilitywaivefieldTemplate;

	@Autowired
	private MongoTemplate mobilityfieldTemplate;

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
		System.out.println(preCheckResult.path("data"));
		

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
					mapper.createObjectNode().put("message", String.format("data.appId %s complete with status %s",
							appId, mobility.getStatus().toUpperCase().trim())));
		JsonNode item = rabbitMQService.sendAndReceive("tpf-service-esb", Map.of("func", "getAppInfo", "body", body));

		if (item.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s waiting sync data", appId)));

		Update update = new Update().set("updatedAt", new Date());
		final boolean updateStageAndStatus = (mobility.getViewLastUpdated() == null
				|| !item.path("data").path(KEY_LAST_UPDATE_DATE).asText().trim().toUpperCase()
						.equals(mobility.getViewLastUpdated().trim().toUpperCase()));
		if (updateStageAndStatus)
			update.set("viewLastUpdated", item.path("data").path(KEY_LAST_UPDATE_DATE).asText().trim().toUpperCase())
					.set("stage", item.path("data").path(KEY_STAGE).asText().toUpperCase().trim())
					.set("status", item.path("data").path(KEY_STATUS).asText().toUpperCase().trim());

		if (!item.path("data").path(KEY_STAGE).asText().toUpperCase().trim().equals(STAGE_SALES_QUEUE))
			update.set("userCreatedQueue", item.path("data").path(KEY_USER_NAME).asText());

		if (updateStageAndStatus) {
			mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
					Mobility.class);
			rabbitMQService.send("tpf-service-app",
					Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
							Map.of("project", "mobility", "id", mobility.getId()), "body",
							convertService.toAppStatus(mobility)));
		}
		((ObjectNode) item.path("data")).remove(KEY_USER_NAME);

		return utils.getJsonNodeResponse(0, body, item.path("data"));
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

		if (mobility.getStage().equals(STAGE_UPLOADED) || mobility.getFilesUpload() != null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.leadId %s uploaded document at %s ",
							data.path("leadId").asInt(), mobility.getUpdatedAt())));
		final String productCode = data.path("productCode").asText().replace(" ", "_").trim().toLowerCase();
		final String schemeCode = data.path("schemeCode").asText().replace(" ", "_").trim().toLowerCase();

//		JsonNode documentFinnOne = rabbitMQService.sendAndReceive("tpf-service-assets",
//				Map.of("func", "getListDocuments", "reference_id", request.path("reference_id"), "body",
//						Map.of("productCode", data.path("productCode").asText(), "schemeCode",l
//								data.path("schemeCode").asText())));

//		if (documentFinnOne.path("status").asInt() != 200)
//			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
//					String.format("%s %s not found", productCode, schemeCode)));

		JsonNode productCodeFinnOne = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getProductCode", "reference_id", request.path("reference_id"), "body",
						Map.of( "schemeCode",
								data.path("schemeCode").asText())));

		if (productCodeFinnOne.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message",
					String.format("%s not found",schemeCode)));

		ObjectNode documentsDb = mapper.convertValue(productCodeFinnOne.path("data").path("documents"), ObjectNode.class);
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
		//Default value for IH
		long partnerId = 3;
		String partnerName = "IN-HOUSE";
		Update update = new Update().set("updatedAt", new Date()).set("stage", STAGE_UPLOADED)
				.set("status", STATUS_PRE_APPROVAL).set("scheme", data.path("schemeCode").asText())
				.set("product", productCodeFinnOne.path("data").path("productCode").asText()).set("chanel", data.path("chanel").asText()).set("branch", data.path("branch").asText())
				.set("schemeFinnOne", productCodeFinnOne.path("data").path("valueShemeFinnOne").asText())
				.set("productFinnOne", productCodeFinnOne.path("data").path("valueProductFinnOne").asText())
				.set("filesUpload", filesUpload)
				.set("partnerId", partnerId)
				.set("partnerName",partnerName);

		if ("STUDENT LOAN".equals(productCodeFinnOne.path("data").path("valueProductFinnOne").asText())){
			update.set("branch","STUDENT");
		}
		mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				Mobility.class);

		JsonNode vendorAutoassign = rabbitMQService.sendAndReceive("tpf-service-autoassign", Map.of("func", "configureVendor", "request_id",
				body.path("reference_id").asText(), "schemeCode", mobility.getSchemeFinnOne(),"project","mobility"));

		if (!vendorAutoassign.path("data").path("data").path("vendorId").asText().isBlank()) {
			partnerId = vendorAutoassign.path("data").path("data").path("vendorId").asLong();
			partnerName = vendorAutoassign.path("data").path("data").path("vendorName").asText();
		}
		update.set("partnerId", partnerId);
		update.set("partnerName", partnerName);
		if(1 == partnerId || 2 == partnerId) {
			if(1 == partnerId){
				rabbitMQService.send("tpf-service-dataentry", Map.of("func", "sendAppNonWeb", "body",
						convertService.toSendAppNonWebFinnone(mobility, partnerId ,body.path("reference_id").asText()).put("reference_id", body.path("reference_id").asText())));
			} else {
				rabbitMQService.send("tpf-service-dataentry-sgb", Map.of("func", "sendAppNonWeb", "body",
						convertService.toSendAppNonWebFinnone(mobility, partnerId, body.path("reference_id").asText()).put("reference_id", body.path("reference_id").asText())));
			}
			mobility = mobilityTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
					Mobility.class);
		} else {
			rabbitMQService.send("tpf-service-esb", Map.of("func", "createQuickLeadApp", "body",
					convertService.toAppFinnone(mobility).put("reference_id", body.path("reference_id").asText())));
		}

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(mobility).put("reference_id", body.path("reference_id").asText())));

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
			return updateAutomationMobility(request);
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

			MobilityWaiveField mobilityWaiveField = mobilitywaivefieldTemplate.findOne(query, MobilityWaiveField.class);
			if (mobilityWaiveField == null)
				return utils.getJsonNodeResponse(500, body, mapper.createObjectNode().put("message",
						String.format("id %s mobilityWaiveField not exits", app.path("appId").asText())));

			HashMap<String, Object> automationResultObject = new HashMap<>();

			automationResultObject.put("createdAt", new Date());

			automationResultObject.put("automationResult", automationResult);

			LinkedList<Map> automationResultsNew = mapper.convertValue(mobilityWaiveField.getAutomationResults(),
					LinkedList.class);
			if (automationResultsNew == null)
				automationResultsNew = new LinkedList<Map>();
			automationResultsNew.push(automationResultObject);
			Update update = new Update().set("updatedAt", new Date());
			update.set("automationResults", automationResultsNew);

			mobilityWaiveField = mobilitywaivefieldTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), MobilityWaiveField.class);
			if (!automationResult.isBlank())
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
								Map.of("project", "mobility", "id", mobilityWaiveField.getId()), "body",
								convertService.toAppAutomationField(mobilityWaiveField)));
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

			MobilityField mobilityfield = mobilityfieldTemplate.findOne(query, MobilityField.class);
			if (mobilityfield == null)
				return utils.getJsonNodeResponse(500, body, mapper.createObjectNode().put("message",
						String.format("id %s not exits", app.path("appId").asText())));

			HashMap<String, Object> automationResultObject = new HashMap<>();

			automationResultObject.put("createdAt", new Date());

			automationResultObject.put("automationResult", automationResult);

			LinkedList<Map> automationResultsNew = mapper.convertValue(mobilityfield.getAutomationResults(),
					LinkedList.class);
			if (automationResultsNew == null)
				automationResultsNew = new LinkedList<Map>();
			automationResultsNew.push(automationResultObject);
			Update update = new Update().set("updatedAt", new Date());
			update.set("automationResults", automationResultsNew);

			mobilityfield = mobilityfieldTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), MobilityField.class);
			if (!automationResult.isBlank())
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
								Map.of("project", "mobility", "id", mobilityfield.getId()), "body",
								convertService.toAppAutomationSubmitField(mobilityfield)));
		}

		return utils.getJsonNodeResponse(0, request, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonNode updateAutomationMobility(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
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
						JsonNode result = apiService.pushAppIdOfLeadId(mobilitySender,
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
			if (currentStage.equals(STAGE_SALES_QUEUE_UPLOADING_HAS_ACCA)
					|| currentStage.equals(STAGE_SALES_QUEUE_HAS_ACCA_FAILED))
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
		if (!automationResult.isBlank())
			rabbitMQService.send("tpf-service-app",
					Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
							Map.of("project", "mobility", "id", mobility.getId()), "body",
							convertService.toAppAutomation(mobility, updateStatus)));
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
		Mobility mobility = mobilityTemplate.findOne(query, Mobility.class);
		if (mobility == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));
		if(3 == mobility.getPartnerId()) {
			if (!mobility.getStatus().equals(STATUS_T_RETURN))
				return utils.getJsonNodeResponse(1, body,
						mapper.createObjectNode().put("message",
								String.format("data.appId %s not request resubmit query. current stage %s status %s", appId,
										mobility.getStage(), mobility.getStatus())));
		}
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
		if(3 == mobility.getPartnerId()) {
			if (returns.hasNonNull("returnQueries")
					&& !(mapper.convertValue(returns.path("returnQueries"), ArrayNode.class)).get(0).path("isComplete")
					.asBoolean())
				return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
						String.format("data.appId %s returnQuery not complete", mobility.getAppId())));
		}
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
		if(3 != mobility.getPartnerId()){
			if(1 == mobility.getPartnerId()){
				rabbitMQService.send("tpf-service-dataentry", Map.of("func", "commentAppNonWeb", "body",
						convertService.toReturnQueryNonWebFinnone(mobility).put("reference_id", body.path("reference_id").asText())));
			} else {
				rabbitMQService.send("tpf-service-dataentry-sgb", Map.of("func", "commentAppNonWeb", "body",
						convertService.toReturnQueryNonWebFinnone(mobility).put("reference_id", body.path("reference_id").asText())));
			}
		} else {
			rabbitMQService.send("tpf-service-esb", Map.of("func", "deResponseQuery", "body",
					convertService.toReturnQueryFinnone(mobility).put("reference_id", body.path("reference_id").asText())));
		}


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

	public JsonNode createField(JsonNode request) throws Exception {
		ArrayNode apps = mapper.convertValue(request.path("body").path("data"), ArrayNode.class);
		if (apps.isNull())
			return utils.getJsonNodeResponse(499, request.path("body").path("data"),
					mapper.createObjectNode().put("message", "data not null"));
		ArrayList<MobilityField> mobilityFields = new ArrayList<MobilityField>();
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
			MobilityField mobilityF = mapper.convertValue(app, MobilityField.class);

//			mobilityF =	MobilityField.builder().appId(appId).build();

			mobilityFields.add(mobilityF);
		}

		JsonNode getDataFieldsDB = rabbitMQService.sendAndReceive("tpf-service-finnone", Map.of("func", "getDataFields",
				"reference_id", request.path("body").path("reference_id"), "body", Map.of("apps", mobilityFields)));

		if (getDataFieldsDB.path("status").asInt(0) != 200) {
			return utils.getJsonNodeResponse(1, request.path("body"), getDataFieldsDB.path("data"));
		}

		ArrayList<MobilityFieldKH> mobilityfieldSender = new ArrayList<MobilityFieldKH>();

		if (getDataFieldsDB.path("data").isArray()) {
			for (JsonNode item : getDataFieldsDB.path("data")) {
				for (MobilityField mobilityField : mobilityFields) {
					if (mobilityField.getAppId().equals(item.path("appId").asText())) {
						MobilityField convert = mapper.convertValue(item, MobilityField.class);
						convert.setComment(mobilityField.getComment());
						convert.setKycNotes(mobilityField.getKycNotes());
						convert.setFieldType(mobilityField.getFieldType());
						if ((convert.getAppStage().toUpperCase().equals("FII"))) {
							Query queryMon = Query.query(Criteria.where("appId").is(convert.getAppId()));
							MobilityField mobilityfieldMon = mobilityTemplate.findOne(queryMon, MobilityField.class);
							Update update = new Update();
							if (mobilityfieldMon != null) {
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
								mobilityfieldMon = mobilityfieldTemplate.findAndModify(queryMon, update,
										new FindAndModifyOptions().returnNew(true), MobilityField.class);

								MobilityField mobilityfieldUpdate = mobilityTemplate.findOne(queryMon,
										MobilityField.class);

								rabbitMQService.send("tpf-service-app", Map.of("func", "updateApp", "reference_id",
										request.path("reference_id"), "param",
										Map.of("project", "mobility", "id", mobilityfieldUpdate.getId()), "body",
										convertService.toAppAutomationSubmitField(mobilityfieldUpdate)));
							} else {
								mobilityfieldTemplate.save(convert);
								Query queryCreate = Query.query(Criteria.where("appId").is(convert.getAppId()));
								MobilityField mobilityfieldCreate = mobilityTemplate.findOne(queryCreate,
										MobilityField.class);
								rabbitMQService.send("tpf-service-app",
										Map.of("func", "createApp", "reference_id",
												request.path("body").path("reference_id"), "body",
												convertService.toAppDisplay(mobilityfieldCreate).put("reference_id",
														request.path("body").path("reference_id").asText())));
								MobilityFieldKH convertKH = mapper.convertValue(convert, MobilityFieldKH.class);
								mobilityfieldSender.add(convertKH);

							}

						}
					}
				}
			}
		}

		if (mobilityfieldSender.size() > 0) {
			new Thread(() -> {
				try {
					int sendCount = 0;
					do {
						ArrayNode array = mapper.convertValue(mobilityfieldSender, ArrayNode.class);
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
		ArrayNode mobilityWaiveFields = JsonNodeFactory.instance.arrayNode();
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
			MobilityWaiveField mobilityWaiveField = mapper.convertValue(app, MobilityWaiveField.class);

			Query queryMon = Query.query(Criteria.where("appId").is(mobilityWaiveField.getAppId()));
			MobilityWaiveField mobilityWaiveFieldMon = mobilitywaivefieldTemplate.findOne(queryMon,
					MobilityWaiveField.class);

			mobilityWaiveField.setProject("mobility");
			mobilityWaiveFields.add(convertService.toESBMobilityWaiveField(mobilityWaiveField));
			if (mobilityWaiveFieldMon != null) {
				Update update = new Update();
				if (mobilityWaiveFieldMon.getAutomationResults() != null) {
					var check_Automation = mapper.convertValue(mobilityWaiveFieldMon.getAutomationResults().get(0),
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
				mobilityWaiveFieldMon = mobilitywaivefieldTemplate.findAndModify(queryMon, update,
						new FindAndModifyOptions().returnNew(true), MobilityWaiveField.class);

				Query queryUpdate = Query.query(Criteria.where("appId").is(mobilityWaiveField.getAppId()));
				MobilityWaiveField mobilityWaiveFieldUpdate = mobilitywaivefieldTemplate.findOne(queryUpdate,
						MobilityWaiveField.class);

				rabbitMQService.send("tpf-service-app",
						Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
								Map.of("project", "mobility", "id", mobilityWaiveFieldUpdate.getId()), "body",
								convertService.toAppAutomationField(mobilityWaiveFieldUpdate)));

			} else {
				HashMap<String, Object> automationResult = new HashMap<>();
				automationResult.put("createdAt", new Date());
				automationResult.put("automationResult", "WAIVE_FIELD_RUN");
				List<Object> list = new ArrayList<>();
				list.add(automationResult);
				mobilityWaiveField.setAutomationResults(list);

				mobilitywaivefieldTemplate.save(mobilityWaiveField);

				Query queryCreate = Query.query(Criteria.where("appId").is(mobilityWaiveField.getAppId()));

				MobilityWaiveField mobilityWaiveFieldCreate = mobilitywaivefieldTemplate.findOne(queryCreate,
						MobilityWaiveField.class);
				rabbitMQService.send("tpf-service-app",
						Map.of("func", "createApp", "reference_id", request.path("body").path("reference_id"), "body",
								convertService.toAppDisplay(mobilityWaiveFieldCreate).put("reference_id",
										request.path("body").path("reference_id").asText())));
			}

		}

		HashMap<String, Object> requestSend = new HashMap<>();
		requestSend.put("func", "waiveField");
		ObjectNode body = mapper.createObjectNode();
		body.put("project", "mobility");
		body.put("transaction_id", "transaction_waive_field");
		body.set("data", mobilityWaiveFields);
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
		MobilityField mobilityfield = mobilityfieldTemplate.findOne(query, MobilityField.class);
		if (mobilityfield == null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.appId %s not exits", appId)));
		if (mobilityfield.getAutomationResults() != null && mobilityfield.getAutomationResults().size() > 0) {
			var check_Automation = mapper.convertValue(mobilityfield.getAutomationResults().get(0), JsonNode.class);
			var autoRun = (check_Automation.path("automationResult").asText().equals("WAIVE_FIELD_RUN"));

			if (autoRun)
				return utils.getJsonNodeResponse(1, body,
						mapper.createObjectNode().put("message", String.format("data.appId %s UPLOADING", appId)));
		}
		if ((mobilityfield.getAppStage() != null && mobilityfield.getAppStage().equals(STAGE_SUBMIT))
				|| (mobilityfield.getAppStatus() != null && mobilityfield.getAppStatus().equals(STATUS_SUBMIT)))
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

		ArrayList<JsonNode> mobilityFields = new ArrayList<JsonNode>();

		mobilityFields.add(mapper.createObjectNode().put("appId", appId));

		JsonNode getDataFieldsDB = rabbitMQService.sendAndReceive("tpf-service-finnone", Map.of("func", "getDataFields",
				"reference_id", request.path("body").path("reference_id"), "body", Map.of("apps", mobilityFields)));

		if (getDataFieldsDB.path("status").asInt(0) != 200) {
			return utils.getJsonNodeResponse(1, request.path("body"), getDataFieldsDB.path("data"));
		}

		if (getDataFieldsDB.path("data").isArray()) {
			for (JsonNode item : getDataFieldsDB.path("data")) {
				if (mobilityfield.getAppId().equals(item.path("appId").asText())) {
					MobilityField check = mapper.convertValue(item, MobilityField.class);
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

		mobilityfield = mobilityfieldTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
				MobilityField.class);

		MobilityField mobilityfieldUpdate = mobilityfieldTemplate.findOne(query, MobilityField.class);

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "mobility", "id", mobilityfieldUpdate.getId()), "body",
						convertService.toAppAutomationSubmitField(mobilityfieldUpdate)));

		ArrayNode mobilitySubmitFields = JsonNodeFactory.instance.arrayNode();

		ObjectNode mobilityObjectNode = mapper.createObjectNode();
		mobilityObjectNode.put("appId", mobilityfield.getAppId());
		mobilityObjectNode.put("phoneConfirmed", mobilityfield.getPhoneConfirmed());
		mobilityObjectNode.put("resultHomeVisit", mobilityfield.getResultHomeVisit());
		mobilityObjectNode.put("resultOfficeVisit", mobilityfield.getResultOfficeVisit());
		mobilityObjectNode.put("result2ndHomeVisit", mobilityfield.getResult2ndHomeVisit());
		mobilityObjectNode.set("attachmentField", filesUploadSender);
		mobilityObjectNode.put("timeOfVisit", mobilityfield.getTimeOfVisit());
		mobilityObjectNode.put("verificationDate", mobilityfield.getVerificationDate());
		mobilityObjectNode.put("remarksDecisionFiv", mobilityfield.getRemarksDecisionFiv());
		mobilityObjectNode.put("remarksDecisionFic", mobilityfield.getRemarksDecisionFic());
		mobilityObjectNode.put("resonDecisionFic", mobilityfield.getResonDecisionFic());

		mobilitySubmitFields.add(mobilityObjectNode);

		HashMap<String, Object> requestSend = new HashMap<>();

		requestSend.put("func", "submitField");
		ObjectNode bodySender = mapper.createObjectNode();
		bodySender.put("project", "mobility");
		bodySender.put("transaction_id", "transaction_submit_field");
		bodySender.set("data", mobilitySubmitFields);
		requestSend.put("body", bodySender);
		requestSend.put("reference_id", request.path("body").path("reference_id"));

		rabbitMQService.send("tpf-service-esb", requestSend);

		return utils.getJsonNodeResponse(0, request.path("body"), null);
	}

	public JsonNode commentApp(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		if (!request.hasNonNull("body"))
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "body not null"));

		if (body.path("data").path("applicationId").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "body.data.applicationId not null"));
		Query query = Query.query(Criteria.where("appId").is(body.path("data").path("applicationId").asText()));

		Mobility mobilityCommentApp = mobilityTemplate.findOne(query, Mobility.class);
		if (mobilityCommentApp == null)
			return utils.getJsonNodeResponse(500, body, mapper.createObjectNode().put("message",
					String.format("id %s mobilityCommentApp not exits", body.path("data").path("applicationId").asText())));

		HashMap<String, Object> commentAppObject = new HashMap<>();

		commentAppObject.put("createdAt", new Date());

		commentAppObject.put("commentApp3P", body.path("data").toString());

		LinkedList<Map> commentApps3PNew = mapper.convertValue(mobilityCommentApp.getCommentApps3PNew(),
				LinkedList.class);
		if (commentApps3PNew == null)
			commentApps3PNew = new LinkedList<Map>();
		commentApps3PNew.push(commentAppObject);
		Update update = new Update().set("updatedAt", new Date());
		update.set("commentApps3PNew", commentApps3PNew);

		mobilityCommentApp = mobilityTemplate.findAndModify(query, update,
				new FindAndModifyOptions().returnNew(true), Mobility.class);

		new Thread(() -> {
			try {
				int sendCount = 0;
				do {
					JsonNode result = apiService.pushCommentApp(body);
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
		return response(200, mapper.convertValue("", JsonNode.class), 0);
	}

	private JsonNode response(int status, JsonNode data, long total) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).put("total", total).set("data", data);
		return response;
	}
}