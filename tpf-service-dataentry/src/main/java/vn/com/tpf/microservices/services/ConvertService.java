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
import vn.com.tpf.microservices.models.leadCreation.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

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
			LeadCreationRequest leadCreationRequest = new LeadCreationRequest();

			/*---------------------------------------------------personInfoType---------------------------------------------------- */
			PersonInfo personInfo = new PersonInfo();
			personInfo.setApplicantType(application.getQuickLead().getCustomerType().toLowerCase());
			personInfo.setFirstName(application.getQuickLead().getFirstName().toUpperCase());
			personInfo.setLastName(application.getQuickLead().getLastName().toUpperCase());
			/*-------------------------dummy-------------------------*/
			personInfo.setLeadApplicantId("");
			personInfo.setApplicantRole("");
			personInfo.setMobilePhoneNumber("");
			personInfo.setMobilePhoneIsdCode("");
			personInfo.setBorrowerType("");
			personInfo.setFieldPerInfo1("");
			personInfo.setFieldPerInfo2("");
			personInfo.setFieldPerInfo3("");
			personInfo.setFieldPerInfo4("");
			personInfo.setFieldPerInfo5("");
			personInfo.setFieldPerInfo6("");
			personInfo.setFieldPerInfo7("");
			personInfo.setFieldPerInfo8("");
			personInfo.setFieldPerInfo9("");
			personInfo.setFieldPerInfo10("");

			Date dateOfBirth = DateUtils.parseDate(application.getQuickLead().getDateOfBirth(), new String[]{"dd/MM/yyyy"});

			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(dateOfBirth);
			personInfo.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
			personInfo.setCity(getDataF1Service.getCity(application.getQuickLead().getCity()));

			/*---------------------------------------------------workAndIncome---------------------------------------------------- */
			WorkAndIncomeType workAndIncomeType = new WorkAndIncomeType();
			workAndIncomeType.setOccupationType(getDataF1Service.getOccupationType(application.getQuickLead().getNatureOfOccupation()));
			workAndIncomeType.setNatureOfOccuptaion("Unemployed");

			/*-------------------------dummy-------------------------*/
			workAndIncomeType.setEmployerName("");
			workAndIncomeType.setWorkingHereForPastMonths("");
			workAndIncomeType.setWorkingHereForPastYears("");
			workAndIncomeType.setTotalWorkExpereinceMonths("");
			workAndIncomeType.setTotalWorkExpereinceYears("");
			workAndIncomeType.setNatureOfProfession("");
			workAndIncomeType.setGrossMonthlySalary(new AmountField());
			workAndIncomeType.setMonthlyTakeHomeSalary(new AmountField());
			workAndIncomeType.setTotalInstallmentCurrentlyPay("");
			workAndIncomeType.setOrganizationName("");
			workAndIncomeType.setYearInBusinessMonths("");
			workAndIncomeType.setYearInBusinessYears("");
			workAndIncomeType.setNetProfit("");
			workAndIncomeType.setStartDateOfCurrentProfession("");
			workAndIncomeType.setStartDateOfBusiness("");
			workAndIncomeType.setLatestYearGrossTotalIncome(new AmountField());
			workAndIncomeType.setOthers("");
			workAndIncomeType.setFieldworkAndIncome1("");
			workAndIncomeType.setFieldworkAndIncome2("");
			workAndIncomeType.setFieldworkAndIncome3("");
			workAndIncomeType.setFieldworkAndIncome4("");
			workAndIncomeType.setFieldworkAndIncome5("");
			workAndIncomeType.setFieldworkAndIncome6("");
			workAndIncomeType.setFieldworkAndIncome7("");
			workAndIncomeType.setFieldworkAndIncome8("");
			workAndIncomeType.setFieldworkAndIncome9("");
			workAndIncomeType.setFieldworkAndIncome10("");

			personInfo.setWorkAndIncome(workAndIncomeType);

			leadCreationRequest.getPersonInfoType().add(personInfo);

			/*---------------------------------------------------productProcessor---------------------------------------------------- */
			leadCreationRequest.setProductProcessor("mCAS");

			/*-----------------------------------------------------documents------------------------------------------------------ */
			application.getQuickLead().getDocuments().stream().forEach(qlDocument -> {
				Documents documents = new Documents();
				documents.setReferenceType("Customer");
				documents.setEntityType(application.getQuickLead().getFirstName().toUpperCase() + " " + application.getQuickLead().getLastName().toUpperCase());
				documents.setDocumentName(qlDocument.getType());

				cal.setTime(new Date());
				try {
					documents.setRecievingDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
				} catch (DatatypeConfigurationException e) {
					log.info("{}", e.toString());
				}

				documents.setRemarks("Document received");

				AttachmentDetails attachmentDetails = new AttachmentDetails();
				attachmentDetails.setAttachedDocName(qlDocument.getOriginalname());
				attachmentDetails.setAttachedDocument(qlDocument.getFilename());
				documents.setAttachmentDetails(attachmentDetails);
				leadCreationRequest.getDocuments().add(documents);
			});

			/*---------------------------------------------------sourcingDetails---------------------------------------------------- */
			SourcingDetails sourcingDetails = new SourcingDetails();
			sourcingDetails.setSourcingChannel(getDataF1Service.getSourcingChannel(application.getQuickLead().getSourcingChannel()));

			/*-------------------------dummy-------------------------*/
			sourcingDetails.setAlternateChannelMode("");
			sourcingDetails.setSourcingBranch("");
			sourcingDetails.setEmployeeName("");
			sourcingDetails.setEmployeeNumber("");
			sourcingDetails.setFieldsourcingDetails1("");
			sourcingDetails.setFieldsourcingDetails2("");
			sourcingDetails.setFieldsourcingDetails3("");
			sourcingDetails.setFieldsourcingDetails4("");
			sourcingDetails.setFieldsourcingDetails5("");
			sourcingDetails.setFieldsourcingDetails6("");
			sourcingDetails.setFieldsourcingDetails7("");
			sourcingDetails.setFieldsourcingDetails8("");
			sourcingDetails.setFieldsourcingDetails9("");
			sourcingDetails.setFieldsourcingDetails10("");

			leadCreationRequest.setSourcingDetails(sourcingDetails);

			/*---------------------------------------------------loanInformation---------------------------------------------------- */
			LoanInformation loanInformation = new LoanInformation();
			loanInformation.setProductType(getDataF1Service.getProductType());
			loanInformation.setLoanProduct(getDataF1Service.getLoanProduct(application.getQuickLead().getSchemeCode()));
			loanInformation.setScheme(getDataF1Service.getScheme(application.getQuickLead().getSchemeCode()));
			AmountField amountField = new AmountField();
			amountField.setValue(BigDecimal.valueOf(Long.valueOf(application.getQuickLead().getLoanAmountRequested())));
			loanInformation.setLoanAmountRequested(amountField);

			/*-------------------------dummy-------------------------*/
			loanInformation.setProduct("");
			loanInformation.setCityInwhichPropertyIsBased("");
			loanInformation.setExistingNumberOftrucksOrBuses("");
			loanInformation.setNumberOfVehiclesFreeFromFinance("");
			loanInformation.setIndicativePrice("");
			loanInformation.setCostOfHome(new AmountField());
			loanInformation.setCostOfContsruction(new AmountField());
			loanInformation.setCostOfLand(new AmountField());

			leadCreationRequest.setLoanInformation(loanInformation);

			/*---------------------------------------------------communicationDetails---------------------------------------------------- */
			CommunicationDetails communicationDetails = new CommunicationDetails();
			communicationDetails.setModeOfCommunication(application.getQuickLead().getPreferredModeOfCommunication());
			communicationDetails.setLeadStatus(getDataF1Service.getLeadStatus(application.getQuickLead().getLeadStatus()));
			communicationDetails.setAddComment(application.getQuickLead().getCommunicationTranscript());
			communicationDetails.setContactedBy("System");
			cal.setTime(new Date());
			communicationDetails.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));

			/*-------------------------dummy-------------------------*/
			communicationDetails.setPhoneNumber("");
			communicationDetails.setNotificationType("");

			leadCreationRequest.setCommunicationDetails(communicationDetails);

			/*---------------------------------------------------MoveToNextStageFlag---------------------------------------------------- */
			leadCreationRequest.setMoveToNextStageFlag(true);

			/*---------------------------------------------------UserName---------------------------------------------------- */
			leadCreationRequest.setUserName("System");

			/*---------------------------------------------------Branchcode---------------------------------------------------- */
			leadCreationRequest.setBranchcode(getDataF1Service.getBranchCode(application.getQuickLead().getSourcingBranch()));

			/*-------------------------dummy-------------------------*/
			leadCreationRequest.setCardType("");
			leadCreationRequest.setLogo("");
			leadCreationRequest.setNetworkGateway("");
			leadCreationRequest.setPromoCode("");
			leadCreationRequest.setRelationship("");

			app = mapper.convertValue(leadCreationRequest, ObjectNode.class);
		}catch (Exception e){
			log.info("{}", e.toString());
		}
		return app;
	}

}
