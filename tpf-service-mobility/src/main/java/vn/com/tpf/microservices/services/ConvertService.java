package vn.com.tpf.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Builder;
import vn.com.tpf.microservices.models.Mobility;
import vn.com.tpf.microservices.models.MobilityField;
import vn.com.tpf.microservices.models.MobilityWaiveField;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ConvertService {
	@Autowired
	private ObjectMapper mapper;

	private final String ProductTypeCode = "Personal Finance";
	private final String CustomerType = "Individual";
//	private final String SourcingChannel = "DIRECT";
//	private final String SourcingBranch = "SMARTNET";
	private final String NatureOfOccupation = "Others";
	private final String Comment = "Comments";
	private final String PreferredModeOfCommunication = "Web-Portal Comments";
	private final String LeadStatus = "Converted";
	private final String CommunicationTranscript = "communicationTranscript";

	public ObjectNode toAppDisplay(Mobility mobility) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "mobility");
		app.put("uuid", mobility.getId());
		if (mobility.getAppId() != null)
			app.put("appId", mobility.getAppId());
		app.put("partnerId", mobility.getLeadId());
		app.put("status", mobility.getStatus());
		if (mobility.getAutomationResults().size() != 0)
			app.put("automationResult", mapper.convertValue(mobility.getAutomationResults().get(0), JsonNode.class)
					.path("automationResult").asText());
		app.put("fullName", (mobility.getLastName() + " " + mobility.getFirstName()).replaceAll("\\s+", " "));
		ArrayNode documents = mapper.createArrayNode();

		mobility.getFilesUpload().forEach(e -> {
			JsonNode mobilityDoc = mapper.convertValue(e, JsonNode.class);
			ObjectNode doc = mapper.createObjectNode();
			doc.put("documentType", mobilityDoc.path("documentCode").asText());
			doc.put("viewUrl", mobilityDoc.path("documentUrlDownload").asText());
			doc.put("downloadUrl", mobilityDoc.path("documentUrlDownload").asText());
			doc.put("updatedAt", mobility.getUpdatedAt().toString());
			documents.add(doc);
		});
		app.set("documents", documents);
		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", mobility.getStage());

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppDisplay(MobilityField mobilityField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "mobility");
		app.put("uuid", mobilityField.getId());
		if (mobilityField.getAppId() != null)
			app.put("appId", mobilityField.getAppId());
		app.put("status", mobilityField.getAppStatus());
		if (mobilityField.getAutomationResults() != null && mobilityField.getAutomationResults().size() != 0)
			app.put("automationResult", mapper.convertValue(mobilityField.getAutomationResults().get(0), JsonNode.class)
					.path("automationResult").asText());
		app.put("fullName", (mobilityField.getFullName()));

		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", mobilityField.getAppStage());

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppDisplay(MobilityWaiveField mobilityWaiveField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "mobility");
		app.put("uuid", mobilityWaiveField.getId());
		if (mobilityWaiveField.getAppId() != null)
			app.put("appId", mobilityWaiveField.getAppId());
//		app.put("status", mobilityWaiveField.getAppStatus());
		if (mobilityWaiveField.getAutomationResults() != null && mobilityWaiveField.getAutomationResults().size() != 0)
			app.put("automationResult",
					mapper.convertValue(mobilityWaiveField.getAutomationResults().get(0), JsonNode.class)
							.path("automationResult").asText());
//		app.put("fullName", (mobilityWaiveField.getFullName()));

		ObjectNode optional = mapper.createObjectNode();
		/* optional.put("stage", mobilityWaiveField.getAppStage()); */

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppAutomation(Mobility mobility, boolean updateStatus) {
		ObjectNode app = mapper.createObjectNode();
		app.put("appId", mobility.getAppId());
		if (updateStatus)
			app.put("status", mobility.getStatus());
		if (mobility.getAutomationResults() != null && mobility.getAutomationResults().size() != 0)
			app.put("automationResult", mapper.convertValue(mobility.getAutomationResults().get(0), JsonNode.class)
					.path("automationResult").asText());
		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", mobility.getStage());

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppAutomationField(MobilityWaiveField mobilityWaiveField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("appId", mobilityWaiveField.getAppId());
		if (mobilityWaiveField.getAutomationResults() != null && mobilityWaiveField.getAutomationResults().size() != 0)
			app.put("automationResult",
					mapper.convertValue(mobilityWaiveField.getAutomationResults().get(0), JsonNode.class)
							.path("automationResult").asText());
		return app;
	}

	public ObjectNode toAppAutomationSubmitField(MobilityField mobilityField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("appId", mobilityField.getAppId());
		if (mobilityField.getAutomationResults() != null && mobilityField.getAutomationResults().size() != 0)
			app.put("automationResult", mapper.convertValue(mobilityField.getAutomationResults().get(0), JsonNode.class)
					.path("automationResult").asText());
		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", mobilityField.getAppStage());

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppStatus(Mobility mobility) {
		ObjectNode app = mapper.createObjectNode();
		app.put("status", mobility.getStatus());
		return app;
	}

	public ObjectNode toAppStage(Mobility mobility) {
		ObjectNode app = mapper.createObjectNode();
		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", mobility.getStage());
		app.set("optional", optional);
		return app;
	}

	public ObjectNode toESBMobilityWaiveField(MobilityWaiveField mobilityWaiveField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("appId", mobilityWaiveField.getAppId());
		return app;
	}

	public ObjectNode toAppFinnone(Mobility mobility) {

		ObjectNode app = mapper.createObjectNode();
		app.put("project", "mobility");
		app.put("quickLeadId", mobility.getId());
		ObjectNode quickLead = mapper.createObjectNode();
		quickLead.put("identificationNumber", mobility.getNationalId());
		quickLead.put("productTypeCode", ProductTypeCode);
		quickLead.put("customerType", CustomerType);
		quickLead.put("productCode", mobility.getProductFinnOne());
		quickLead.put("loanAmountRequested", mobility.getLoanRequest());
		quickLead.put("firstName", mobility.getFirstName());
		quickLead.put("lastName", mobility.getLastName());
		quickLead.put("city", mobility.getCityFinnOne());
		quickLead.put("sourcingChannel", mobility.getChanel());
		quickLead.put("dateOfBirth", mobility.getDateOfBirth());
		quickLead.put("sourcingBranch", mobility.getBranch());
		quickLead.put("natureOfOccupation", NatureOfOccupation);
		quickLead.put("schemeCode", mobility.getSchemeFinnOne());
		quickLead.put("comment", Comment);
		quickLead.put("preferredModeOfCommunication", PreferredModeOfCommunication);
		quickLead.put("leadStatus", LeadStatus);
		quickLead.put("communicationTranscript", CommunicationTranscript);

		ArrayNode documents = mapper.createArrayNode();

		mobility.getFilesUpload().forEach(e -> {
			JsonNode mobilityDoc = mapper.convertValue(e, JsonNode.class);
			ObjectNode doc = mapper.createObjectNode();
			doc.put("type", mobilityDoc.path("type").asText());
			doc.put("originalname", mobilityDoc.path("originalname").asText());
			doc.put("filename", mobilityDoc.path("filename").asText());
			documents.add(doc);
		});

		quickLead.set("documents", documents);

		app.set("quickLead", quickLead);

		return app;
	}

	public ObjectNode toReturnQueryFinnone(Mobility mobility) {

		ObjectNode app = mapper.createObjectNode();
		app.put("project", "mobility");
		app.put("transaction_id", mobility.getId());
		app.put("appId", mobility.getAppId());

		JsonNode lastReturnQuery = mapper.convertValue(mobility.getReturns().get("returnQueries"), ArrayNode.class)
				.get(0);
		app.put("commentText", lastReturnQuery.path("comment").asText());
		ObjectNode dataDocument = mapper.createObjectNode();
		dataDocument.put("fileName", lastReturnQuery.path("data").path("filename").asText());
		dataDocument.put("documentName", lastReturnQuery.path("data").path("type").asText());

		app.set("dataDocument", dataDocument);

		return app;
	}

	public ObjectNode toSaleQueueFinnone(Mobility mobility) {

		ObjectNode app = mapper.createObjectNode();
		app.put("project", "mobility");
		app.put("transaction_id", mobility.getId());
		app.put("appId", mobility.getAppId());

		JsonNode lastReturnQueue = mapper.convertValue(mobility.getReturns().get("returnQueues"), ArrayNode.class)
				.get(0);
		app.put("commentText", lastReturnQueue.path("comment").asText());
		ArrayNode dataDocuments = mapper.createArrayNode();

		if (lastReturnQueue.hasNonNull("data")) {
			ArrayNode lastReturnQueueDatas = mapper.convertValue(lastReturnQueue.path("data"), ArrayNode.class);
			for (JsonNode data : lastReturnQueueDatas) {
				ObjectNode dataDocument = mapper.createObjectNode();
				dataDocument.put("fileName", data.path("filename").asText());
				dataDocument.put("documentName", data.path("type").asText());
				dataDocuments.add(dataDocument);
			}

			app.set("dataDocuments", dataDocuments);
		} else {
			app.set("dataDocuments", mapper.createArrayNode());
		}

		return app;
	}

	public ObjectNode toSendAppNonWebFinnone(Mobility mobility, String partnerId, String requestId) {

		ObjectNode app = mapper.createObjectNode();
		app.put("request_id", requestId);
		app.put("date_time", ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

		ObjectNode quickLead = mapper.createObjectNode();
		quickLead.put("project", "mobility");
		quickLead.put("partnerId", partnerId);
		quickLead.put("appId", mobility.getAppId());
		quickLead.put("quickLeadId", mobility.getId());
		quickLead.put("identificationNumber", mobility.getNationalId());
		quickLead.put("productTypeCode", ProductTypeCode);
		quickLead.put("customerType", CustomerType);
		quickLead.put("productCode", mobility.getProductFinnOne());
		quickLead.put("loanAmountRequested", mobility.getLoanRequest());
		quickLead.put("firstName", mobility.getFirstName());
		quickLead.put("lastName", mobility.getLastName());
		quickLead.put("city", mobility.getCityFinnOne());
		quickLead.put("sourcingChannel", mobility.getChanel());
		quickLead.put("dateOfBirth", mobility.getDateOfBirth());
		quickLead.put("sourcingBranch", mobility.getBranch());
		quickLead.put("natureOfOccupation", NatureOfOccupation);
		quickLead.put("schemeCode", mobility.getSchemeFinnOne());
		quickLead.put("comment", Comment);
		quickLead.put("preferredModeOfCommunication", PreferredModeOfCommunication);
		quickLead.put("leadStatus", LeadStatus);
		quickLead.put("communicationTranscript", CommunicationTranscript);

		ArrayNode documents = mapper.createArrayNode();

		mobility.getFilesUpload().forEach(e -> {
			JsonNode mobilityDoc = mapper.convertValue(e, JsonNode.class);
			ObjectNode doc = mapper.createObjectNode();
			doc.put("type", mobilityDoc.path("type").asText());
			doc.put("originalname", mobilityDoc.path("originalname").asText());
			doc.put("filename", mobilityDoc.path("filename").asText());
			documents.add(doc);
		});

		quickLead.set("documents", documents);

		app.set("data", quickLead);

		return app;
	}

}
