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
	
	public ObjectNode toAppFinnoneWithFullApp(Crm crm) {

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
		
		ObjectNode fullApp = mapper.createObjectNode();
		fullApp.put("firstName", crm.getFirstName());
		fullApp.put("middleName", crm.getMiddleName());
		fullApp.put("lastName", crm.getLastName());
		fullApp.put("gender", crm.getGender());
		fullApp.put("dateOfBirth", crm.getDateOfBirth());
		fullApp.put("maritalStatus", crm.getMaritalStatus());
		fullApp.put("primaryAddress", crm.getPrimaryAddress());
		fullApp.put("phoneNumber", crm.getPhoneNumber());
		fullApp.put("employmentName", crm.getEmploymentName());
		fullApp.put("incomeExpense", crm.getIncomeExpense());
		fullApp.put("amount", crm.getAmount());
		fullApp.put("modeOfPayment", crm.getModeOfPayment());
		fullApp.put("dayOfSalaryPayment", crm.getDayOfSalaryPayment());
		fullApp.put("sourcingBranch", crm.getBranch());
		fullApp.put("sourcingChannel", crm.getChanel());
		fullApp.put("loanApplicationType", crm.getLoanApplicationType());
		fullApp.put("productCode", crm.getProductFinnOne());
		fullApp.put("schemeCode", crm.getSchemeFinnOne());
		fullApp.put("loanAmountRequested", crm.getLoanAmountRequested());
		fullApp.put("requestedTenure", crm.getRequestedTenure());
		fullApp.put("interestRate", crm.getInterestRate());
		fullApp.put("saleAgentCodeLoanDetails", crm.getSaleAgentCodeLoanDetails());
		fullApp.put("vapProduct", crm.getVapProduct());
		fullApp.put("vapTreatment", crm.getVapTreatment());
		fullApp.put("insuranceCompany", crm.getInsuranceCompany());
		fullApp.put("loanPurpose", crm.getLoanPurpose());
		fullApp.put("loanPurposeOther", crm.getLoanPurposeOther());
		fullApp.put("numberOfDependents", crm.getNumberOfDependents());
		fullApp.put("monthlyRental", crm.getMonthlyRental());
		fullApp.put("houseOwnership", crm.getHouseOwnership());
		fullApp.put("newBankCardNumber", crm.getNewBankCardNumber());
		fullApp.put("remarksDynamicForm", crm.getRemarksDynamicForm());
		fullApp.put("saleAgentCodeDynamicForm", crm.getSaleAgentCodeDynamicForm());
		fullApp.put("courierCode", crm.getCourierCode());
		fullApp.put("maximumInterestedRate", crm.getMaximumInterestedRate());
		

		ArrayNode documents = mapper.createArrayNode();
		ArrayNode addresses = mapper.createArrayNode();
		ArrayNode references = mapper.createArrayNode();
		ArrayNode family = mapper.createArrayNode();
		ArrayNode identifications = mapper.createArrayNode();

		crm.getFilesUpload().forEach(e -> {
			JsonNode crmDoc = mapper.convertValue(e, JsonNode.class);
			ObjectNode doc = mapper.createObjectNode();
			doc.put("type", crmDoc.path("type").asText());
			doc.put("originalname", crmDoc.path("originalname").asText());
			doc.put("filename", crmDoc.path("filename").asText());
			documents.add(doc);
		});
		
		crm.getAddresses().forEach(e -> {
			JsonNode crmAdresses = mapper.convertValue(e, JsonNode.class);
			addresses.add(crmAdresses);
		});
		
		crm.getReferences().forEach(e -> {
			JsonNode crmReferences= mapper.convertValue(e, JsonNode.class);
			references.add(crmReferences);
		});
		crm.getFamily().forEach(e -> {
			JsonNode crmFamilys= mapper.convertValue(e, JsonNode.class);
			family.add(crmFamilys);
		});
		crm.getIdentifications().forEach(e -> {
			JsonNode crmIdentifications= mapper.convertValue(e, JsonNode.class);
			identifications.add(crmIdentifications);
		});
		quickLead.set("documents", documents);
		fullApp.set("addresses", addresses);
		fullApp.set("documents", documents);
		fullApp.set("references", references);
		fullApp.set("family", family);
		fullApp.set("identifications", identifications);
		app.set("quickLead", quickLead);
		app.set("fullInfoApp", fullApp);

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

	public ObjectNode toSaleQueueFinnoneAndSenback(Crm crm) {

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

		ArrayNode documents = mapper.createArrayNode();

		if (lastReturnQueue.hasNonNull("data")) {
			ArrayNode lastReturnQueueDatas = mapper.convertValue(lastReturnQueue.path("data"), ArrayNode.class);
			for (JsonNode data : lastReturnQueueDatas) {
				ObjectNode dataDocument = mapper.createObjectNode();
				dataDocument.put("type", data.path("type").asText());
				dataDocument.put("filename", data.path("filename").asText());
				dataDocument.put("originalname", data.path("originalname").asText());
				documents.add(dataDocument);
			}
		}

		ObjectNode fullApp = mapper.createObjectNode();
		fullApp.put("firstName", lastReturnQueue.path("firstName").asText());
		fullApp.put("middleName", lastReturnQueue.path("middleName").asText());
		fullApp.put("lastName", lastReturnQueue.path("lastName").asText());
		fullApp.put("gender", lastReturnQueue.path("gender").asText());
		fullApp.put("dateOfBirth", lastReturnQueue.path("dateOfBirth").asText());
		fullApp.put("maritalStatus", lastReturnQueue.path("maritalStatus").asText());
		fullApp.put("primaryAddress", lastReturnQueue.path("primaryAddress").asText());
		fullApp.put("phoneNumber", lastReturnQueue.path("phoneNumber").asText());
		fullApp.put("employmentName", lastReturnQueue.path("employmentName").asText());
		fullApp.put("incomeExpense", lastReturnQueue.path("incomeExpense").asText());
		fullApp.put("amount", lastReturnQueue.path("amount").asText());
		fullApp.put("modeOfPayment", lastReturnQueue.path("modeOfPayment").asText());
		fullApp.put("dayOfSalaryPayment", lastReturnQueue.path("dayOfSalaryPayment").asText());
		fullApp.put("sourcingBranch", lastReturnQueue.path("sourcingBranch").asText());
		fullApp.put("sourcingChannel", lastReturnQueue.path("sourcingChannel").asText());
		fullApp.put("loanApplicationType", lastReturnQueue.path("loanApplicationType").asText());
		fullApp.put("productCode", crm.getProductFinnOne());
		fullApp.put("schemeCode", crm.getSchemeFinnOne());
		fullApp.put("loanAmountRequested", lastReturnQueue.path("loanAmountRequested").asText());
		fullApp.put("requestedTenure", lastReturnQueue.path("requestedTenure").asText());
		fullApp.put("interestRate", lastReturnQueue.path("interestRate").asText());
		fullApp.put("saleAgentCodeLoanDetails", lastReturnQueue.path("saleAgentCodeLoanDetails").asText());
		fullApp.put("vapProduct", lastReturnQueue.path("vapProduct").asText());
		fullApp.put("vapTreatment", lastReturnQueue.path("vapTreatment").asText());
		fullApp.put("insuranceCompany", lastReturnQueue.path("insuranceCompany").asText());
		fullApp.put("loanPurpose", lastReturnQueue.path("loanPurpose").asText());
		fullApp.put("loanPurposeOther", lastReturnQueue.path("loanPurposeOther").asText());
		fullApp.put("numberOfDependents", lastReturnQueue.path("numberOfDependents").asText());
		fullApp.put("monthlyRental", lastReturnQueue.path("monthlyRental").asText());
		fullApp.put("houseOwnership", lastReturnQueue.path("houseOwnership").asText());
		fullApp.put("newBankCardNumber", lastReturnQueue.path("newBankCardNumber").asText());
		fullApp.put("remarksDynamicForm", lastReturnQueue.path("remarksDynamicForm").asText());
		fullApp.put("saleAgentCodeDynamicForm", lastReturnQueue.path("saleAgentCodeDynamicForm").asText());
		fullApp.put("courierCode", lastReturnQueue.path("courierCode").asText());
		fullApp.put("maximumInterestedRate", lastReturnQueue.path("maximumInterestedRate").asText());



		ArrayNode addresses = mapper.createArrayNode();
		ArrayNode references = mapper.createArrayNode();
		ArrayNode family = mapper.createArrayNode();
		ArrayNode identifications = mapper.createArrayNode();


		if (lastReturnQueue.hasNonNull("addresses")) {
			ArrayNode lastReturnQueueDatas = mapper.convertValue(lastReturnQueue.path("addresses"), ArrayNode.class);
			for (JsonNode data : lastReturnQueueDatas) {
				ObjectNode dataDocument = mapper.createObjectNode();
				dataDocument.put("addressType", data.path("addressType").asText());
				dataDocument.put("addressLine1", data.path("addressLine1").asText());
				dataDocument.put("addressLine2", data.path("addressLine2").asText());
				dataDocument.put("addressLine3", data.path("addressLine3").asText());
				dataDocument.put("area", data.path("area").asText());
				dataDocument.put("city", data.path("city").asText());
				addresses.add(dataDocument);
			}
		}

		if (lastReturnQueue.hasNonNull("references")) {
			ArrayNode lastReturnQueueDatas = mapper.convertValue(lastReturnQueue.path("references"), ArrayNode.class);
			for (JsonNode data : lastReturnQueueDatas) {
				ObjectNode dataDocument = mapper.createObjectNode();
				dataDocument.put("name", data.path("name").asText());
				dataDocument.put("relationship", data.path("relationship").asText());
				dataDocument.put("phoneNumber", data.path("phoneNumber").asText());
				references.add(dataDocument);
			}
		}

		if (lastReturnQueue.hasNonNull("family")) {
			ArrayNode lastReturnQueueDatas = mapper.convertValue(lastReturnQueue.path("family"), ArrayNode.class);
			for (JsonNode data : lastReturnQueueDatas) {
				ObjectNode dataDocument = mapper.createObjectNode();
				dataDocument.put("memberName", data.path("memberName").asText());
				dataDocument.put("phoneNumber", data.path("phoneNumber").asText());
				dataDocument.put("relationship", data.path("relationship").asText());
				family.add(dataDocument);
			}
		}

		if (lastReturnQueue.hasNonNull("identifications")) {
			ArrayNode lastReturnQueueDatas = mapper.convertValue(lastReturnQueue.path("identifications"), ArrayNode.class);
			for (JsonNode data : lastReturnQueueDatas) {
				ObjectNode dataDocument = mapper.createObjectNode();
				dataDocument.put("identificationType", data.path("identificationType").asText());
				dataDocument.put("identificationNumber", data.path("identificationNumber").asText());
				dataDocument.put("issuingCountry", data.path("issuingCountry").asText());
				dataDocument.put("placeOfIssue", data.path("placeOfIssue").asText());
				dataDocument.put("issueDate", data.path("issueDate").asText());
				dataDocument.put("expiryDate", data.path("expiryDate").asText());
				identifications.add(dataDocument);
			}
		}

		fullApp.set("addresses", addresses);
		fullApp.set("documents", documents);
		fullApp.set("references", references);
		fullApp.set("family", family);
		fullApp.set("identifications", identifications);
		app.set("fullInfoApp", fullApp);

		return app;
	}
}
