package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.com.tpf.microservices.models.Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ConvertService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private GetDataF1Service getDataF1Service;

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

		app.set("optional", optional);
		return app;
	}

	public ObjectNode toApiF1(Application application) {
		log.info("{}", mapper.convertValue(application, JsonNode.class));
		ObjectNode app = mapper.createObjectNode();
		try {
			/*-----------------personInfoType------------------ */
			ArrayNode personInfoType = mapper.createArrayNode();
			ObjectNode person = mapper.createObjectNode();
			person.put("applicantType", application.getQuickLead().getCustomerType().toLowerCase());
			person.put("firstName", application.getQuickLead().getFirstName().toUpperCase());
			person.put("lastName", application.getQuickLead().getLastName().toUpperCase());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dateOfBirth = DateUtils.parseDate(application.getQuickLead().getDateOfBirth(), new String[]{"dd/MM/yyyy"});
			person.put("dateOfBirth", df.format(dateOfBirth));

			person.put("city", getDataF1Service.getCity(application.getQuickLead().getCity()));
			personInfoType.add(person);
			app.set("personInfoType", personInfoType);

			/*-----------------workAndIncome------------------ */
			ObjectNode workAndIncome = mapper.createObjectNode();
			workAndIncome.put("occupationType", getDataF1Service.getOccupationType(application.getQuickLead().getNatureOfOccupation()));
			workAndIncome.put("natureOfOccuptaion", "Unemployed");

			app.set("workAndIncome", workAndIncome);

			/*-----------------productProcessor------------------ */
			app.put("productProcessor", "mCAS");

			/*-----------------documents------------------ */
			ArrayNode documents = mapper.createArrayNode();
			application.getQuickLead().getDocuments().stream().forEach(qlDocument -> {
				ObjectNode document = mapper.createObjectNode();
				document.put("referenceType", "Customer");
				document.put("entityType", application.getQuickLead().getFirstName() + application.getQuickLead().getLastName());
				document.put("documentName", qlDocument.getType());
				document.put("recievingDate", df.format(new Date()));
				document.put("remarks", "Document received");

				ObjectNode attachmentDetails = mapper.createObjectNode();
				attachmentDetails.put("attachedDocName", qlDocument.getOriginalname());
				attachmentDetails.put("attachedDocument", qlDocument.getFilename());
				document.set("attachmentDetails", attachmentDetails);
				documents.add(document);
			});
			app.set("documents", documents);

			/*-----------------sourcingDetails------------------ */
			ObjectNode sourcingDetails = mapper.createObjectNode();
			sourcingDetails.put("sourcingChannel", getDataF1Service.getSourcingChannel(application.getQuickLead().getSourcingChannel()));
			app.set("sourcingDetails", sourcingDetails);

			/*-----------------loanInformation------------------ */
			ObjectNode loanInformation = mapper.createObjectNode();

			loanInformation.put("productType", getDataF1Service.getProductType());
			loanInformation.put("loanProduct", getDataF1Service.getLoanProduct(application.getQuickLead().getSchemeCode()));
			loanInformation.put("scheme", getDataF1Service.getScheme(application.getQuickLead().getSchemeCode()));
			loanInformation.set("loanAmountRequested",
					mapper.createObjectNode().put("currencyCode", "VND").put("value", Long.valueOf(application.getQuickLead().getLoanAmountRequested())));
			app.set("loanInformation", loanInformation);

			/*-----------------communicationDetails------------------ */
			ObjectNode communicationDetails = mapper.createObjectNode();
			communicationDetails.put("modeOfCommunication", application.getQuickLead().getPreferredModeOfCommunication());
			communicationDetails.put("leadStatus", getDataF1Service.getLeadStatus(application.getQuickLead().getLeadStatus()));
			communicationDetails.put("addComment", application.getQuickLead().getCommunicationTranscript());
			communicationDetails.put("contactedBy", "System");
			communicationDetails.put("date", df.format(new Date()));
			app.set("communicationDetails", communicationDetails);

			app.put("moveToNextStageFlag", true);

			app.put("userName", "System");

			app.put("branchcode", getDataF1Service.getBranchCode(application.getQuickLead().getSourcingBranch()));
		}catch (Exception e){
			log.info("{}", e.toString());
		}
		return app;
	}

}
