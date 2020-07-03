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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.models.Application;
import vn.com.tpf.microservices.models.Finnone.*;
import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;
import vn.com.tpf.microservices.models.Identification;
import vn.com.tpf.microservices.models.leadCreation.*;
import vn.com.tpf.microservices.models.leadCreation.AttachmentDetails;
import vn.com.tpf.microservices.models.leadCreation.CommunicationDetails;
import vn.com.tpf.microservices.models.leadCreation.Documents;
import vn.com.tpf.microservices.models.leadCreation.PersonInfo;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConvertService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private GetDataF1Service getDataF1Service;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${spring.url.uploadfile}")
	private String urlUploadfile;

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

	public JsonNode toApiF1(Application application) {
		JsonNode app = mapper.createObjectNode();
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
			personInfo.setMobilePhoneNumber("123456789");
			personInfo.setMobilePhoneIsdCode("84");
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

				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
				HttpEntity<String> entity = new HttpEntity<>(headers);

				ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + "/" + qlDocument.getFilename(), HttpMethod.GET, entity, byte[].class);

				attachmentDetails.setAttachedDocument(response.getBody());

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

			amountField.setValue(new BigDecimal(application.getQuickLead().getLoanAmountRequested().replace(",", "")));
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
			communicationDetails.setModeOfCommunication("PHONE");
			communicationDetails.setLeadStatus(getDataF1Service.getLeadStatus(application.getQuickLead().getLeadStatus()));
			communicationDetails.setAddComment(application.getQuickLead().getCommunicationTranscript());
			communicationDetails.setContactedBy("System");
			cal.setTime(new Date());
			communicationDetails.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));

			/*-------------------------dummy-------------------------*/
			communicationDetails.setPhoneNumber("others");
			communicationDetails.setNotificationType("TP_SMS");

			leadCreationRequest.setCommunicationDetails(communicationDetails);

			/*---------------------------------------------------MoveToNextStageFlag---------------------------------------------------- */
			leadCreationRequest.setMoveToNextStageFlag(true);

			/*---------------------------------------------------Branchcode---------------------------------------------------- */
			leadCreationRequest.setBranchcode(getDataF1Service.getBranchCode(application.getQuickLead().getSourcingBranch()));

			/*-------------------------dummy-------------------------*/
			leadCreationRequest.setCardType("");
			leadCreationRequest.setLogo("");
			leadCreationRequest.setNetworkGateway("");
			leadCreationRequest.setPromoCode("");
			leadCreationRequest.setRelationship("");

			app = mapper.convertValue(leadCreationRequest, JsonNode.class);
		}catch (Exception e){
			log.info("{}", e.toString());
		}
		return app;
	}

	public JsonNode toAppApiF1(Application application){
		JsonNode app = mapper.createObjectNode();
		try {
			UpdateApplicationRequest updateApplicationRequest = new UpdateApplicationRequest();

			/*-------------------------------------------appParameter-------------------------------------------*/
			AppParameter appParameter = new AppParameter();
			appParameter.setProductProcessor("mCAS");
			appParameter.setApplicationNumber(application.getApplicationId());
			appParameter.setBranchCode("");
			appParameter.setUserName("System (TBD)");
			appParameter.setMoveToNextStageFlag(true);

			/*-------------------------------------------customerDet-------------------------------------------*/
			CustomerDet customerDet = new CustomerDet();
			/*-------------------------------------------CUSTOMER PARAMETER-------------------------------------------*/
			CustomerParameter customerParameter = new CustomerParameter();
			customerParameter.setPartyRole(BigInteger.ONE);
			customerParameter.setCustomerType("");
			customerParameter.setCustomerNumber(application.getCustomerId());
			customerDet.setCustomerParameter(customerParameter);

			/*-------------------------------------------PERSONAL INFOR-------------------------------------------*/
			vn.com.tpf.microservices.models.Finnone.PersonInfo personInfo = convertPersonInfo(application);
			customerDet.setPersonInfo(personInfo);
			/*-------------------------------------------IDENTIFICATIONS-------------------------------------------*/
			List<UpdateIdentificationDetails> updateIdentificationDetailsList = convertUpdateIdentificationDetails(application);
			customerDet.getUpdateIdentificationDetails().addAll(updateIdentificationDetailsList);
			/*-------------------------------------------ADDRESSES-------------------------------------------*/
			List<UpdateAddressDetails> updateAddressDetailsList = convertUpdateAddressDetails(application);
			customerDet.getUpdateAddressDetails().addAll(updateAddressDetailsList);
			/*-------------------------------------------COMMUNICATION DETAILS-------------------------------------------*/
			vn.com.tpf.microservices.models.Finnone.CommunicationDetails communicationDetails = new vn.com.tpf.microservices.models.Finnone.CommunicationDetails();
			communicationDetails.setPrimaryEmailId(application.getApplicationInformation().getPersonalInformation().getCommunicationDetails().getPrimaryEmailId());
			communicationDetails.setPhoneNumber(application.getApplicationInformation().getPersonalInformation().getCommunicationDetails().getPhoneNumbers()
					.stream().map(this::convertPhoneNumber).collect(Collectors.toList()));
			customerDet.setCommunicationDetails(communicationDetails);
			/*-------------------------------------------FAMILY-------------------------------------------*/
			UpdateFamily updateFamily = convertUpdateFamily(application);
			customerDet.setUpdateFamily(updateFamily);
			/*-------------------------------------------EMPLOYMENT DETAILS-------------------------------------------*/
			List<UpdateOccupationInfo> updateOccupationInfoList = convertUpdateOccupationInfo(application);
			customerDet.getUpdateOccupationInfo().addAll(updateOccupationInfoList);

			customerDet.setYearsInTotalOccupation(Integer.valueOf(application.getApplicationInformation().getEmploymentDetails().getTotalYearsInOccupation()));
			customerDet.setMonthsInTotalOccupation(Integer.valueOf(application.getApplicationInformation().getEmploymentDetails().getTotalMonthsInOccupation()));

			/*-------------------------------------------FINANCIAL DETAILS-------------------------------------------*/
			UpdateFinancialDetails updateFinancialDetails = convertUpdateFinancialDetails(application);
			customerDet.setFinancialDetails(updateFinancialDetails);
			/*-------------------------------------------LOAN DETAILS-------------------------------------------*/
			UpdateLoanDetails updateLoanDetails = covertUpdateLoanDetails(application);

			/*-------------------------------------------REFERENCES-------------------------------------------*/
			List<ReferenceDetails> referenceDetails = application.getReferences().stream().map(reference -> {
				ReferenceDetails referenceDetails1 = new ReferenceDetails();
				referenceDetails1.setName(reference.getName());
				referenceDetails1.setRelationship(reference.getRelationship());
				referenceDetails1.setPhoneNumber(reference.getPhoneNumbers().stream().map(this::convertPhoneNumber).collect(Collectors.toList()));
				return referenceDetails1;
			}).collect(Collectors.toList());

			/*-------------------------------------------DYNAMIC FORMS-------------------------------------------*/
			UpdateDynamicFormDetails dynamicFormDetails = new UpdateDynamicFormDetails();
			dynamicFormDetails.setDynamicFormName(application.getDynamicForm().get(0).getFormName());

			ObjectNode objectNode = mapper.convertValue(application.getDynamicForm().get(0), ObjectNode.class);
			objectNode.remove("formName");
			objectNode.put("houseOwnership", getDataF1Service.getHouseOwnership(application.getDynamicForm().get(0).getHouseOwnership()));
			dynamicFormDetails.setDynamicFormData(objectNode.toString());

			/*-------------------------------------------DOCUMENTS-------------------------------------------*/
			List<UpdateDocuments> updateDocuments = covertDocuments(application);

			updateApplicationRequest.setAppParameter(appParameter);
			updateApplicationRequest.getCustomerDet().add(customerDet);
			updateApplicationRequest.setUpdateLoanDetails(updateLoanDetails);
			updateApplicationRequest.getReferenceDetails().addAll(referenceDetails);
			updateApplicationRequest.getDocuments().addAll(updateDocuments);
			updateApplicationRequest.getDynamicFormDetails().add(dynamicFormDetails);
			app = mapper.convertValue(updateApplicationRequest, JsonNode.class);
		}catch (Exception e){
			log.info("{}", e.toString());
		}
		return app;
	}

	private List<UpdateDocuments> covertDocuments(Application application) {
		if (application.getDocuments() == null || application.getDocuments().size() == 0){
			return new ArrayList<>();
		}
		return application.getDocuments()
				.stream().map(qlDocument -> {
					UpdateDocuments documents = new UpdateDocuments();
					documents.setDocumentReferenceId(Long.valueOf(getDataF1Service.getDocumentReferenceId(application.getApplicationId())));
					documents.setReferenceType("Customer");
					String entityType = application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getFirstName().toUpperCase();
					if(StringUtils.hasLength(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getMiddleName()))
						entityType += " " + application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getMiddleName().toUpperCase();
					entityType += " " + application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getLastName().toUpperCase();
					documents.setEntityType(entityType);
					documents.setDocumentName(qlDocument.getType());
					documents.setVerificationStatus("verified");
					documents.setPhysicalState("photocopy");
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(new Date());
					try {
						documents.setRecievingDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
					} catch (DatatypeConfigurationException e) {
						log.info("{}", e.toString());
					}

					documents.setRemarks("Document received");

					HttpHeaders headers = new HttpHeaders();
					headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
					HttpEntity<String> entity = new HttpEntity<>(headers);


					vn.com.tpf.microservices.models.Finnone.AttachmentDetails attachmentDetails = new vn.com.tpf.microservices.models.Finnone.AttachmentDetails();
					attachmentDetails.setAttachedDocName(qlDocument.getOriginalname());
					ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + qlDocument.getFilename(), HttpMethod.GET, entity, byte[].class);

					attachmentDetails.setAttachedDocument(response.getBody());
					documents.getAttachmentDetails().add(attachmentDetails);
					return documents;
				}).collect(Collectors.toList());
	}

	private UpdateLoanDetails covertUpdateLoanDetails(Application application) {
		/*-------------------------------------------SOURCING DETAILS-------------------------------------------*/
		UpdateSourcingDetail sourcingDetail = new UpdateSourcingDetail();
		LoanInfo loanInfo = new LoanInfo();
		loanInfo.setProductCode(getDataF1Service.getLoanProduct(application.getLoanDetails().getSourcingDetails().getSchemeCode()));
		loanInfo.setSchemeCode(getDataF1Service.getScheme(application.getLoanDetails().getSourcingDetails().getSchemeCode()));
		MoneyType amountRequested = new MoneyType();
		amountRequested.setCurrencyCode("VND");
		amountRequested.setValue(new BigDecimal(application.getLoanDetails().getSourcingDetails().getLoanAmountRequested().replace(",", "")));
		loanInfo.setLoanAmountRequested(amountRequested);
		loanInfo.setRequestedTenure(Integer.valueOf(application.getLoanDetails().getSourcingDetails().getRequestedTenure()));
		loanInfo.setLoanPurpose("OTHERS");
		loanInfo.setLoanPurposeDesc(application.getLoanDetails().getSourcingDetails().getLoanPurposeDesc());
		loanInfo.setChassisApplicationNum(application.getLoanDetails().getSourcingDetails().getChassisApplicationNum());
		sourcingDetail.setLoanInfo(loanInfo);

		ApplicationDetails applicationDetails = new ApplicationDetails();
		applicationDetails.setLoanApplicationType(getDataF1Service.getLoanApplicationType(application.getLoanDetails().getSourcingDetails().getLoanApplicationType()));
		applicationDetails.setOfficer(getDataF1Service.getOfficer(application.getLoanDetails().getSourcingDetails().getSaleAgentCode()));
		applicationDetails.setSourcingChannel("");
		sourcingDetail.setApplicationDetails(applicationDetails);
		/*-------------------------------------------VAP DETAILS-------------------------------------------*/
		UpdateVapDetail updateVapDetail = new UpdateVapDetail();
		String vapProductApi = getDataF1Service.getVapProduct(application.getLoanDetails().getVapDetails().getVapProduct());
		updateVapDetail.setVapProduct(vapProductApi);
		updateVapDetail.setVapTreatment(getDataF1Service.getVapTreatment(application.getLoanDetails().getVapDetails().getVapProduct()));
		updateVapDetail.setInsuranceCompany(getDataF1Service.getInsuranceCompany(application.getLoanDetails().getVapDetails().getVapProduct()));
		Map<String, Object> amtPayOut = getDataF1Service.getAmtCompPolicy(application.getLoanDetails().getVapDetails().getVapProduct());
		updateVapDetail.setAmtCompPolicy(amtPayOut.get("CODE_AMT_COMP").toString());
		updateVapDetail.setPayOutCompPolicy(amtPayOut.get("CODE_PAY_OUT").toString());
		MoneyType amount = new MoneyType();
		amount.setCurrencyCode("VND");
		amount.setValue(BigDecimal.ZERO);
		updateVapDetail.setCoverageAmount(amount);
		updateVapDetail.setPremiumAmount(amount);
		UpdateLoanDetails updateLoanDetails = new UpdateLoanDetails();
		updateLoanDetails.setSourcingDetail(sourcingDetail);
		updateLoanDetails.getVapDetail().add(updateVapDetail);
		return updateLoanDetails;
	}

	private UpdateFinancialDetails convertUpdateFinancialDetails(Application application) {
		UpdateFinancialDetails updateFinancialDetails = new UpdateFinancialDetails();
		application.getApplicationInformation().getFinancialDetails().forEach(financialDetail -> {
			UpdateIncomeDetails incomeDetail = new UpdateIncomeDetails();
			incomeDetail.setIncomeExpense(getDataF1Service.getIncomeExpense(financialDetail.getIncomeExpense()));
			incomeDetail.setFrequency("MONTHLY");
			MoneyType amount = new MoneyType();
			amount.setCurrencyCode("VND");
			amount.setValue(new BigDecimal(financialDetail.getAmount().replace(",", "")));
			incomeDetail.setAmount(amount);
			incomeDetail.setPaymentMode(getDataF1Service.getPaymentMode(financialDetail.getModeOfPayment()));
			incomeDetail.setIncomeSource(financialDetail.getDayOfSalaryPayment());
			incomeDetail.setPercentage(BigDecimal.valueOf(100));
			updateFinancialDetails.getIncomeDetails().add(incomeDetail);
		});
		return updateFinancialDetails;
	}

	private UpdateFamily convertUpdateFamily(Application application) {
		UpdateFamily updateFamily = new UpdateFamily();
		if(application.getApplicationInformation().getPersonalInformation().getFamily().size() > 0){
			updateFamily.setNoOfDependents(0);
			updateFamily.setNoOfDependentChildren(0);
			UpdateFamilyDetails updateFamilyDetails = new UpdateFamilyDetails();
			application.getApplicationInformation().getPersonalInformation().getFamily().forEach(family -> {
				FamilyDetails familyDetails = new FamilyDetails();
				familyDetails.setMemberName(family.getMemberName());
				familyDetails.setRelationship(family.getRelationship());
				familyDetails.setPhoneNumber(family.getPhoneNumber());
				familyDetails.setIsDependent(0);
				familyDetails.setEducationStatus("Uneducated");
				updateFamilyDetails.setFamilyDetails(familyDetails);
			});
			updateFamily.getUpdateFamilyDetails().add(updateFamilyDetails);
		}
		return updateFamily;
	}

	private List<UpdateOccupationInfo> convertUpdateOccupationInfo(Application application) {
		List<UpdateOccupationInfo> updateOccupationInfoList = new ArrayList<>();
		UpdateOccupationInfo updateOccupationInfo = new UpdateOccupationInfo();
		OccupationInfo occupationInfo = new OccupationInfo();
		occupationInfo.setOccupationType(getDataF1Service.getOccupationType(application.getApplicationInformation().getEmploymentDetails().getOccupationType()));
		occupationInfo.setEmployerCode(application.getApplicationInformation().getEmploymentDetails().getEmployerCode());
		occupationInfo.setNatureOfBusiness(getDataF1Service.getNatureOfBusiness(application.getApplicationInformation().getEmploymentDetails().getNatureOfBusiness()));
		occupationInfo.setRemarks(application.getApplicationInformation().getEmploymentDetails().getRemarks());
		occupationInfo.setEmployeeNumber(application.getApplicationInformation().getEmploymentDetails().getOtherCompanyTaxCode());
		occupationInfo.setIndustry(getDataF1Service.getIndustry(application.getApplicationInformation().getEmploymentDetails().getIndustry()));
		occupationInfo.setEmploymentType(getDataF1Service.getEmploymentType(application.getApplicationInformation().getEmploymentDetails().getEmploymentType()));
		occupationInfo.setEmploymentStatus(getDataF1Service.getEmploymentStatus(application.getApplicationInformation().getEmploymentDetails().getEmploymentStatus()));
		occupationInfo.setDepartmentName(application.getApplicationInformation().getEmploymentDetails().getDepartmentName());
		occupationInfo.setDesignation(application.getApplicationInformation().getEmploymentDetails().getDesignation());
		occupationInfo.setYearsInJob(Integer.valueOf(application.getApplicationInformation().getEmploymentDetails().getYearsInJob()));
		occupationInfo.setMonthsInJob(Integer.valueOf(application.getApplicationInformation().getEmploymentDetails().getMonthsInJob()));
		occupationInfo.setIsMajorEmployment(1);
		occupationInfo.setNatureOfOccupation(getDataF1Service.getNatureOfOccupation(application.getApplicationInformation().getEmploymentDetails().getNatureOfOccupation()));
		updateOccupationInfo.setOccupationInfo(occupationInfo);
		updateOccupationInfoList.add(updateOccupationInfo);
		return updateOccupationInfoList;
	}

	private PhoneNumber convertPhoneNumber(vn.com.tpf.microservices.models.PhoneNumber phoneNumber) {
		PhoneNumber phoneNumber1 = new PhoneNumber();
		phoneNumber1.setPhoneType(phoneNumber.getPhoneType());
		phoneNumber1.setExtension(phoneNumber.getExtension());
		phoneNumber1.setIsdCode(phoneNumber.getIsdCode());
		phoneNumber1.setPhoneNumber(phoneNumber.getPhoneNumber());
		phoneNumber1.setCountryCode(phoneNumber.getCountryCode());
		phoneNumber1.setStdCode(phoneNumber.getStdCode());
		return phoneNumber1;
	}

	private List<UpdateAddressDetails> convertUpdateAddressDetails(Application application) {
		List<UpdateAddressDetails> updateAddressDetailsList = application.getApplicationInformation().getPersonalInformation().getAddresses()
				.stream()
				.map(address -> {
					UpdateAddressDetails updateAddressDetails = new UpdateAddressDetails();
					updateAddressDetails.setDeleteFlag(false);
					AddressDetails addressDetails = new AddressDetails();
					addressDetails.setAddressType(getDataF1Service.getAddressType(address.getAddressType()));
					addressDetails.setCountry(getDataF1Service.getCountry(address.getCountry()));
					Map<String, Object> cityMap = getDataF1Service.getState_City_Zip(address.getCity());
					addressDetails.setState(cityMap.get("STATE").toString());
					addressDetails.setCity(cityMap.get("CITY").toString());
					addressDetails.setZipcode(cityMap.get("ZIP").toString());
					addressDetails.setArea(getDataF1Service.getArea(address.getArea(), cityMap.get("CITY").toString()));
					addressDetails.setLandMark(address.getLandMark());
					addressDetails.setAddressLine1(address.getAddressLine1());
					addressDetails.setAddressLine2(address.getAddressLine2());
					addressDetails.setAddressLine3(address.getAddressLine3());
					addressDetails.setYearsInCurrentAddress(Integer.valueOf(address.getYearsInCurrentAddress()));
					addressDetails.setMonthsInCurrentAddress(Integer.valueOf(address.getMonthsInCurrentAddress()));
					if(application.getApplicationInformation().getPersonalInformation().getCommunicationDetails().getPrimaryAddress().toUpperCase().equals(address.getAddressType().toUpperCase())){
						addressDetails.setPrimaryAddress(1);
					}
					addressDetails.setPhoneNumber(address.getPhoneNumbers().stream().map(this::convertPhoneNumber).collect(Collectors.toList()));
					addressDetails.setAccomodationType("");
					updateAddressDetails.setAddressDetails(addressDetails);
					return updateAddressDetails;
				}).collect(Collectors.toList());

		return updateAddressDetailsList;
	}

	private vn.com.tpf.microservices.models.Finnone.PersonInfo convertPersonInfo(Application application) {
		vn.com.tpf.microservices.models.Finnone.PersonInfo personInfo = new vn.com.tpf.microservices.models.Finnone.PersonInfo();
		personInfo.setGender(getDataF1Service.getGender(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getGender()));
		personInfo.setFirstName(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getFirstName().toUpperCase());
		if(StringUtils.hasLength(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getMiddleName())){
			personInfo.setMiddleName(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getMiddleName().toUpperCase());
		}
		personInfo.setLastName(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getLastName().toUpperCase());

		GregorianCalendar cal = new GregorianCalendar();
		try{
			Date dateOfBirth = DateUtils.parseDate(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getDateOfBirth(), new String[]{"dd/MM/yyyy"});
			cal.setTime(dateOfBirth);
			personInfo.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
		} catch (ParseException | DatatypeConfigurationException e) {
			log.info("{}", e.toString());
		}

		personInfo.setMaritalStatus(getDataF1Service.getMaritalStatus(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getMaritalStatus()));

		personInfo.setPlaceOfBirth(application.getApplicationInformation().getPersonalInformation().getIdentifications()
				.stream()
				.filter(identification -> identification.getIdentificationType().trim().toUpperCase().equals( "Current National ID".toUpperCase()))
				.map(Identification::getPlaceOfIssue).collect(Collectors.joining()));

		personInfo.setNationality(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getNationality());
		personInfo.setCustomerCategoryCode(getDataF1Service.getCustomerCategoryCode(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getCustomerCategoryCode()));
		personInfo.setSalutation(getDataF1Service.getSalutation());
		personInfo.setResidentType(getDataF1Service.getResidentType());
		personInfo.setConstitutionCode(getDataF1Service.getConstitutionCode());
		personInfo.setMothersMaidenName("dummy");

		return personInfo;
	}

	private List<UpdateIdentificationDetails> convertUpdateIdentificationDetails(Application application) {
		List<UpdateIdentificationDetails> updateIdentificationDetailsList = application.getApplicationInformation().getPersonalInformation().getIdentifications()
				.stream()
				.map(identification -> {
			UpdateIdentificationDetails updateIdentificationDetails = new UpdateIdentificationDetails();
			updateIdentificationDetails.setDeleteFlag(false);
			IdentificationDetails identificationDetails = new IdentificationDetails();
			identificationDetails.setIdentificationType(getDataF1Service.getIdentificationType(identification.getIdentificationType()));
			identificationDetails.setIdentificationNumber(identification.getIdentificationNumber());
			GregorianCalendar cal = new GregorianCalendar();
			if(StringUtils.hasLength(identification.getIssueDate())){
				try {
					Date issueDate = DateUtils.parseDate(identification.getIssueDate(), new String[]{"dd/MM/yyyy"});
					cal.setTime(issueDate);
					identificationDetails.setIssueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
				} catch (ParseException | DatatypeConfigurationException e) {
					log.info("{}", e.toString());
				}
			}
			if(StringUtils.hasLength(identification.getExpiryDate())){
				try {
					Date expiryDate = DateUtils.parseDate(identification.getExpiryDate(), new String[]{"dd/MM/yyyy"});
					cal.setTime(expiryDate);
					identificationDetails.setExpiryDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
				} catch (ParseException | DatatypeConfigurationException e) {
					log.info("{}", e.toString());
				}
			}
			identificationDetails.setIssuingCountry(getDataF1Service.getCountry(identification.getIssuingCountry()));
			updateIdentificationDetails.setIdentificationDetails(identificationDetails);
			return updateIdentificationDetails;
		}).collect(Collectors.toList());

		return  updateIdentificationDetailsList;
	}
}
