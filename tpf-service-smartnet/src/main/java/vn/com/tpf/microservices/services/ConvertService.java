package vn.com.tpf.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Smartnet;

@Service
public class ConvertService {
	@Autowired
	private ObjectMapper mapper;
	
	private final String ProductTypeCode = "Personal Finance";
	private final String CustomerType = "Individual";
	private final String SourcingChannel = "DIRECT";
	private final String SourcingBranch = "SMARTNET";
	private final String NatureOfOccupation = "Others";
	private final String Comment = "Comments";
	private final String PreferredModeOfCommunication = "Web-Portal Comments";
	private final String LeadStatus = "Converted";
	private final String CommunicationTranscript = "communicationTranscript";

	

	public ObjectNode toAppDisplay(Smartnet smartnet) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "smartnet");
		app.put("uuid", smartnet.getId());
		if( smartnet.getAppId() != null)
			app.put("appId", smartnet.getAppId());
		app.put("partnerId", smartnet.getLeadId());
		app.put("status", smartnet.getStage());
		if( smartnet.getAutomationResults().size() != 0)
			app.put("automationResult", mapper.convertValue(smartnet.getAutomationResults().get(0),JsonNode.class).path("automationResult").asText());
		app.put("fullName",
				(smartnet.getLastName() + " " + smartnet.getFirstName()).replaceAll("\\s+", " "));
		ArrayNode documents = mapper.createArrayNode();
		
		smartnet.getFilesUpload().forEach(e -> {
			JsonNode smartnetDoc = mapper.convertValue(e,JsonNode.class);
			ObjectNode doc = mapper.createObjectNode();
			doc.put("documentType", smartnetDoc.path("documentCode").asText());
			doc.put("viewUrl", smartnetDoc.path("documentUrlDownload").asText());
			doc.put("downloadUrl", smartnetDoc.path("documentUrlDownload").asText());
			doc.put("updatedAt",smartnet.getUpdatedAt().toString());
			documents.add(doc);
		});
		app.set("documents", documents);
		ObjectNode optional = mapper.createObjectNode();
		
		app.set("optional", optional);
		return app;
	}

	
	public ObjectNode toAppFinnone(Smartnet smartnet ) {
			
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "smartnet");
		app.put("quickLeadId", smartnet.getId());
		ObjectNode quickLead =  mapper.createObjectNode();
		quickLead.put("identificationNumber", smartnet.getNationalId());
		quickLead.put("productTypeCode", ProductTypeCode);
		quickLead.put("customerType", CustomerType);
		quickLead.put("productCode", smartnet.getProductFinnOne());
		quickLead.put("loanAmountRequested", smartnet.getLoanRequest());
		quickLead.put("firstName", smartnet.getFirstName());
		quickLead.put("lastName", smartnet.getLastName());
		quickLead.put("city", smartnet.getCityFinnOne());
		quickLead.put("sourcingChannel", SourcingChannel);
		quickLead.put("dateOfBirth", smartnet.getDateOfBirth());
		quickLead.put("sourcingBranch", SourcingBranch);
		quickLead.put("natureOfOccupation", NatureOfOccupation);
		quickLead.put("schemeCode", smartnet.getSchemeFinnOne());
		quickLead.put("comment", Comment);
		quickLead.put("preferredModeOfCommunication", PreferredModeOfCommunication);
		quickLead.put("leadStatus", LeadStatus);
		quickLead.put("communicationTranscript", CommunicationTranscript);
		
		ArrayNode documents =  mapper.createArrayNode();
		
		smartnet.getFilesUpload().forEach(e -> {
			JsonNode smartnetDoc = mapper.convertValue(e,JsonNode.class);
			ObjectNode doc = mapper.createObjectNode();
			doc.put("type", smartnetDoc.path("type").asText());
			doc.put("originalname", smartnetDoc.path("originalname").asText());
			doc.put("filename", smartnetDoc.path("filename").asText());
			documents.add(doc);
		});
		
		quickLead.set("documents", documents);
			
		app.set("quickLead", quickLead);
		
		return app;
	}
	
	
	public ObjectNode toReturnQueryFinnone(Smartnet smartnet ) {
		
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "smartnet");
		app.put("transaction_id", smartnet.getId());
		app.put("appId", smartnet.getAppId());
	
		JsonNode lastReturnQuery = mapper.convertValue( smartnet.getReturns().get("returnQueries"), ArrayNode.class).get(0);
		app.put("commentText", lastReturnQuery.path("comment").asText());
		ObjectNode dataDocument = mapper.createObjectNode();
		dataDocument.put("fileName", lastReturnQuery.path("data").path("filename").asText());
		dataDocument.put("documentName", lastReturnQuery.path("data").path("type").asText());
			
		app.set("dataDocument", dataDocument);
		
		return app;
	}
	
	public ObjectNode toSaleQueueFinnone(Smartnet smartnet ) {
		
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "smartnet");
		app.put("transaction_id", smartnet.getId());
		app.put("appId", smartnet.getAppId());
	
		JsonNode lastReturnQueue = mapper.convertValue( smartnet.getReturns().get("returnQueues"), ArrayNode.class).get(0);
		app.put("commentText", lastReturnQueue.path("comment").asText());
		ArrayNode dataDocuments = mapper.createArrayNode();
		
		ArrayNode lastReturnQueueDatas  =  mapper.convertValue( lastReturnQueue.path("data"), ArrayNode.class);
		for (JsonNode data : lastReturnQueueDatas) {
			ObjectNode dataDocument = mapper.createObjectNode();
			dataDocument.put("fileName", data.path("filename").asText());
			dataDocument.put("documentName", data.path("type").asText());
			dataDocuments.add(dataDocument);
		}	
		app.set("dataDocuments", dataDocuments);
		
		return app;
	}


}
