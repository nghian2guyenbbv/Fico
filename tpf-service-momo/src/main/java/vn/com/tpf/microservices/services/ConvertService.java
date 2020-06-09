package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.models.Automation.*;
import vn.com.tpf.microservices.models.Finnone.*;
import vn.com.tpf.microservices.models.Finnone.CommunicationDetails;
import vn.com.tpf.microservices.models.Finnone.Family;
import vn.com.tpf.microservices.models.Finnone.LoanDetails;
import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;
import vn.com.tpf.microservices.models.Momo;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ConvertService {

	@Autowired
	private RestTemplate restTemplateDownload;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ObjectNode toAppDisplay(Momo momo) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "momo");
		app.put("uuid", momo.getId());
		app.put("appId", momo.getAppId());
		app.put("partnerId", momo.getMomoLoanId());
		app.put("status", momo.getStatus());
		app.put("automationResult", momo.getAutomationResult());
		app.put("fullName",
				(momo.getLastName() + " " + momo.getMiddleName() + " " + momo.getFirstName()).replaceAll("\\s+", " "));
		ArrayNode documents = mapper.createArrayNode();
		momo.getPhotos().forEach(e -> {
			ObjectNode doc = mapper.createObjectNode();
			doc.put("documentType", e.getType());
			doc.put("viewUrl", e.getLink());
			doc.put("downloadUrl", e.getLink());
			doc.set("updatedAt", mapper.convertValue(e.getUpdatedAt(), JsonNode.class));
			documents.add(doc);
		});
		app.set("documents", documents);
		ObjectNode optional = mapper.createObjectNode();
		optional.put("smsResult", momo.getSmsResult());
		optional.put("error", momo.getError());
		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppFinnone(Momo momo) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "momo");
		
		
		ObjectNode spouse = mapper.createObjectNode();
		ArrayNode references = mapper.createArrayNode();
		ObjectNode applicationInformation = mapper.createObjectNode();
		ObjectNode personalInformation = mapper.createObjectNode();
		ArrayNode identifications = mapper.createArrayNode();
		identifications.add(mapper.createObjectNode().put("identificationType", "Current National ID")
				.put("issuingCountry", "Vietnam").put("identificationNumber", momo.getPersonalId())
				.put("placeOfBirth", momo.getIssuePlace()).put("issueDate", momo.getIssueDate()));
		momo.getReferences().forEach(e -> {
			if (e.getRelation().equals("Spouse")) {
				spouse.put("memberName", e.getFullName());
				spouse.put("phoneNumber", e.getPhoneNumber());
				spouse.put("relationship", e.getRelation());
				spouse.put("personalId", e.getPersonalId());
				personalInformation.set("family", mapper.createArrayNode().add(spouse));
				identifications.add(mapper.createObjectNode().put("identificationType", "Spouse Current National ID")
						.put("issuingCountry", "Vietnam").set("identificationNumber", spouse.path("personalId")));
			} else {
				ObjectNode ref = mapper.createObjectNode();
				ref.put("name", e.getFullName());
				ref.put("relationship", e.getRelation());
				ref.set("phoneNumbers", mapper.createArrayNode()
						.add(mapper.createObjectNode().put("phoneType", "Mobile Phone").put("phoneNumber", e.getPhoneNumber().replaceAll("^[0]", ""))));
				references.add(ref);
			}
		});
		app.set("references", references);

		ArrayNode documents = mapper.createArrayNode();
		momo.getPhotos().forEach(e -> {
			documents.add(mapper.createObjectNode().put("type", e.getType()).put("link", e.getLink()));
		});
		app.set("documents", documents);


		personalInformation.set("personalInfo",
				mapper.createObjectNode().put("firstName", momo.getFirstName()).put("middleName", momo.getMiddleName())
						.put("lastName", momo.getLastName())
						.put("fullName",
								(momo.getFirstName() + " " + momo.getMiddleName() + " " + momo.getLastName()).replaceAll("\\s+", " "))
						.put("gender", momo.getGender()).put("dateOfBirth", momo.getDateOfBirth()).put("nationality", "Vietnamese")
						.put("maritalStatus", momo.getMaritalStatus())
						.put("issuePlace", momo.getIssuePlace()));
	
	  personalInformation.set("identifications",identifications);
	  
		personalInformation.set("addresses", mapper.createArrayNode().add(mapper.createObjectNode()
				.put("addressType", "Current Address").put("country", "Vietnam").put("state", momo.getRegion())
				.put("city", momo.getCity()).put("zipcode", "70000").put("area", momo.getDistrict())
				.put("addressLine1", momo.getAddress1()).put("addressLine2", momo.getAddress2())
				.put("addressLine3", momo.getWard()).set("phoneNumbers", mapper.createArrayNode().add(
						mapper.createObjectNode().put("phoneType", "Mobile Phone").put("phoneNumber", momo.getPhoneNumber())))));
		personalInformation.set("communicationDetails",
				mapper.createObjectNode().put("primaryAddress", "Current Address").put("primaryEmailId", momo.getEmail())
						.set("phoneNumbers", mapper.createArrayNode().add(
								mapper.createObjectNode().put("phoneType", "Mobile Phone").put("phoneNumber", momo.getPhoneNumber()))));
	
		applicationInformation.set("personalInformation", personalInformation);
		applicationInformation.set("financialDetails", mapper.createArrayNode()
				.add(mapper.createObjectNode().put("incomeExpense", "Main Personal Income").put("amount", momo.getSalary())));
		app.set("applicationInformation", applicationInformation);

		ObjectNode loanDetails = mapper.createObjectNode();
		loanDetails.set("sourcingDetails",
				mapper.createObjectNode().put("productCode", momo.getProductCode()).put("loanAmountRequested", momo.getAmount())
						.put("requestedTenure", momo.getLoanTime()).put("loanApplicationType", "New Application")
						.put("chassisApplicationNum", momo.getMomoLoanId()).put("saleAgentCode", "OTHER VALUE"));
		if (momo.isInsurrance()) {
			loanDetails.set("vapDetails", mapper.createObjectNode().put("vapProduct", "INSP02_InsParameter")
					.put("vapTreatment", "Financed").put("insuranceCompany", "TPF_GIC-Global Insurance Company"));
		}
		app.set("loanDetails", loanDetails);

		return app;
	}

	public CreateApplicationRequest toFin1API(ObjectNode momoAppFinnone) throws JsonProcessingException, ParseException, DatatypeConfigurationException {
		CreateApplicationRequest createApplicationRequest=new CreateApplicationRequest();

		Application application = mapper.treeToValue(momoAppFinnone, Application.class);

		//dateTime parse
		GregorianCalendar cal = new GregorianCalendar();

		String pattern = "dd/MM/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		//
		ApplicantInformation applicantInformation=new ApplicantInformation();

		//------------------ APPLICANTINFORMATION - PERSONAL INFO--------------------------
		PersonInfo personInfo=new PersonInfo();
		personInfo.setFirstName(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getFirstName().toUpperCase());
		personInfo.setLastName(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getLastName().toUpperCase());
		personInfo.setMiddleName(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getMiddleName().toUpperCase());
		personInfo.setGender(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getGender().toUpperCase());
		personInfo.setSalutation("MR");
		personInfo.setMothersMaidenName("dummy");
		personInfo.setConstitutionCode("1");
		personInfo.setResidentType(getDataF1_residentType());

		cal.setTime(simpleDateFormat.parse(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getDateOfBirth()));
		personInfo.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));

		personInfo.setPlaceOfBirth(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getIssuePlace());
		personInfo.setMaritalStatus(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getMaritalStatus());

		personInfo.setNationality("Vietnamese");
		personInfo.setCustomerCategoryCode("OTH");

		//------------------ END ----------------------------------

		//-------------------APPLICANTINFORMATION - IDENTIFICATION----------------------------
		List<IdentificationDetails> identificationDetailsList=new ArrayList<IdentificationDetails>();

		for (Identification identification : application.getApplicationInformation().getPersonalInformation().getIdentifications()) {
			//nationalID
			IdentificationDetails identificationDetails=new IdentificationDetails();
			if(identification.getIdentificationType().equals("Current National ID"))
			{

				identificationDetails.setIdentificationType("NATIONAL_ID");
				identificationDetails.setIdentificationNumber(identification.getIdentificationNumber());
				identificationDetails.setIssuingCountry(getDataF1_IssusingCountry());
				cal.setTime(simpleDateFormat.parse(identification.getIssueDate()));
				identificationDetails.setIssueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
			}else if(identification.getIdentificationType().equals("Spouse Current National ID"))
			{
				identificationDetails.setIdentificationType("SPOUSE_NATIONAL_ID");
				identificationDetails.setIdentificationNumber(identification.getIdentificationNumber());
				identificationDetails.setIssuingCountry(getDataF1_IssusingCountry());
			}
			identificationDetailsList.add(identificationDetails);
		}
		//-------------------END---------------------------------------

		//------------------- APPLICANTINFORMATION - ADDRESS-----------------------
		List<AddressDetails> addressDetailsList=new ArrayList<AddressDetails>();

		for (Address fa : application.getApplicationInformation().getPersonalInformation().getAddresses()) {
			AddressDetails addressDetails=new AddressDetails();
			addressDetails.setAddressType(getDataF1_AddressType(fa.getAddressType()));
			addressDetails.setCountry("VN"); //set default
			addressDetails.setAddressLine1(fa.getAddressLine1());
			addressDetails.setAddressLine2(fa.getAddressLine2());
			addressDetails.setAddressLine3(fa.getAddressLine3());
			addressDetails.setArea(getDataF1_AreaCode(fa.getArea()));
			addressDetails.setZipcode(getDataF1_ZipCode(fa.getCity()));
			addressDetails.setCity(getDataF1_CityCode(fa.getCity()));
			addressDetails.setPrimaryAddress(0);
			addressDetails.setAccomodationType("OTHERS");

			if(fa.getAddressType()!=null && fa.getAddressType().equals("Current Address")){
				addressDetails.setMonthsInCurrentAddress(1);
				addressDetails.setYearsInCurrentAddress(1);
				addressDetails.setLandMark(fa.getLandMark());
				List<PhoneNumber> phoneNumbersList=new ArrayList<PhoneNumber>();
				PhoneNumber phoneNumber=new PhoneNumber();
				phoneNumber.setPhoneType("Mobile");
				phoneNumber.setIsdCode("+84");
				phoneNumber.setPhoneNumber(fa.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().isPresent()?fa.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().get().getPhoneNumber().replace("+84", ""):"");
				phoneNumber.setCountryCode("VN");
				phoneNumbersList.add(phoneNumber);

				addressDetails.setPhoneNumber(phoneNumbersList);
				addressDetails.setPrimaryAddress(1);
			}

			addressDetailsList.add(addressDetails);
		}

		//address FB
		AddressDetails addressDetailsFB=new AddressDetails();
		addressDetailsFB.setAddressType(getDataF1_AddressType("Family Book Address"));
		addressDetailsFB.setCountry("VN"); //set default
		addressDetailsFB.setAddressLine1(addressDetailsList.get(0).getAddressLine1());
		addressDetailsFB.setAddressLine2(addressDetailsList.get(0).getAddressLine2());
		addressDetailsFB.setAddressLine3(addressDetailsList.get(0).getAddressLine3());
		addressDetailsFB.setArea(addressDetailsList.get(0).getArea());
		addressDetailsFB.setZipcode(addressDetailsList.get(0).getZipcode());
		addressDetailsFB.setCity(addressDetailsList.get(0).getCity());
		addressDetailsFB.setPrimaryAddress(0);
		addressDetailsFB.setAccomodationType("OTHERS");
		addressDetailsList.add(addressDetailsFB);

		//address WK
		AddressDetails addressDetailsWK=new AddressDetails();
		addressDetailsWK.setAddressType(getDataF1_AddressType("Working Address"));
		addressDetailsWK.setCountry("VN"); //set default
		addressDetailsWK.setAddressLine1(addressDetailsList.get(0).getAddressLine1());
		addressDetailsWK.setAddressLine2(addressDetailsList.get(0).getAddressLine2());
		addressDetailsWK.setAddressLine3(addressDetailsList.get(0).getAddressLine3());
		addressDetailsWK.setArea(addressDetailsList.get(0).getArea());
		addressDetailsWK.setZipcode(addressDetailsList.get(0).getZipcode());
		addressDetailsWK.setCity(addressDetailsList.get(0).getCity());
		addressDetailsWK.setPrimaryAddress(0);
		addressDetailsWK.setAccomodationType("OTHERS");
		addressDetailsList.add(addressDetailsWK);

		//address spouse
		if(application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getMaritalStatus().equals("Married"))
		{
			AddressDetails addressDetailsSP=new AddressDetails();
			addressDetailsSP.setAddressType(getDataF1_AddressType("Spouse Address"));
			addressDetailsSP.setCountry("VN"); //set default
			addressDetailsSP.setAddressLine1(addressDetailsList.get(0).getAddressLine1());
			addressDetailsSP.setAddressLine2(addressDetailsList.get(0).getAddressLine2());
			addressDetailsSP.setAddressLine3(addressDetailsList.get(0).getAddressLine3());
			addressDetailsSP.setArea(addressDetailsList.get(0).getArea());
			addressDetailsSP.setZipcode(addressDetailsList.get(0).getZipcode());
			addressDetailsSP.setCity(addressDetailsList.get(0).getCity());
			addressDetailsSP.setPrimaryAddress(0);
			addressDetailsSP.setAccomodationType("OTHERS");
			addressDetailsList.add(addressDetailsSP);
		}

		//--------------------END-------------------------------------------------

		//----------------communicationDetails----------------

		CommunicationDetails communicationDetails = new CommunicationDetails();

		List<PhoneNumber> phoneNumbersList=new ArrayList<PhoneNumber>();
		PhoneNumber phoneNumber=new PhoneNumber();
		phoneNumber.setPhoneType("Mobile");
		phoneNumber.setIsdCode("+84");
		if(application.getApplicationInformation().getPersonalInformation().getCommunicationDetails().getPhoneNumbers()!=null) {
			phoneNumber.setPhoneNumber(application.getApplicationInformation().getPersonalInformation().getCommunicationDetails().getPhoneNumbers().stream().
					filter(c -> c.getPhoneType().equals("Mobile Phone")).findAny().isPresent() ?
					application.getApplicationInformation().getPersonalInformation().getCommunicationDetails().getPhoneNumbers().stream()
							.filter(c -> c.getPhoneType().equals("Mobile Phone")).findAny().get().getPhoneNumber().replace("+84", "") : "");
		}
		phoneNumber.setCountryCode("VN");
		phoneNumbersList.add(phoneNumber);
		communicationDetails.setPhoneNumber(phoneNumbersList);
		communicationDetails.setPrimaryEmailId(application.getApplicationInformation().getPersonalInformation().getCommunicationDetails().getPrimaryEmailId());

		applicantInformation.setCommunicationDetails(communicationDetails);
		//--------------------END-------------------------------------------------


		//---------------- Family -------------------
		if(application.getApplicationInformation().getPersonalInformation().getFamily()!=null) {
			Family family = new Family();
			for (vn.com.tpf.microservices.models.Automation.Family fr : application.getApplicationInformation().getPersonalInformation().getFamily()) {
				FamilyDetails familyDetails = new FamilyDetails();
				familyDetails.setMemberName(fr.getMemberName());
				familyDetails.setRelationship(fr.getRelationship());
				familyDetails.setPhoneNumber(fr.getPhoneNumber());
				familyDetails.setEducationStatus("Uneducated");

				List<FamilyDetails> familyDetailsList = new ArrayList<FamilyDetails>();
				familyDetailsList.add(familyDetails);
				family.setFamilyDetails(familyDetailsList);
			}

			applicantInformation.setFamily(family);
		}
		//------------------------ END --------------------------------------------

		//---------------------- APPLICANTINFORMATION - FINANCE ------------------
		FinancialDetails financialDetails=new FinancialDetails();
		List<IncomeDetails> incomeDetailsList=new ArrayList<IncomeDetails>();
		IncomeDetails incomeDetails=new IncomeDetails();
		MoneyType salary=new MoneyType();
		salary.setValue(BigDecimal.valueOf(Long.parseLong(application.getApplicationInformation().getFinancialDetails().get(0).getAmount())));
		salary.setCurrencyCode("VND"); //set default
		incomeDetails.setAmount(salary);
		incomeDetails.setFrequency("MONTHLY"); //set default
		incomeDetails.setPercentage(BigDecimal.valueOf(100)); //set default;
		incomeDetails.setIncomeExpense(getDataF1_IncomeExpense()); //set default
		incomeDetailsList.add(incomeDetails);
		financialDetails.setIncomeDetails(incomeDetailsList);
		//--------------------- END -----------------------------------------------


		//-------------------- APPLICANTINFORMATION - OCCUPATION -----------------
		List<OccupationInfo> occupationInfoList=new ArrayList<OccupationInfo>();
		OccupationInfo occupationInfo=new OccupationInfo();
		occupationInfo.setOccupationType("others");
		occupationInfo.setNatureOfOccupation("Unemployed");
		occupationInfo.setMonthsInJob(1); //set default
		occupationInfo.setYearsInJob(1); //set default
		occupationInfo.setIsMajorEmployment(1); //set default
		occupationInfoList.add(occupationInfo);

		//--------------------- END ----------------------------------------------

		applicantInformation.setCustomerType("individual");// set default
		applicantInformation.setPersonInfo(personInfo);
		applicantInformation.setIdentificationDetails(identificationDetailsList);
		applicantInformation.setAddressDetails(addressDetailsList);
		applicantInformation.setOccupationInfo(occupationInfoList);
		applicantInformation.setFinancialDetails(financialDetails);

		//--------------------- LOAN DETAIL ---------------------------------------
		LoanDetails loanDetails=new LoanDetails();
		SourcingDetail sourcingDetail=new SourcingDetail();

		ApplicationDetails applicationDetails=new ApplicationDetails();
		applicationDetails.setLoanApplicationType("NewApplication"); // set default
		applicationDetails.setSourcingChannel("MOMO"); // set default
		applicationDetails.setOfficer("other");
		applicationDetails.setApplicationFormNumber(application.getLoanDetails().getSourcingDetails().getChassisApplicationNum());

		LoanApplication loanApplication=new LoanApplication();
		loanApplication.setLoanBranch("MOMO"); //set default

		LoanInfo loanInfo=new LoanInfo();
		loanInfo.setSchemeCode(application.getLoanDetails().getSourcingDetails().getProductCode());
		MoneyType loanAmountRequest =new MoneyType();
		loanAmountRequest.setValue(BigDecimal.valueOf(Long.parseLong(application.getLoanDetails().getSourcingDetails().getLoanAmountRequested())));
		loanInfo.setLoanAmountRequested(loanAmountRequest);
		loanInfo.setRequestedTenure(Integer.parseInt(application.getLoanDetails().getSourcingDetails().getRequestedTenure()));
		loanInfo.setProductCode("DGL_CASH");
		loanInfo.setLoanPurpose("OTHERS");
		sourcingDetail.setLoanInfo(loanInfo);
		sourcingDetail.setApplicationDetails(applicationDetails);
		sourcingDetail.setLoanApplication(loanApplication);
		loanDetails.setSourcingDetail(sourcingDetail);
		//----------------------- END ----------------------------------------------

		//--------------------- VAP DETAIL ---------------------------------------
		List<VapDetail> vapDetailList=new ArrayList<VapDetail>();
		if(application.getLoanDetails().getVapDetails()!=null)
		{
			VapDetail vapDetail=new VapDetail();
			vapDetail.setVapProduct(getDataF1_VapDetail());
			vapDetail.setVapTreatment("FINANCED");
			vapDetail.setInsuranceCompany("TPF_GIC-Global Insurance Company");
			vapDetailList.add(vapDetail);

			loanDetails.setVapDetail(vapDetailList);
		}

		//----------------------- END --------------------------------------------


		//----------------------- REFERENCE ------------------------------------------
		List<ReferenceDetails> referenceDetailsList=new ArrayList<ReferenceDetails>();
		List<PhoneNumber> phoneNumbersListRef=new ArrayList<PhoneNumber>();
		for (Reference fr: application.getReferences()) {
				ReferenceDetails referenceDetails=new ReferenceDetails();
				referenceDetails.setName(fr.getName());
				referenceDetails.setRelationship(fr.getRelationship());

				PhoneNumber phoneNumberRef=new PhoneNumber();
				phoneNumberRef.setPhoneNumber(fr.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().isPresent()?fr.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().get().getPhoneNumber():"");
				phoneNumberRef.setPhoneType("Mobile");
				phoneNumberRef.setIsdCode("+84");
				phoneNumberRef.setCountryCode("VN");

				phoneNumbersListRef.add(phoneNumberRef);

				referenceDetails.setPhoneNumber(phoneNumbersList);
				referenceDetailsList.add(referenceDetails);
		}
		//----------------------- END --------------------------------------------

		//------------------------ DOCUMENT ----------------------------------------
		List<Documents> documentsList=new ArrayList<Documents>();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<>(headers);

		for (Document fp: application.getDocuments()) {
			Documents documents=new Documents();

			documents.setReferenceType("Customer");
			String fullname = application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getFirstName() +" "
					         + application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getMiddleName()+" "
					          + application.getApplicationInformation().getPersonalInformation().getPersonalInfo().getLastName();
			documents.setEntityType(fullname);
			documents.setVerificationStatus("verified");
			documents.setPhysicalState("photocopy");
			documents.setMandatory(true);
			documents.setRemarks("Document received");

			cal.setTime(new Date());
			documents.setRecievingDate(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED));
			List<AttachmentDetails> attachmentDetailsList=new ArrayList<AttachmentDetails>();
			AttachmentDetails attachmentDetails=new AttachmentDetails();
			String fileName=fp.getLink().substring(fp.getLink().lastIndexOf("/")+1);
			if(fp.getType().equals("Selfie"))
			{
				documents.setDocumentName("TPF_Customer Photograph");
				attachmentDetails.setAttachedDocName(fileName);
			}else if(fp.getType().equals("National ID Front"))
			{
				documents.setDocumentName("TPF_ID_Card_Front");
				attachmentDetails.setAttachedDocName(fileName);
			}else if(fp.getType().equals("National ID Back"))
			{
				documents.setDocumentName("TPF_ID_Card_Back");
				attachmentDetails.setAttachedDocName(fileName);
			}

			ResponseEntity<byte[]> response = restTemplateDownload.exchange(fp.getLink(), HttpMethod.GET, entity, byte[].class);
			attachmentDetails.setAttachedDocument(response.getBody());
			attachmentDetailsList.add(attachmentDetails);

			documents.setAttachmentDetails(attachmentDetailsList);
			documentsList.add(documents);
		}
		//------------------------ END  ----------------------------------------

		//-------------------------- DYNAMIC FORM --------------------------------------
		List<DynamicFormDetails> dynamicFormDetailsList=new ArrayList<DynamicFormDetails>();

		//dynamic form
		DynamicFormDetails dynamicFormDetailsMsc=new DynamicFormDetails();
		dynamicFormDetailsMsc.setDynamicFormName("frmAppDtl");

		ObjectNode objectNode1=mapper.createObjectNode();
		ObjectNode loanDetail=objectNode1.putObject("Loan_details_1");
		loanDetail.put("Other_loan_purpose_detail","vay MOMO");
		loanDetail.putArray("Loan_purpose_1").add(7);

		objectNode1.put("CardAgentCode",mapper.createObjectNode()
				.put("newbankcardnumber","2222222222222222")
				.put("salesagentcode","DS0001100")
		);

		objectNode1.put("OTH",mapper.createObjectNode()
				.put("Max_req_EIR","62")
		);

		objectNode1.put("familyinformation",mapper.createObjectNode()
				.put("householdmembers",0)
				.put("house_ownership","FOWM")
				.put("mortgage_payment_cost","VND~0.0000000")
		);

		dynamicFormDetailsMsc.setDynamicFormData(objectNode1.toString());

		dynamicFormDetailsList.add(dynamicFormDetailsMsc);
		//------------------------- END -----------------------------------

		List<ApplicantInformation> applicantInformationList=new ArrayList<ApplicantInformation>();
		applicantInformationList.add(applicantInformation);
		createApplicationRequest.setProductProcessor("mCAS");
		createApplicationRequest.setBranchCode("BAN");
		createApplicationRequest.setUserName("System");
		createApplicationRequest.setProductTypeCode(getDataF1_ProductTypeCode());
		createApplicationRequest.setApplicantInformation(applicantInformationList);
		createApplicationRequest.setLoanDetails(loanDetails);
		createApplicationRequest.setReferenceDetails(referenceDetailsList);
		createApplicationRequest.setDocuments(documentsList);
		createApplicationRequest.setDynamicFormDetails(dynamicFormDetailsList);
		createApplicationRequest.setMoveToNextStageFlag(true);


		return createApplicationRequest;
	}


	public String getDataF1_OccupationType()
	{
		// String sql=String.format("SELECT * from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter WHERE DTYPE='OccupationType' AND NAME ='%s'","Salaried");
		String sql="SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter WHERE DTYPE='OccupationType' AND NAME ='Salaried'";
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("CODE")!=null)
		{
			return resultData.get("CODE").toString();
		}

		return "";
	}

	public String getDataF1_EmployerCode()
	{
		String sql="SELECT EMPLOYER_CODE  FROM NEO_CM_GA25_GIR_SD.EMPLOYER x WHERE EMPLOYER_NAME ='Others' AND APPROVAL_STATUS =0";
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("EMPLOYER_CODE")!=null)
		{
			return resultData.get("EMPLOYER_CODE").toString();
		}

		return "";
	}

	public String getDataF1_ProductCode(String product)
	{
		String sql=String.format("SELECT  lp.PRODUCT_CODE, ls.SCHEME_NAME from NEO_CAS_LMS_GA25_GIR_SD.loan_PRODUCT lp,NEO_CAS_LMS_GA25_GIR_SD.loan_scheme ls \n" +
				"WHERE lp.ID = ls.LOAN_PRODUCT and lp.approval_status=0 and ls.approval_status=0 and ls.SCHEME_NAME ='%s'",product);
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("PRODUCT_CODE")!=null)
		{
			return resultData.get("PRODUCT_CODE").toString();
		}

		return "";
	}

	public String getDataF1_SchemeCode(String product)
	{
		String sql=String.format("SELECT SCHEME_CODE from NEO_CAS_LMS_GA25_GIR_SD.loan_scheme \n" +
				"WHERE SCHEME_NAME='%S' and APPROVAL_STATUS=0",product);
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("SCHEME_CODE")!=null)
		{
			return resultData.get("SCHEME_CODE").toString();
		}

		return "";
	}

	public String getDataF1_CityCode(String province)
	{
		String sql=String.format("SELECT a.CITY_code,b.STATE_code,c.ZIP_CODE , a.CITY_NAME\n" +
				"FROM NEO_CAS_LMS_GA25_GIR_SD.city a, NEO_CAS_LMS_GA25_GIR_SD.state b,NEO_CAS_LMS_GA25_GIR_SD.ZIP_CODE c \n" +
				"where a.state=b.id and a.id=c.city \n" +
				"and a.APPROVAL_STATUS=0 and b.APPROVAL_STATUS=0 and c.APPROVAL_STATUS=0 and Upper(a.CITY_NAME)='%s'",province);
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("CITY_CODE")!=null)
		{
			return resultData.get("CITY_CODE").toString();
		}

		return "";
	}

	public String getDataF1_ZipCode(String province)
	{
		String sql=String.format("SELECT a.CITY_code,b.STATE_code,c.ZIP_CODE , a.CITY_NAME\n" +
				"FROM NEO_CAS_LMS_GA25_GIR_SD.city a, NEO_CAS_LMS_GA25_GIR_SD.state b,NEO_CAS_LMS_GA25_GIR_SD.ZIP_CODE c \n" +
				"where a.state=b.id and a.id=c.city \n" +
				"and a.APPROVAL_STATUS=0 and b.APPROVAL_STATUS=0 and c.APPROVAL_STATUS=0 and Upper(a.CITY_NAME)='%s'",province);
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("ZIP_CODE")!=null)
		{
			return resultData.get("ZIP_CODE").toString();
		}

		return "";
	}

	public String getDataF1_AreaCode(String area)
	{
		//String sql=String.format("SELECT AREA_CODE, AREA_NAME from NEO_CAS_LMS_GA25_GIR_SD.AREA WHERE AREA_NAME like '%%%s%%' and APPROVAL_STATUS=0",area);
		String sql=String.format("SELECT AREA_CODE, AREA_NAME from NEO_CAS_LMS_GA25_GIR_SD.AREA WHERE AREA_NAME = '%s' and APPROVAL_STATUS=0",area);
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("AREA_CODE")!=null)
		{
			return resultData.get("AREA_CODE").toString();
		}

		return "";
	}

	public String getDataF1_IncomeExpense()
	{
		String sql="SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.INCOME_EXPENSE WHERE DESCRIPTION='Main Personal Income' and APPROVAL_STATUS=0";
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("CODE")!=null)
		{
			return resultData.get("CODE").toString();
		}

		return "";
	}

	public String getDataF1_ProductTypeCode()
	{
		String sql="SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter WHERE DTYPE='ProductCategory' AND NAME ='Personal Finance'";
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("CODE")!=null)
		{
			return resultData.get("CODE").toString();
		}

		return "";
	}

	public String getDataF1_AddressType(String adressType)
	{
		String sql=String.format("Select CODE, NAME from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter WHERE dtype = 'AddressType' AND NAME = '%s'",adressType);
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("CODE")!=null)
		{
			return resultData.get("CODE").toString();
		}

		return "";
	}

	public String getDataF1_Industry()
	{
		String sql=String.format("SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.INDUSTRY WHERE NAME='T-Manufacturing' and  APPROVAL_STATUS=0");
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("CODE")!=null)
		{
			return resultData.get("CODE").toString();
		}

		return "";
	}

	public String getDataF1_EmploymentType()
	{
		String sql=String.format("SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter WHERE DTYPE='EmploymentType' AND NAME ='Regular Employment'");
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("CODE")!=null)
		{
			return resultData.get("CODE").toString();
		}

		return "";
	}

	public String getDataF1_residentType()
	{
		String sql=String.format(" select code from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter where dtype='ResidentType'");
		List<Map<String,Object>> resultData =jdbcTemplate.queryForList(sql);

		if(resultData!=null && resultData.size()>0)
		{
			return resultData.get(0).get("CODE").toString();
		}

		return "";
	}

	public String getDataF1_IssusingCountry()
	{
		String sql=String.format("SELECT COUNTRYISOCODE from NEO_CAS_LMS_GA25_GIR_SD.COUNTRY WHERE COUNTRY_NAME ='Vietnam' and APPROVAL_STATUS=0");
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("COUNTRYISOCODE")!=null)
		{
			return resultData.get("COUNTRYISOCODE").toString();
		}

		return "";
	}

	public String getDataF1_HouseOwnerShip()
	{
		String sql=String.format("select fco.CUSTOME_ITEM_VALUE from " +
				"NEO_CAS_LMS_GA25_GIR_SD.uimeta_data ud inner join " +
				"NEO_CAS_LMS_GA25_GIR_SD.panel_definition pd on ud.id = pd.ui_panel_def_fk " +
				"join NEO_CAS_LMS_GA25_GIR_SD.field_definition fd on pd.id = fd.panel_field_def_fk " +
				"JOIN NEO_CAS_LMS_GA25_GIR_SD.field_custom_options fco on fd.id = fco.fk_field_custom_option " +
				"and ud.approval_status=0 " +
				"and ud.model_name like '%frmAppDtl%' " +
				"and pd.PANEL_KEY='familyinformation' " +
				"and fd.FIELD_KEY='house_ownership' " +
				"AND fco.CUSTOME_ITEM_LABEL='Family Owned without Mortgage'");
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("CUSTOME_ITEM_VALUE")!=null)
		{
			return resultData.get("CUSTOME_ITEM_VALUE").toString();
		}

		return "";
	}

	public String getDataF1_VapDetail(){
		String sql=String.format("select CODE from NEO_CAS_LMS_GA25_GIR_SD.vap_parameter_policy vp where vp.name='INSP02_InsParameter' and vp.approval_status=0;");
		Map<String, Object> resultData =jdbcTemplate.queryForMap(sql);

		if(resultData!=null && resultData.size()>0 && resultData.get("CODE")!=null)
		{
			return resultData.get("CODE").toString();
		}

		return "";
	}

}
