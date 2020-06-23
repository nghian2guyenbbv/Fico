package vn.com.tpf.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Crm;
import vn.com.tpf.microservices.models.CrmField;
import vn.com.tpf.microservices.models.CrmWaiveField;
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

	public ObjectNode toAppDisplay(Crm crm) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "crm");
		app.put("uuid", crm.getId());
		if (crm.getAppId() != null)
			app.put("appId", crm.getAppId());
		app.put("partnerId", crm.getLeadId());
		app.put("status", crm.getStatus());
		if (crm.getAutomationResults().size() != 0)
			app.put("automationResult", mapper.convertValue(crm.getAutomationResults().get(0), JsonNode.class)
					.path("automationResult").asText());
		app.put("fullName", (crm.getLastName() + " " + crm.getFirstName()).replaceAll("\\s+", " "));
		ArrayNode documents = mapper.createArrayNode();

		crm.getFilesUpload().forEach(e -> {
			JsonNode crmDoc = mapper.convertValue(e, JsonNode.class);
			ObjectNode doc = mapper.createObjectNode();
			doc.put("documentType", crmDoc.path("documentCode").asText());
			doc.put("viewUrl", crmDoc.path("documentUrlDownload").asText());
			doc.put("downloadUrl", crmDoc.path("documentUrlDownload").asText());
			doc.put("updatedAt", crm.getUpdatedAt().toString());
			documents.add(doc);
		});
		app.set("documents", documents);
		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", crm.getStage());

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppDisplay(CrmField crmField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "crm");
		app.put("uuid", crmField.getId());
		if (crmField.getAppId() != null)
			app.put("appId", crmField.getAppId());
		app.put("status", crmField.getAppStatus());
		if (crmField.getAutomationResults() != null && crmField.getAutomationResults().size() != 0)
			app.put("automationResult", mapper.convertValue(crmField.getAutomationResults().get(0), JsonNode.class)
					.path("automationResult").asText());
		app.put("fullName", (crmField.getFullName()));

		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", crmField.getAppStage());

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppDisplay(CrmWaiveField crmWaiveField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "crm");
		app.put("uuid", crmWaiveField.getId());
		if (crmWaiveField.getAppId() != null)
			app.put("appId", crmWaiveField.getAppId());
//		app.put("status", mobilityWaiveField.getAppStatus());
		if (crmWaiveField.getAutomationResults() != null && crmWaiveField.getAutomationResults().size() != 0)
			app.put("automationResult",
					mapper.convertValue(crmWaiveField.getAutomationResults().get(0), JsonNode.class)
							.path("automationResult").asText());
//		app.put("fullName", (mobilityWaiveField.getFullName()));

		ObjectNode optional = mapper.createObjectNode();
		/* optional.put("stage", mobilityWaiveField.getAppStage()); */

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppAutomation(Crm crm, boolean updateStatus) {
		ObjectNode app = mapper.createObjectNode();
		app.put("appId", crm.getAppId());
		if (updateStatus)
			app.put("status", crm.getStatus());
		if (crm.getAutomationResults() != null && crm.getAutomationResults().size() != 0)
			app.put("automationResult", mapper.convertValue(crm.getAutomationResults().get(0), JsonNode.class)
					.path("automationResult").asText());
		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", crm.getStage());

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppAutomationField(CrmWaiveField crmWaiveField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("appId", crmWaiveField.getAppId());
		if (crmWaiveField.getAutomationResults() != null && crmWaiveField.getAutomationResults().size() != 0)
			app.put("automationResult",
					mapper.convertValue(crmWaiveField.getAutomationResults().get(0), JsonNode.class)
							.path("automationResult").asText());
		return app;
	}

	public ObjectNode toAppAutomationSubmitField(CrmField crmField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("appId", crmField.getAppId());
		if (crmField.getAutomationResults() != null && crmField.getAutomationResults().size() != 0)
			app.put("automationResult", mapper.convertValue(crmField.getAutomationResults().get(0), JsonNode.class)
					.path("automationResult").asText());
		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", crmField.getAppStage());

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppStatus(Crm crm) {
		ObjectNode app = mapper.createObjectNode();
		app.put("status", crm.getStatus());
		return app;
	}

	public ObjectNode toAppStage(Crm crm) {
		ObjectNode app = mapper.createObjectNode();
		ObjectNode optional = mapper.createObjectNode();
		optional.put("stage", crm.getStage());
		app.set("optional", optional);
		return app;
	}

	public ObjectNode toESBCrmWaiveField(CrmWaiveField crmWaiveField) {
		ObjectNode app = mapper.createObjectNode();
		app.put("appId", crmWaiveField.getAppId());
		return app;
	}

	public ObjectNode toAppFinnone(Crm crm) {

		ObjectNode app = mapper.createObjectNode();
		app.put("project", "crm");
		app.put("quickLeadId", crm.getId());
		app.put("neoCustID", crm.getNeoCustID());
		app.put("cifNumber", crm.getCifNumber());
		app.put("idNumber", crm.getIdNumber());
		
		ObjectNode quickLead = mapper.createObjectNode();
		quickLead.put("identificationNumber", crm.getNationalId());
		quickLead.put("productTypeCode", ProductTypeCode);
		quickLead.put("customerType", CustomerType);
		quickLead.put("productCode", crm.getProductFinnOne());
		quickLead.put("loanAmountRequested", crm.getLoanRequest());
		quickLead.put("firstName", crm.getFirstName());
		quickLead.put("lastName", crm.getLastName());
		quickLead.put("city", crm.getCityFinnOne());
		quickLead.put("sourcingChannel", crm.getChanel());
		quickLead.put("dateOfBirth", crm.getDateOfBirth());
		quickLead.put("sourcingBranch", crm.getBranch());
		quickLead.put("natureOfOccupation", NatureOfOccupation);
		quickLead.put("schemeCode", crm.getSchemeFinnOne());
		quickLead.put("comment", Comment);
		quickLead.put("preferredModeOfCommunication", PreferredModeOfCommunication);
		quickLead.put("leadStatus", LeadStatus);
		quickLead.put("communicationTranscript", CommunicationTranscript);

		ArrayNode documents = mapper.createArrayNode();

		crm.getFilesUpload().forEach(e -> {
			JsonNode crmDoc = mapper.convertValue(e, JsonNode.class);
			ObjectNode doc = mapper.createObjectNode();
			doc.put("type", crmDoc.path("type").asText());
			doc.put("originalname", crmDoc.path("originalname").asText());
			doc.put("filename", crmDoc.path("filename").asText());
			documents.add(doc);
		});

		quickLead.set("documents", documents);

		app.set("quickLead", quickLead);

		return app;
	}

	public ObjectNode toReturnQueryFinnone(Crm crm) {

		ObjectNode app = mapper.createObjectNode();
		app.put("project", "crm");
		app.put("transaction_id", crm.getId());
		app.put("appId", crm.getAppId());

		JsonNode lastReturnQuery = mapper.convertValue(crm.getReturns().get("returnQueries"), ArrayNode.class)
				.get(0);
		app.put("commentText", lastReturnQuery.path("comment").asText());
		ObjectNode dataDocument = mapper.createObjectNode();
		dataDocument.put("fileName", lastReturnQuery.path("data").path("filename").asText());
		dataDocument.put("documentName", lastReturnQuery.path("data").path("type").asText());

		app.set("dataDocument", dataDocument);

		return app;
	}

	public ObjectNode toSaleQueueFinnone(Crm crm) {

		ObjectNode app = mapper.createObjectNode();
		app.put("project", "crm");
		app.put("transaction_id", crm.getId());
		app.put("appId", crm.getAppId());

		JsonNode lastReturnQueue = mapper.convertValue(crm.getReturns().get("returnQueues"), ArrayNode.class)
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

}
