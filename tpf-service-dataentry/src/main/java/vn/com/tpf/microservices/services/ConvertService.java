package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.com.tpf.microservices.models.Application;

@Service
public class ConvertService {

	@Autowired
	private ObjectMapper mapper;

	public ObjectNode toAppDisplay(Application application) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "dataentry");
		app.put("uuid", application.getId());
		app.put("status", application.getStatus());
		app.put("appId", application.getApplicationId());
//		app.put("status", application.getStatus());
		if (application.getApplicationInformation() != null){
			app.put("fullName", StringUtils.trimWhitespace(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getFullName()));
		}else {
			app.put("fullName", (StringUtils.trimWhitespace(application.getQuickLead().getFirstName())+ " " + StringUtils.trimWhitespace(application.getQuickLead().getLastName())));
		}
//		app.put("automationResult", application.getDescription());
//		app.put("assigned", application.getUserName());
		ArrayNode documents = mapper.createArrayNode();
		if(application.getQuickLead().getDocuments() != null) {
			application.getQuickLead().getDocuments().forEach(e -> {
				ObjectNode doc = mapper.createObjectNode();
				doc.put("documentType", e.getOriginalname());
				doc.put("viewUrl", e.getFilename());
				doc.put("downloadUrl", e.getFilename());
				doc.put("type", e.getType());
//			doc.set("updatedAt", mapper.convertValue(e.getUpdatedAt(), JsonNode.class));
				documents.add(doc);
			});
			app.set("documents", documents);
		}

		ArrayNode comments = mapper.createArrayNode();
		if(application.getComment() != null){
			application.getComment().forEach(e -> {
				ObjectNode doc = mapper.createObjectNode();
				doc.put("commentId", e.getCommentId());
				doc.put("state", e.getStage());
				doc.put("type", e.getType());
				doc.put("code", e.getCode());
				doc.put("request", e.getRequest());

				try {
					doc.put("response", mapper.writeValueAsString(e.getResponse()));
					doc.put("responseJson", mapper.convertValue(e.getResponse(), ObjectNode.class));
					doc.put("createdAt", mapper.convertValue(e.getCreatedDate(), JsonNode.class));
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
				comments.add(doc);
			});
			app.set("comments", comments);
		}
		ObjectNode optional = mapper.createObjectNode();
		if (application.getApplicationInformation() != null) {
			optional.put("identificationNumber", application.getApplicationInformation().getPersonalInformation().getIdentifications().get(0).getIdentificationNumber());
		}else if (application.getQuickLead().getIdentificationNumber() != null){
			optional.put("identificationNumber", application.getQuickLead().getIdentificationNumber());
		}
		if (application.getQuickLeadId() != null) {
			optional.put("quickLeadId", application.getQuickLeadId());
			if (application.getQuickLead().getSourcingBranch() != null) {
				optional.put("branchName", application.getQuickLead().getSourcingBranch());
			}
			if (application.getQuickLead().getSchemeCode() != null) {
				optional.put("schemeCode", application.getQuickLead().getSchemeCode());
			}
		}
		if (application.getUserName_DE() != null) {
			optional.put("userName_DE", application.getUserName_DE());
		}

		if (!StringUtils.isEmpty(application.getPartnerId())) {
			optional.put("partnerId", application.getPartnerId());
		}
		if (!StringUtils.isEmpty(application.getPartnerName())) {
			optional.put("partnerName", application.getPartnerName());
		}

		try{
            optional.put("reasonCancel", application.getReasonCancel());
			optional.put("createdBy", application.getUserName());
		} catch (Exception e) {
		}


//		optional.put("isHolding", application.isHolding());

		app.set("optional", optional);
		return app;
	}

}
