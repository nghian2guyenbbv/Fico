package vn.com.tpf.microservices.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.StringUtils;
import vn.com.tpf.microservices.models.*;
import vn.com.tpf.microservices.models.AutoAllocation.AutoAllocationDTO;
import vn.com.tpf.microservices.models.AutoAllocation.AutoAssignAllocationDTO;
import vn.com.tpf.microservices.models.AutoAllocation.AutoReassignUserDTO;
import vn.com.tpf.microservices.models.AutoAssign.AutoAssignDTO;
import vn.com.tpf.microservices.models.AutoCRM.*;
import vn.com.tpf.microservices.models.AutoField.RequestAutomationDTO;
import vn.com.tpf.microservices.models.AutoField.SubmitFieldDTO;
import vn.com.tpf.microservices.models.AutoField.WaiveFieldDTO;
import vn.com.tpf.microservices.models.Automation.*;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDTO;
import vn.com.tpf.microservices.models.QuickLead.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataInitial {
    public static Map<String, Object> getDataFromDE_QL(Application application) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(application.getQuickLead().getCommunicationTranscript())){
            application.getQuickLead().setCommunicationTranscript("dummy");
        }
        map.put("ApplicationDTO", application);
        return map;
    }

    public static Map<String, Object> getDataFromDE(Application application) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();

        map.put("ApplicationDTO", application);
        map.put("AppId", application.getApplicationId());

        ////********************************APPLICATIONINFO DTO************************************////
        //PersonalInfo
        PersonalInfo personalInfo=application.getApplicationInformation().getPersonalInformation().getPersonalInfo();

        //Identification
        List<IdentificationDTO> identificationDTOs = new ArrayList<>();
        for (Identification identification : application.getApplicationInformation().getPersonalInformation().getIdentifications()) {
            identificationDTOs.add(new IdentificationDTO(identification.getIdentificationType(), identification.getIdentificationNumber(), identification.getIssueDate(), identification.getExpiryDate(), identification.getPlaceOfIssue(),identification.getIssuingCountry()));
        }

        //Address
        List<AddressDTO> addressDTOs = new ArrayList<>();
        for (Address address : application.getApplicationInformation().getPersonalInformation().getAddresses()) {
            AddressDTO addressDTO = AddressDTO.builder()
                    .addressType(address.getAddressType())
                    .country(address.getCountry())
                    .region(address.getState())
                    .city(address.getCity())
                    .area(address.getArea())
                    .building(address.getAddressLine1())
                    .house(address.getAddressLine2())
                    .ward(address.getAddressLine3())
                    .addressLandmark(address.getLandMark())
                    .residentDurationYear(address.getYearsInCurrentAddress())
                    .residentDurationMonth(address.getMonthsInCurrentAddress())
                    .priStd(address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().isPresent()?address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().get().getStdCode():"")
                    .priNumber(address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().isPresent()?address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().get().getPhoneNumber():"")
                    .priExt(address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().isPresent()?address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().get().getExtension():"")
                    .mobilePhone(address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().isPresent()?address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().get().getPhoneNumber():"").build();
            addressDTOs.add(addressDTO);
        }

        //update them communicatedetail
        CommunicationDetails communicationDetails=application.getApplicationInformation().getPersonalInformation().getCommunicationDetails();


        //Family
        List<FamilyDTO> familyDTOs = new ArrayList<>();
        for (Family family : application.getApplicationInformation().getPersonalInformation().getFamily()) {
            FamilyDTO familyDTO = FamilyDTO.builder()
                    .relationshipType(family.getRelationship())
                    .memberName(family.getMemberName())
                    .phoneNumber(family.getPhoneNumber())
                    .educationStatus("")
                    .comName("").build();
            familyDTOs.add(familyDTO);
        }

        //Employment Detail
        EmploymentDetails employmentDetails=application.getApplicationInformation().getEmploymentDetails();
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .occupationType(employmentDetails.getOccupationType())
                .employeeNumber(employmentDetails.getEmployeeNumber())
                .taxCode(employmentDetails.getEmployerCode())
                .natureOfBussiness(employmentDetails.getNatureOfBusiness())
                .natureOfOccupation(employmentDetails.getNatureOfOccupation()!=null?employmentDetails.getNatureOfOccupation():"Unemployed")
                .industry(employmentDetails.getIndustry())
                .department(employmentDetails.getDepartmentName())
                .level(employmentDetails.getDesignation())
                .employmentStatus(employmentDetails.getEmploymentStatus())
                .employmentType(employmentDetails.getEmploymentType())
                .durationYears(employmentDetails.getYearsInJob())
                .durationMonths(employmentDetails.getMonthsInJob())
                .otherCompanyTaxCode(employmentDetails.getOtherCompanyTaxCode().equals("0")?"":employmentDetails.getOtherCompanyTaxCode())
                .totalMonthsInOccupation(employmentDetails.getTotalMonthsInOccupation().isEmpty()?0:Integer.parseInt(employmentDetails.getTotalMonthsInOccupation()))
                .totalYearsInOccupation(employmentDetails.getTotalYearsInOccupation().isEmpty()?0:Integer.parseInt(employmentDetails.getTotalYearsInOccupation()))
                .isMajorEmployment(employmentDetails.getIsMajorEmployment())
                .remarks(employmentDetails.getRemarks()).build();

        //Income Detail
        List<IncomeDetailDTO> incomeDetailDTOList=new ArrayList<>();
        for(FinancialDetail financialDetail : application.getApplicationInformation().getFinancialDetails())
        {
            IncomeDetailDTO incomeDetailDTO=IncomeDetailDTO.builder()
                    .incomeHead(financialDetail.getIncomeExpense())
                    .frequency("Monthly")
                    .amount(financialDetail.getAmount())
                    .percentage("100")
                    .modeOfPayment(financialDetail.getModeOfPayment())
                    .dayOfSalaryPayment(financialDetail.getDayOfSalaryPayment())
                    .build();
            incomeDetailDTOList.add(incomeDetailDTO);
        }

        //Bank Credit Card Detail [19/01/2021]
        List<BankCreditCardDetailsDTO> bankCreditCardDetailsDTOList=new ArrayList<>();
        for(BankCreditCardDetails bankCreditCardDetails : application.getApplicationInformation().getBankingDetails())
        {
            BankCreditCardDetailsDTO bankingDetailDTO = BankCreditCardDetailsDTO.builder()
                    .bankName(bankCreditCardDetails.getBankName())
                    .branchName(bankCreditCardDetails.getBankName())
                    .accountNumber(bankCreditCardDetails.getAccountNumber())
                    .typeOfAccount(bankCreditCardDetails.getTypeOfAccount())
                    .build();
            bankCreditCardDetailsDTOList.add(bankingDetailDTO);
        }

        ApplicationInfoDTO applicationInfoDTO = ApplicationInfoDTO.builder()
                .gender(personalInfo.getGender())
                .firstName(personalInfo.getFirstName())
                .middleName(personalInfo.getMiddleName())
                .lastName(personalInfo.getLastName())
                .dateOfBirth(personalInfo.getDateOfBirth())
                .placeOfIssue(identificationDTOs.stream().filter(x->x.getDocumentType().equals("Current National ID")).findAny().get().getPlaceOfIssue())
                .maritalStatus(personalInfo.getMaritalStatus())
                .national(personalInfo.getNationality())
                .education(personalInfo.getCustomerCategoryCode())
                .identification(identificationDTOs)
                .address(addressDTOs)
                .email(application.getApplicationInformation().getPersonalInformation().getCommunicationDetails().getPrimaryEmailId())
                .communicationDetails(communicationDetails)
                .family(familyDTOs)
                .employmentDetails(employmentDTO)
                .incomeDetails(incomeDetailDTOList)
                .bankCreditCardDetails(bankCreditCardDetailsDTOList)
                .build();
        map.put("ApplicationInfoDTO", applicationInfoDTO);
        ////********************************END APPLICATIONINFO DTO************************************////

        ////******************************** LOAN DETAIL DTO************************************////
        //LoanDetails
        LoanDetails loanDetails=application.getLoanDetails();
        LoanDetailsDTO loanDetailsDTO = LoanDetailsDTO.builder()
                .branch("VINID") //set branch default la FPT
                .channel("VINID")
                .applicationNumber(loanDetails.getSourcingDetails().chassisApplicationNum)
                .loanAppType(loanDetails.getSourcingDetails().loanApplicationType)
                .productName(loanDetails.getSourcingDetails().productCode)
                .scheme(loanDetails.getSourcingDetails().getSchemeCode())
                .loanAmount(loanDetails.getSourcingDetails().getLoanAmountRequested())
                .loanTerm(loanDetails.getSourcingDetails().getRequestedTenure())
                .loadPurpose(loanDetails.getSourcingDetails().getLoanPurposeDesc()!= null ? loanDetails.getSourcingDetails().getLoanPurposeDesc():"")
                .saleAgentCode(loanDetails.getSourcingDetails().getSaleAgentCode())
                .build();
        map.put("LoanDetailsDTO", loanDetailsDTO);

        //LoanVAP
        LoanDetailsVapDTO loanDetailsVapDTO = LoanDetailsVapDTO.builder().build();
        if(loanDetails.getVapDetails()!=null)
        {
            loanDetailsVapDTO = LoanDetailsVapDTO.builder()
                    .vapProduct(loanDetails.getVapDetails().vapProduct) //set branch default la FPT
                    .vapTreatment(loanDetails.getVapDetails().vapTreatment)
                    .insuranceCompany(loanDetails.getVapDetails().getInsuranceCompany())
                    .build();
        }
        map.put("LoanDetailsVapDTO", loanDetailsVapDTO);

        ////********************************END LOAN DETAIL DTO************************************////

        ////******************************** DOCUMENT DTO ************************************////
        List<DocumentDTO> documentDTOS = new ArrayList<>();
        if(application.getDocuments()!=null && application.getDocuments().size()>0){
            for(Document document : application.getDocuments()) {
                DocumentDTO documentDTO = DocumentDTO.builder ()
                        .originalname(document.getOriginalname())
                        .filename(document.getFilename())
                        .type(document.getType()).build();
                documentDTOS.add(documentDTO);
            }
        }
        map.put("DocumentDTO", documentDTOS);
        ////********************************END DOCUMENT DTO************************************////

        ////******************************** REFERENCE DTO ************************************////

        List<ReferenceDTO> referenceDTOs = new ArrayList<>();
        for(Reference reference : application.getReferences()) {
            ReferenceDTO referenceDTO = ReferenceDTO.builder()
                    .fullName(reference.getName())
                    .relationShip(reference.getRelationship())
                    .priStd(reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().isPresent()?reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().get().getStdCode():"")
                    .priNumber(reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().isPresent()?reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().get().getPhoneNumber():"")
                    .priExt(reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().isPresent()?reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Primary Phone")).findAny().get().getExtension():"")
                    .mobilePhoneNumber(reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().isPresent()?reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().get().getPhoneNumber():"").build();
            referenceDTOs.add(referenceDTO);
        }
        map.put("ReferenceDTO", referenceDTOs);
        ////********************************END REFERENCE DTO************************************////

        ////******************************** DYNAMIC FORM ************************************////
        List<DynamicForm> dynamicFormList=new ArrayList<>();
        for(DynamicForm dynamicForm: application.getDynamicForm()){
            if(dynamicForm.getFormName().equals("frmAppDtl")){
                MiscFrmAppDtlDTO miscFrmAppDtlDTO = MiscFrmAppDtlDTO.builder()
                        .loanPurpose(dynamicForm.getLoanPurpose())
                        .numOfDependents(dynamicForm.getNumberOfDependents())
                        .houseOwnership(dynamicForm.getHouseOwnership())
                        //.mortgagePaymentCost(dynamicForm.getMonthlyFee())
                        .mortgagePaymentCost(dynamicForm.getMonthlyRental())
                        .newBankCardNumber(dynamicForm.getNewBankCardNumber())
                        .salesAgentCode(dynamicForm.getSaleAgentCode())
                        .maxRequestRate(dynamicForm.getMaximumInterestedRate())
                        .courierCode(dynamicForm.getCourierCode())
                        .totalMonthlyPayable(dynamicForm.getTotalMonthlyPayable())
                        .loanOfWork(dynamicForm.getLoanAtWork()!=null?dynamicForm.getLoanAtWork():"")
                        .companyName(dynamicForm.getCompanyName()!=null?dynamicForm.getCompanyName():"")
                        .monthlyFee(dynamicForm.getMonthlyFee()!=null?dynamicForm.getMonthlyFee():"")
                        .contractNumber(dynamicForm.getContractNumber()!=null?dynamicForm.getContractNumber():"")
                        .oldContractLoanAmount(dynamicForm.getOldContractLoanAmount()!=null?dynamicForm.getOldContractLoanAmount():"")
                        .zalo(dynamicForm.getZalo()!=null?dynamicForm.getZalo():"")
                        .remark(dynamicForm.getRemark()).build();
                map.put("MiscFrmAppDtlDTO", miscFrmAppDtlDTO);
            }


//            //VINID
//           if(dynamicForm.getFormName().equals("frm2WL")){
//               MiscVinIdDTO miscVinIdDTO = MiscVinIdDTO.builder()
//                        .model(dynamicForm.getModel())
//                        .goodCode(dynamicForm.getGoodCode())
//                        .goodType(dynamicForm.getGoodType())
//                        .quantity(dynamicForm.getQuantity())
//                        .venCode(dynamicForm.getVenCode())
//                        .vinCode(dynamicForm.getVinCode())
//                        .saleChannel(dynamicForm.getSaleChannel())
//                        .dealerCode(dynamicForm.getDealerCode())
//                        .productName(dynamicForm.getProductName())
//                        .goodPrice(dynamicForm.getGoodPrice())
//                        .downPayment(dynamicForm.getDownPayment())
//                        .employeeCardNum(dynamicForm.getEmployeeCard()).build();
//                map.put("MiscVinIdDTO", miscVinIdDTO);
//            }

//            //FPT
//            if(dynamicForm.getFormName().equals("frmFPT")){
//                 MiscFptDTO  miscFptDTO = MiscFptDTO.builder()
//                            .model(dynamicForm.getModel())
//                            .goodCode(dynamicForm.getGoodCode())
//                            .goodType(dynamicForm.getGoodType())
//                            .quantity(dynamicForm.getQuantity())
//                            .goodPrice(dynamicForm.getGoodPrice())
//                            .downPayment(dynamicForm.getDownPayment())
//                            .employeeCardNum(dynamicForm.getEmployeeCard()).build();
//                map.put("MiscFptDTO", miscFptDTO);
//            }

        }
        ////********************************ENDDYNAMIC FORM************************************////

        return map;
    }

    public static Map<String, Object> getDataMomo(Application application){
        Map<String, Object> map = new HashMap<>();

        map.put("ApplicationDTO", application);
        map.put("AppId", application.getApplicationId());

        ////********************************APPLICATIONINFO DTO************************************////
        //PersonalInfo
        PersonalInfo personalInfo=application.getApplicationInformation().getPersonalInformation().getPersonalInfo();
        personalInfo.setCustomerCategoryCode("Highschool");

        //Identification
        List<IdentificationDTO> identificationDTOs = new ArrayList<>();
        for (Identification identification : application.getApplicationInformation().getPersonalInformation().getIdentifications()) {
            identificationDTOs.add(IdentificationDTO.builder().documentType(identification.getIdentificationType())
                                                                .documentNumber(identification.identificationNumber)
                                                                .issueDate(identification.getIssueDate()==null?"":identification.getIssueDate())
                                                                .expirationDate("")
                                                                .countryOfIssue(identification.getIssuingCountry()).build());
        }

        //Address
        List<AddressDTO> addressDTOs = new ArrayList<>();
        for (Address address : application.getApplicationInformation().getPersonalInformation().getAddresses()) {
            AddressDTO addressDTO = AddressDTO.builder()
                    .addressType(address.getAddressType())
                    .country(address.getCountry())
                    .region("ALL")
                    .city(address.getCity())
                    .area(address.getArea())
                    .building(address.getAddressLine1())
                    .house(address.getAddressLine2())
                    .ward(address.getAddressLine3())
                    .residentDurationYear("1")
                    .residentDurationMonth("1")
                    .mobilePhone(address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().isPresent()?address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().get().getPhoneNumber().replace("+84", ""):"").build();
            addressDTOs.add(addressDTO);
        }
        AddressDTO addressDTO1 = AddressDTO.builder()
                .addressType("Family Book Address")
                .country(addressDTOs.get(0).getCountry())
                .region("ALL")
                .city(addressDTOs.get(0).getCity())
                .area(addressDTOs.get(0).getArea())
                .building(addressDTOs.get(0).getBuilding())
                .house(addressDTOs.get(0).getHouse())
                .ward(addressDTOs.get(0).getWard())
                .residentDurationYear("")
                .residentDurationMonth("")
                .mobilePhone("").build();
        addressDTOs.add(addressDTO1);

        AddressDTO addressDTO2 = AddressDTO.builder()
                .addressType("Working Address")
                .country(addressDTOs.get(0).getCountry())
                .region("ALL")
                .city(addressDTOs.get(0).getCity())
                .area(addressDTOs.get(0).getArea())
                .building(addressDTOs.get(0).getBuilding())
                .house(addressDTOs.get(0).getHouse())
                .ward(addressDTOs.get(0).getWard())
                .residentDurationYear("")
                .residentDurationMonth("")
                .mobilePhone("").build();
        addressDTOs.add(addressDTO2);

        //Family
        List<FamilyDTO> familyDTOs = new ArrayList<>();
        if(application.getApplicationInformation().getPersonalInformation().getFamily()!=null && application.getApplicationInformation().getPersonalInformation().getFamily().size()>0) {
            for (Family family : application.getApplicationInformation().getPersonalInformation().getFamily()) {
                FamilyDTO familyDTO = FamilyDTO.builder()
                        .relationshipType(family.getRelationship())
                        .memberName(family.getMemberName())
                        .phoneNumber(family.getPhoneNumber())
                        .educationStatus("")
                        .comName("").build();
                familyDTOs.add(familyDTO);
            }
        }

        //check MarialStatus: married
        if(personalInfo.getMaritalStatus().equals("Married")){
            AddressDTO addressDTO3 = AddressDTO.builder()
                    .addressType("Spouse Address")
                    .country(addressDTOs.get(0).getCountry())
                    .region("ALL")
                    .city(addressDTOs.get(0).getCity())
                    .area(addressDTOs.get(0).getArea())
                    .building(addressDTOs.get(0).getBuilding())
                    .house(addressDTOs.get(0).getHouse())
                    .ward(addressDTOs.get(0).getWard())
                    .residentDurationYear("")
                    .residentDurationMonth("")
                    //.mobilePhone(familyDTOs.stream().filter(c->c.getRelationshipType().equals("Spouse")).findAny().isPresent()?familyDTOs.stream().filter(c->c.getRelationshipType().equals("Spouse")).findAny().get().getPhoneNumber():"").build();
                    .mobilePhone("").build();
            addressDTOs.add(addressDTO3);
        }

        //Employment Detail
        EmploymentDetails employmentDetails=application.getApplicationInformation().getEmploymentDetails();
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .occupationType("Others")
                .natureOfOccupation("Unemployed")
                .build();

        //Income Detail
        List<IncomeDetailDTO> incomeDetailDTOList=new ArrayList<>();
        for(FinancialDetail financialDetail : application.getApplicationInformation().getFinancialDetails())
        {
            IncomeDetailDTO incomeDetailDTO=IncomeDetailDTO.builder()
                    .incomeHead(financialDetail.getIncomeExpense())
                    .frequency("Monthly")
                    .amount(financialDetail.getAmount())
                    .percentage("100")
                    .build();
            incomeDetailDTOList.add(incomeDetailDTO);
        }

        ApplicationInfoDTO applicationInfoDTO = ApplicationInfoDTO.builder()
                .gender(personalInfo.getGender())
                .firstName(personalInfo.getLastName())
                .middleName(personalInfo.getMiddleName())
                .lastName(personalInfo.getFirstName())
                .dateOfBirth(personalInfo.getDateOfBirth())
                .placeOfIssue(personalInfo.getIssuePlace())
                .maritalStatus(personalInfo.getMaritalStatus())
                .national(personalInfo.getNationality())
                .education(personalInfo.getCustomerCategoryCode())
                .identification(identificationDTOs)
                .address(addressDTOs)
                .email(application.getApplicationInformation().getPersonalInformation().getCommunicationDetails().getPrimaryEmailId())
                .family(familyDTOs)
                .employmentDetails(employmentDTO)
                .incomeDetails(incomeDetailDTOList)
                .build();
        map.put("ApplicationInfoDTO", applicationInfoDTO);
        ////********************************END APPLICATIONINFO DTO************************************////

        ////******************************** LOAN DETAIL DTO************************************////
        //LoanDetails
        LoanDetails loanDetails=application.getLoanDetails();
        LoanDetailsDTO loanDetailsDTO = LoanDetailsDTO.builder()
                .branch("MOMO") //set branch default la FPT
                .channel("MOMO")
                .applicationNumber(loanDetails.getSourcingDetails().chassisApplicationNum)
                .loanAppType(loanDetails.getSourcingDetails().loanApplicationType)
                .productName(loanDetails.getSourcingDetails().productCode)
                .scheme(loanDetails.getSourcingDetails().getSchemeCode())
                .loanAmount(loanDetails.getSourcingDetails().getLoanAmountRequested())
                .loanTerm(loanDetails.getSourcingDetails().getRequestedTenure())
                .saleAgentCode(loanDetails.getSourcingDetails().getSaleAgentCode())
                .build();


        if (loanDetailsDTO.getProductName().equals("DG01"))
        {
            loanDetailsDTO.setScheme("DG01_MOMO TRIAL");
        }
        else
        {
            loanDetailsDTO.setScheme("DG02_MOMO ENTRY");
        }
        loanDetailsDTO.setProductName("DGL_CASH");
        map.put("LoanDetailsDTO", loanDetailsDTO);

        //LoanVAP

        if(loanDetails.getVapDetails()!=null)
        {
            LoanDetailsVapDTO loanDetailsVapDTO = LoanDetailsVapDTO.builder()
                    .vapProduct(loanDetails.getVapDetails().vapProduct) //set branch default la FPT
                    .vapTreatment(loanDetails.getVapDetails().vapTreatment)
                    .insuranceCompany(loanDetails.getVapDetails().getInsuranceCompany())
                    .build();
            map.put("LoanDetailsVapDTO", loanDetailsVapDTO);
        }

        ////********************************END LOAN DETAIL DTO************************************////

        ////******************************** DOCUMENT DTO ************************************////
        List<DocumentDTO> documentDTOS = new ArrayList<>();
        if(application.getDocuments()!=null && application.getDocuments().size()>0){
            for(Document document : application.getDocuments()) {
                DocumentDTO documentDTO = DocumentDTO.builder ()
                        .originalname(document.getOriginalname())
                        .filename(document.getFilename()).build();
                documentDTOS.add(documentDTO);
            }
        }
        map.put("DocumentDTO", documentDTOS);
        ////********************************END DOCUMENT DTO************************************////

        ////******************************** REFERENCE DTO ************************************////

        List<ReferenceDTO> referenceDTOs = new ArrayList<>();
        for(Reference reference : application.getReferences()) {
            ReferenceDTO referenceDTO = ReferenceDTO.builder()
                    .fullName(reference.getName())
                    .relationShip(reference.getRelationship())
                    .mobilePhoneNumber(reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().isPresent()?reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().get().getPhoneNumber():"").build();
            referenceDTOs.add(referenceDTO);
        }
        map.put("ReferenceDTO", referenceDTOs);
        ////********************************END REFERENCE DTO************************************////

        ////******************************** DYNAMIC FORM ************************************////

        MiscFrmAppDtlDTO miscFrmAppDtlDTO = MiscFrmAppDtlDTO.builder()
                .loanPurpose("Education, sports")
                .numOfDependents("0")
                .houseOwnership("Family Owned without Mortgage")
                .mortgagePaymentCost("0")
                .newBankCardNumber("2222222222222222")
                //.salesAgentCode("DS0001100") //UAT
                .salesAgentCode("MM99999") //PRO
                .maxRequestRate("62").build();
        map.put("MiscFrmAppDtlDTO", miscFrmAppDtlDTO);

        ////********************************ENDDYNAMIC FORM************************************////

        return map;
    }

    public static Map<String, Object> getDataFpt(Application application){
        Map<String, Object> map = new HashMap<>();

        map.put("ApplicationDTO", application);
        map.put("AppId", application.getApplicationId());

        ////********************************APPLICATIONINFO DTO************************************////
        //PersonalInfo
        PersonalInfo personalInfo=application.getApplicationInformation().getPersonalInformation().getPersonalInfo();
        personalInfo.setCustomerCategoryCode("Highschool");

        //Identification
        List<IdentificationDTO> identificationDTOs = new ArrayList<>();
        for (Identification identification : application.getApplicationInformation().getPersonalInformation().getIdentifications()) {
            identificationDTOs.add(IdentificationDTO.builder().documentType(identification.getIdentificationType())
                    .documentNumber(identification.identificationNumber)
                    .issueDate(identification.getIssueDate()==null?"":identification.getIssueDate())
                    .expirationDate("")
                    .countryOfIssue(identification.getIssuingCountry()).build());
        }

        IdentificationDTO identificationDTO1=IdentificationDTO.builder()
                .documentType("Family Book Number")
                .documentNumber(identificationDTOs.get(0).getDocumentNumber())
                .issueDate("")
                .expirationDate("")
                .countryOfIssue("Vietnam").build();
        identificationDTOs.add(identificationDTO1);

        IdentificationDTO identificationDTO2=IdentificationDTO.builder()
                .documentType("Insurance Number")
                .documentNumber(application.dynamicForm.stream().filter(q->q.formName.equals("frmFpt")).findAny().get().getEmployeeCard())
                .issueDate("")
                .expirationDate("")
                .countryOfIssue("Vietnam").build();
        identificationDTOs.add(identificationDTO2);

        //Address
        List<AddressDTO> addressDTOs = new ArrayList<>();
        for (Address address : application.getApplicationInformation().getPersonalInformation().getAddresses()) {
            AddressDTO addressDTO = AddressDTO.builder()
                    .addressType(address.getAddressType())
                    .country(address.getCountry())
                    .region("ALL")
                    .city(address.getCity())
                    .area(address.getArea())
                    .building(address.getAddressLine1())
                    .house(address.getAddressLine2())
                    .ward(address.getAddressLine3())
                    .residentDurationYear("1")
                                                .residentDurationMonth("1")
                    .mobilePhone(address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().isPresent()?address.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().get().getPhoneNumber().replace("+84", ""):"").build();
            addressDTOs.add(addressDTO);
        }

        //Family
        List<FamilyDTO> familyDTOs = new ArrayList<>();
        if(application.getApplicationInformation().getPersonalInformation().getFamily()!=null && application.getApplicationInformation().getPersonalInformation().getFamily().size()>0) {
            for (Family family : application.getApplicationInformation().getPersonalInformation().getFamily()) {
                FamilyDTO familyDTO = FamilyDTO.builder()
                        .relationshipType(family.getRelationship())
                        .memberName(family.getMemberName())
                        .phoneNumber(family.getPhoneNumber())
                        .educationStatus("")
                        .comName("").build();
                familyDTOs.add(familyDTO);
            }
        }

//        //check MarialStatus: married
//        if(personalInfo.getMaritalStatus().equals("Married")){
//            AddressDTO addressDTO3 = AddressDTO.builder()
//                    .addressType("Spouse Address")
//                    .country(addressDTOs.get(0).getCountry())
//                    .region("ALL")
//                    .city(addressDTOs.get(0).getCity())
//                    .area(addressDTOs.get(0).getArea())
//                    .building(addressDTOs.get(0).getBuilding())
//                    .house(addressDTOs.get(0).getHouse())
//                    .ward(addressDTOs.get(0).getWard())
//                    .residentDurationYear("")
//                    .residentDurationMonth("")
//                    .mobilePhone(familyDTOs.stream().filter(c->c.getRelationshipType().equals("Spouse")).findAny().isPresent()?familyDTOs.stream().filter(c->c.getRelationshipType().equals("Spouse")).findAny().get().getPhoneNumber():"").build();
//            addressDTOs.add(addressDTO3);
//        }

        //Employment Detail
        EmploymentDetails employmentDetails=application.getApplicationInformation().getEmploymentDetails();
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .occupationType("Salaried")
                .taxCode("Others")
                .natureOfBussiness("Regular")
                .industry("T-Manufacturing")
                .department("SẢN XUẤT")
                .level("1")
                .employmentStatus("Present")
                .durationYears("0")
                .durationMonths("1").build();

        //Income Detail
        List<IncomeDetailDTO> incomeDetailDTOList=new ArrayList<>();
        for(FinancialDetail financialDetail : application.getApplicationInformation().getFinancialDetails())
        {
            IncomeDetailDTO incomeDetailDTO=IncomeDetailDTO.builder()
                    .incomeHead(financialDetail.getIncomeExpense())
                    .frequency("Monthly")
                    .amount(financialDetail.getAmount())
                    .percentage("100")
                    .build();
            incomeDetailDTOList.add(incomeDetailDTO);
        }

        ApplicationInfoDTO applicationInfoDTO = ApplicationInfoDTO.builder()
                .gender(personalInfo.getGender())
                .firstName(personalInfo.getLastName())
                .middleName(personalInfo.getMiddleName())
                .lastName(personalInfo.getFirstName())
                .dateOfBirth(personalInfo.getDateOfBirth())
                .placeOfIssue("Hà Nội")
                .maritalStatus(personalInfo.getMaritalStatus())
                .national(personalInfo.getNationality())
                .education(personalInfo.getCustomerCategoryCode())
                .identification(identificationDTOs)
                .address(addressDTOs)
                .email("")
                .family(familyDTOs)
                .employmentDetails(employmentDTO)
                .incomeDetails(incomeDetailDTOList)
                .build();
        map.put("ApplicationInfoDTO", applicationInfoDTO);
        ////********************************END APPLICATIONINFO DTO************************************////

        ////******************************** LOAN DETAIL DTO************************************////
        //LoanDetails
        LoanDetails loanDetails=application.getLoanDetails();
        LoanDetailsDTO loanDetailsDTO = LoanDetailsDTO.builder()
                .branch("FPT") //set branch default la FPT
                .channel("FPT")
                .applicationNumber(loanDetails.getSourcingDetails().chassisApplicationNum)
                .loanAppType(loanDetails.getSourcingDetails().loanApplicationType)
                .productName("CDL_CASH")
                .scheme(loanDetails.getSourcingDetails().getProductCode())
                .loanAmount(loanDetails.getSourcingDetails().getLoanAmountRequested())
                .loanTerm(loanDetails.getSourcingDetails().getRequestedTenure())
                .saleAgentCode(loanDetails.getSourcingDetails().getSaleAgentCode())
                .build();

        map.put("LoanDetailsDTO", loanDetailsDTO);

//        //LoanVAP
//        LoanDetailsVapDTO loanDetailsVapDTO = LoanDetailsVapDTO.builder()
//                .vapProduct(loanDetails.getVapDetails().vapProduct) //set branch default la FPT
//                .vapTreatment(loanDetails.getVapDetails().vapTreatment)
//                .insuranceCompany(loanDetails.getVapDetails().getInsuranceCompany())
//                .build();
//        map.put("LoanDetailsVapDTO", loanDetailsVapDTO);

        ////********************************END LOAN DETAIL DTO************************************////

        ////******************************** DOCUMENT DTO ************************************////
        List<DocumentDTO> documentDTOS = new ArrayList<>();
        if(application.getDocuments()!=null && application.getDocuments().size()>0){
            for(Document document : application.getDocuments()) {
                DocumentDTO documentDTO = DocumentDTO.builder ()
                        .originalname(document.getOriginalname())
                        .filename(document.getFilename()).build();
                documentDTOS.add(documentDTO);
            }
        }
        map.put("DocumentDTO", documentDTOS);
        ////********************************END DOCUMENT DTO************************************////

        ////******************************** REFERENCE DTO ************************************////

        List<ReferenceDTO> referenceDTOs = new ArrayList<>();
        for(Reference reference : application.getReferences()) {
            ReferenceDTO referenceDTO = ReferenceDTO.builder()
                    .fullName(reference.getName())
                    .relationShip(reference.getRelationship())
                    .mobilePhoneNumber(reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().isPresent()?reference.getPhoneNumbers().stream().filter(c->c.getPhoneType().equals("Mobile Phone")).findAny().get().getPhoneNumber():"").build();
            referenceDTOs.add(referenceDTO);
        }
        map.put("ReferenceDTO", referenceDTOs);
        ////********************************END REFERENCE DTO************************************////

        ////******************************** DYNAMIC FORM ************************************////

        List<DynamicForm> dynamicFormList=new ArrayList<>();
        for(DynamicForm dynamicForm: application.getDynamicForm()){
            if(dynamicForm.getFormName().equals("frmAppDtl")){
                MiscFrmAppDtlDTO miscFrmAppDtlDTO = MiscFrmAppDtlDTO.builder()
                        .loanPurpose("Other")
                        .numOfDependents("0")
                        .mortgagePaymentCost("0")
                        .houseOwnership("Rented")
                        .newBankCardNumber("1111111111111111")
                        .salesAgentCode(dynamicForm.getSaleAgentCode())
                        .maxRequestRate("62").build();
                map.put("MiscFrmAppDtlDTO", miscFrmAppDtlDTO);
            }

            //FPT
            if(dynamicForm.getFormName().equals("frmFpt")){
                 MiscFptDTO  miscFptDTO = MiscFptDTO.builder()
                            .productDetails(dynamicForm.getProductDetails())
                            .employeeCardNum(dynamicForm.getEmployeeCard())
                            .creditLimit("0")
                            .downPayment(dynamicForm.getDownPayment()).build();
                map.put("MiscFptDTO", miscFptDTO);
            }

        }

        ////********************************ENDDYNAMIC FORM************************************////

        return map;
    }

    public static Map<String, Object> getDataFromDE_AutoAssign(List<AutoAssignDTO> autoAssignDTOList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("AutoAssignList", autoAssignDTOList);
        return map;
    }

    public static Map<String, Object> getDataFromDE_ResponseQuery(DEResponseQueryDTO deResponseQueryDTOList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("DEResponseQueryList", deResponseQueryDTOList);
        return map;
    }

    public static Map<String, Object> getDataFromDE_SaleQueue(DESaleQueueDTO deSaleQueueDTOList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("DESaleQueueList", deSaleQueueDTOList);
        return map;
    }

    public static Map<String, Object> getDataFrom_Waive_Field(RequestAutomationDTO waiveFieldDTOList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("RequestAutomationWaiveFieldList", waiveFieldDTOList);
        ////********************************REASSIGN USER************************************////
        List<WaiveFieldDTO> waiveFieldDTOs = new ArrayList<>();
        for(WaiveFieldDTO waiveField : waiveFieldDTOList.getWaiveFieldDTO()) {
            WaiveFieldDTO waiveFieldDTO = WaiveFieldDTO.builder()
                    .appId(waiveField.getAppId())
                    .project(waiveFieldDTOList.getProject())
                    .referenceId(waiveFieldDTOList.getReference_id())
                    .transactionId(waiveFieldDTOList.getTransaction_id())
                    .projectAuto("WAIVEFIELD")
                    .build();
            waiveFieldDTOs.add(waiveFieldDTO);
        }
        map.put("waiveFieldList", waiveFieldDTOs);
        return map;
    }

    public static Map<String, Object> getDataFrom_Submit_Field(RequestAutomationDTO submitFieldDTOList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("RequestAutomationSubmitFieldList", submitFieldDTOList);
        ////********************************REASSIGN USER************************************////
        List<SubmitFieldDTO> submitFieldDTOs = new ArrayList<>();
        SubmitFieldDTO submitFieldDTO = SubmitFieldDTO.builder().build();
        for(SubmitFieldDTO submitField : submitFieldDTOList.getSubmitFieldDTO()) {
            submitFieldDTO = SubmitFieldDTO.builder()
                    .appId(submitField.getAppId())
                    .project(submitFieldDTOList.getProject())
                    .referenceId(submitFieldDTOList.getReference_id())
                    .transactionId(submitFieldDTOList.getTransaction_id())
                    .projectAuto("SUBMITFIELD")
                    .phoneConfirmed(submitField.getPhoneConfirmed())
                    .resultHomeVisit(submitField.getResultHomeVisit())
                    .resultOfficeVisit(submitField.getResultOfficeVisit())
                    .result2ndHomeVisit(submitField.getResult2ndHomeVisit())
                    .timeOfVisit(submitField.getTimeOfVisit())
                    .verificationDate(submitField.getVerificationDate())
                    .remarksDecisionFiv(submitField.getRemarksDecisionFiv())
                    .remarksDecisionFic(submitField.getResonDecisionFic())
                    .resonDecisionFic(submitField.getResonDecisionFic())
                    .attachmentField(submitField.getAttachmentField())
                    .build();
            submitFieldDTOs.add(submitFieldDTO);
        }
        map.put("submitFieldList", submitFieldDTO);
        return map;
    }

    public static Map<String, Object> getDataFromCRM_QLWithCustID(CRM_ExistingCustomerDTO existingCustomerDTOList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("QuickLeadAppWithCustIDList", existingCustomerDTOList);
        return map;
    }

    public static Map<String, Object> getDataFrom_Existing_Customer(CRM_ExistingCustomerDTO existingCustomerDTOList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("ExistingCustomerList", existingCustomerDTOList);

        ////********************************APPLICATIONINFORMATION************************************////
        ////*********PERSONALINFORMATION
        ////*********PERSONALINFO
        CRM_PersonalInfoDTO personalInfos = CRM_PersonalInfoDTO.builder()
                .firstName(existingCustomerDTOList.getFullInfoApp().getFirstName())
                .middleName(existingCustomerDTOList.getFullInfoApp().getMiddleName())
                .lastName(existingCustomerDTOList.getFullInfoApp().getLastName())
                .gender(existingCustomerDTOList.getFullInfoApp().getGender())
                .dateOfBirth(existingCustomerDTOList.getFullInfoApp().getDateOfBirth())
                .nationality("Vietnamese")
                .maritalStatus(existingCustomerDTOList.getFullInfoApp().getMaritalStatus())
                .build();

        ////*********IDENTIFICATION
        //IDENTIFICATION
        List<CRM_IdentificationsListDTO> identificationsDTOs = new ArrayList<>();
        for (CRM_IdentificationsDTO identifications : existingCustomerDTOList.getFullInfoApp().getIdentifications()) {
            CRM_IdentificationsListDTO identificationsDTO = CRM_IdentificationsListDTO.builder()
                    .identificationType(identifications.getIdentificationType())
                    .identificationNumber(identifications.getIdentificationNumber())
                    .issuingCountry(identifications.getIssuingCountry())
                    .placeOfIssue(identifications.getPlaceOfIssue())
                    .issueDate(identifications.getIssueDate())
                    .expiryDate(identifications.getExpiryDate())
                    .build();
            identificationsDTOs.add(identificationsDTO);
        }

        ////*********ADDRESS
        List<CRM_AddressListDTO> addressDTOs = new ArrayList<>();
        if (existingCustomerDTOList.getFullInfoApp().getAddresses() != null){
            for (CRM_AddressDTO address : existingCustomerDTOList.getFullInfoApp().getAddresses()) {
                CRM_AddressListDTO addressDTO = CRM_AddressListDTO.builder()
                        .addressType(address.getAddressType())
                        .country("Vietnam")
                        .region("ALL")
                        .city(address.getCity())
                        .area(address.getArea())
                        .building(address.getAddressLine1())
                        .house(address.getAddressLine2())
                        .ward(address.getAddressLine3())
                        .addressLandmark(address.getLandMark())
                        .residentDurationYear(address.getYearsInCurrentAddress())
                        .residentDurationMonth(address.getMonthsInCurrentAddress())
                        .mobilePhone(address.getPhoneNumber())
                        .build();
                addressDTOs.add(addressDTO);
            }
        }


        ////*********COMMUNICATIONDETAILS
        CRM_CommunicationDetailsDTO communicationDetails = CRM_CommunicationDetailsDTO.builder()
                .primaryAddress(existingCustomerDTOList.getFullInfoApp().getPrimaryAddress())
                .phoneNumbers(existingCustomerDTOList.getFullInfoApp().getPhoneNumber())
                .build();

        //Family
        List<CRM_FamilyListDTO> familyDTOs = new ArrayList<>();
        for (CRM_FamilyDTO family : existingCustomerDTOList.getFullInfoApp().getFamily()) {
            CRM_FamilyListDTO familyDTO = CRM_FamilyListDTO.builder()
                    .relationship(family.getRelationship())
                    .memberName(family.getMemberName())
                    .phoneNumber(family.getPhoneNumber())
                    .build();
            familyDTOs.add(familyDTO);
        }

        //Employment Detail
        CRM_EmploymentDetailsDTO employmentDetails = CRM_EmploymentDetailsDTO.builder()
                .occupationType("Others")
                .isMajorEmployment("Others")
                .natureOfOccupation("Commercial")
//                .natureOfOccupation(existingCustomerDTOList.getFullInfoApp().getNatureOfOccupation()!=null?existingCustomerDTOList.getFullInfoApp().getNatureOfOccupation():"Unemployed")
                .remarks(existingCustomerDTOList.getFullInfoApp().getEmploymentName())
                .build();


        //financialDetails
        CRM_FinancialDetailsDTO financialDetails = CRM_FinancialDetailsDTO.builder()
                .incomeExpense(existingCustomerDTOList.getFullInfoApp().getIncomeExpense())
                .frequency("Monthly")
                .amount(existingCustomerDTOList.getFullInfoApp().getAmount())
                .modeOfPayment(existingCustomerDTOList.getFullInfoApp().getModeOfPayment())
                .dayOfSalaryPayment(existingCustomerDTOList.getFullInfoApp().getDayOfSalaryPayment())
                .build();


        CRM_ApplicationInformationsListDTO applicationInfoDTO = CRM_ApplicationInformationsListDTO.builder()
                .personalInfo(personalInfos)
                .identification(identificationsDTOs)
                .address(addressDTOs)
                .family(familyDTOs)
                .communicationDetail(communicationDetails)
                .employmentDetail(employmentDetails)
                .financialDetail(financialDetails)
                .build();
        map.put("ApplicationInfoDTO", applicationInfoDTO);
        ////********************************END APPLICATIONINFO DTO************************************////

        ////******************************** LOAN DETAIL DTO************************************////
        ////*********LOANDETAILS
        CRM_SourcingDetailsDTO sourcingDetails = CRM_SourcingDetailsDTO.builder()
                .sourcingBranch(existingCustomerDTOList.getFullInfoApp().getSourcingBranch())
                .sourcingChannel(existingCustomerDTOList.getFullInfoApp().getSourcingChannel())
                .loanApplicationType(existingCustomerDTOList.getFullInfoApp().getLoanApplicationType())
                .productCode(existingCustomerDTOList.getFullInfoApp().getProductCode())
                .schemeCode(existingCustomerDTOList.getFullInfoApp().getSchemeCode())
                .loanAmountRequested(existingCustomerDTOList.getFullInfoApp().getLoanAmountRequested())
                .requestedTenure(existingCustomerDTOList.getFullInfoApp().getRequestedTenure())
                .interestRate(existingCustomerDTOList.getFullInfoApp().getInterestRate())
                .saleAgentCode(existingCustomerDTOList.getFullInfoApp().getSaleAgentCodeLoanDetails())
                .build();

        CRM_VapDetailsDTO vapDetails = CRM_VapDetailsDTO.builder()
                .vapProduct(existingCustomerDTOList.getFullInfoApp().getVapProduct())
                .vapTreatment(existingCustomerDTOList.getFullInfoApp().getVapTreatment())
                .insuranceCompany(existingCustomerDTOList.getFullInfoApp().getInsuranceCompany())
                .build();

        CRM_LoanDetailsDTO loanDetails = CRM_LoanDetailsDTO.builder()
                .sourcingDetails(sourcingDetails)
                .vapDetails(vapDetails)
                .build();
        map.put("VapDetailsDTO", loanDetails);


        ////********************************END LOAN DETAIL DTO************************************////

        ///******************************** REFERENCE DTO ************************************////

        List<CRM_ReferencesListDTO> referenceDTOs = new ArrayList<>();
        for(CRM_ReferencesDTO reference : existingCustomerDTOList.getFullInfoApp().getReferences()) {
            CRM_ReferencesListDTO referenceDTO = CRM_ReferencesListDTO.builder()
                    .fullName(reference.getName())
                    .relationShip(reference.getRelationship())
                    .mobilePhoneNumber(reference.getPhoneNumber())
                    .build();
            referenceDTOs.add(referenceDTO);
        }
        map.put("ReferenceDTO", referenceDTOs);
        ////********************************END REFERENCE DTO************************************////

        //------------------------- UPDATE SET BANKCCARD THEM SCHEME - 24-09-20 -----------------

        if(existingCustomerDTOList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu01_gold")||
                existingCustomerDTOList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu02_titan") ||
                existingCustomerDTOList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu03_silver") ||
                existingCustomerDTOList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu04_brown") ||
                existingCustomerDTOList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu05_blue"))
        {
            existingCustomerDTOList.getFullInfoApp().setNewBankCardNumber("5555555555555555");
        }

        //------------------------- END ---------------------------------------------------------

        ////******************************** DYNAMIC FORM ************************************////
        CRM_DynamicFormDTO dynamicForms = CRM_DynamicFormDTO.builder()
                .loanPurpose(existingCustomerDTOList.getFullInfoApp().getLoanPurpose())
                .loanPurposeOther(existingCustomerDTOList.getFullInfoApp().getLoanPurposeOther())
                .numberOfDependents(existingCustomerDTOList.getFullInfoApp().getNumberOfDependents())
                .monthlyRental(existingCustomerDTOList.getFullInfoApp().getMonthlyRental())
                .houseOwnership(existingCustomerDTOList.getFullInfoApp().getHouseOwnership())
                .newBankCardNumber(existingCustomerDTOList.getFullInfoApp().getNewBankCardNumber())
                .saleAgentCode(existingCustomerDTOList.getFullInfoApp().getSaleAgentCodeDynamicForm())
                .courierCode(existingCustomerDTOList.getFullInfoApp().getCourierCode())
                .maximumInterestedRate(existingCustomerDTOList.getFullInfoApp().getMaximumInterestedRate())
                .remark(existingCustomerDTOList.getFullInfoApp().getRemarksDynamicForm())
                .build();
        map.put("MiscFrmAppDtlDTO", dynamicForms);

        ////********************************ENDDYNAMIC FORM************************************////

        ////******************************** DOCUMENT DTO ************************************////
        List<CRM_DocumentsDTO> documentDTOS = new ArrayList<>();
        if(existingCustomerDTOList.getFullInfoApp().getDocuments()!=null && existingCustomerDTOList.getFullInfoApp().getDocuments().size()>0){
            for(CRM_DocumentsDTO document : existingCustomerDTOList.getFullInfoApp().getDocuments()) {
                CRM_DocumentsDTO documentDTO = CRM_DocumentsDTO.builder ()
                        .originalname(document.getOriginalname())
                        .filename(document.getFilename())
                        .type(document.getType()).build();
                documentDTOS.add(documentDTO);
            }
        }
        map.put("DocumentDTO", documentDTOS);
        ////********************************END DOCUMENT DTO************************************////

        return map;
    }

    public static Map<String, Object> getDataFrom_Sale_Queue_FullInfo(CRM_SaleQueueDTO saleQueueList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("SaleQueueList", saleQueueList);

        ////********************************APPLICATIONINFORMATION************************************////
        ////*********PERSONALINFORMATION
        ////*********PERSONALINFO
        CRM_PersonalInfoDTO personalInfos = CRM_PersonalInfoDTO.builder()
                .firstName(saleQueueList.getFullInfoApp().getFirstName())
                .middleName(saleQueueList.getFullInfoApp().getMiddleName())
                .lastName(saleQueueList.getFullInfoApp().getLastName())
                .gender(saleQueueList.getFullInfoApp().getGender())
                .dateOfBirth(saleQueueList.getFullInfoApp().getDateOfBirth())
                .nationality("Vietnamese")
                .maritalStatus(saleQueueList.getFullInfoApp().getMaritalStatus())
                .build();

        ////*********IDENTIFICATION
        //IDENTIFICATION
        List<CRM_IdentificationsListDTO> identificationsDTOs = new ArrayList<>();
        for (CRM_IdentificationsDTO identifications : saleQueueList.getFullInfoApp().getIdentifications()) {
            CRM_IdentificationsListDTO identificationsDTO = CRM_IdentificationsListDTO.builder()
                    .identificationType(identifications.getIdentificationType())
                    .identificationNumber(identifications.getIdentificationNumber())
                    .issuingCountry(identifications.getIssuingCountry())
                    .placeOfIssue(identifications.getPlaceOfIssue())
                    .issueDate(identifications.getIssueDate())
                    .expiryDate(identifications.getExpiryDate())
                    .build();
            identificationsDTOs.add(identificationsDTO);
        }

        ////*********ADDRESS
        List<CRM_AddressListDTO> addressDTOs = new ArrayList<>();
        if (saleQueueList.getFullInfoApp().getAddresses() != null){
            for (CRM_AddressDTO address : saleQueueList.getFullInfoApp().getAddresses()) {
                CRM_AddressListDTO addressDTO = CRM_AddressListDTO.builder()
                        .addressType(address.getAddressType())
                        .country("Vietnam")
                        .region("ALL")
                        .city(address.getCity())
                        .area(address.getArea())
                        .building(address.getAddressLine1())
                        .house(address.getAddressLine2())
                        .ward(address.getAddressLine3())
                        .addressLandmark(address.getLandMark())
                        .residentDurationYear(address.getYearsInCurrentAddress())
                        .residentDurationMonth(address.getMonthsInCurrentAddress())
                        .mobilePhone(address.getPhoneNumber())
                        .build();
                addressDTOs.add(addressDTO);
            }
        }


        ////*********COMMUNICATIONDETAILS
        CRM_CommunicationDetailsDTO communicationDetails = CRM_CommunicationDetailsDTO.builder()
                .primaryAddress(saleQueueList.getFullInfoApp().getPrimaryAddress())
                .phoneNumbers(saleQueueList.getFullInfoApp().getPhoneNumber())
                .build();

        //Family
        List<CRM_FamilyListDTO> familyDTOs = new ArrayList<>();
        for (CRM_FamilyDTO family : saleQueueList.getFullInfoApp().getFamily()) {
            CRM_FamilyListDTO familyDTO = CRM_FamilyListDTO.builder()
                    .relationship(family.getRelationship())
                    .memberName(family.getMemberName())
                    .phoneNumber(family.getPhoneNumber())
                    .build();
            familyDTOs.add(familyDTO);
        }

        //Employment Detail
        CRM_EmploymentDetailsDTO employmentDetails = CRM_EmploymentDetailsDTO.builder()
                .occupationType("Others")
                .isMajorEmployment("Others")
//                .natureOfOccupation(saleQueueList.getFullInfoApp().getNatureOfOccupation()!=null?saleQueueList.getFullInfoApp().getNatureOfOccupation():"Unemployed")
                .natureOfOccupation("Commercial")
                .remarks(saleQueueList.getFullInfoApp().getEmploymentName())
                .build();


        //financialDetails
        CRM_FinancialDetailsDTO financialDetails = CRM_FinancialDetailsDTO.builder()
                .incomeExpense(saleQueueList.getFullInfoApp().getIncomeExpense())
                .frequency("Monthly")
                .amount(saleQueueList.getFullInfoApp().getAmount())
                .modeOfPayment(saleQueueList.getFullInfoApp().getModeOfPayment())
                .dayOfSalaryPayment(saleQueueList.getFullInfoApp().getDayOfSalaryPayment())
                .build();


        CRM_ApplicationInformationsListDTO applicationInfoDTO = CRM_ApplicationInformationsListDTO.builder()
                .personalInfo(personalInfos)
                .identification(identificationsDTOs)
                .address(addressDTOs)
                .family(familyDTOs)
                .communicationDetail(communicationDetails)
                .employmentDetail(employmentDetails)
                .financialDetail(financialDetails)
                .build();
        map.put("ApplicationInfoDTO", applicationInfoDTO);
        ////********************************END APPLICATIONINFO DTO************************************////

        ////******************************** LOAN DETAIL DTO************************************////
        ////*********LOANDETAILS
        CRM_SourcingDetailsDTO sourcingDetails = CRM_SourcingDetailsDTO.builder()
                .sourcingBranch(saleQueueList.getFullInfoApp().getSourcingBranch())
                .sourcingChannel(saleQueueList.getFullInfoApp().getSourcingChannel())
                .loanApplicationType(saleQueueList.getFullInfoApp().getLoanApplicationType())
                .productCode(saleQueueList.getFullInfoApp().getProductCode())
                .schemeCode(saleQueueList.getFullInfoApp().getSchemeCode())
                .loanAmountRequested(saleQueueList.getFullInfoApp().getLoanAmountRequested())
                .requestedTenure(saleQueueList.getFullInfoApp().getRequestedTenure())
                .interestRate(saleQueueList.getFullInfoApp().getInterestRate())
                .saleAgentCode(saleQueueList.getFullInfoApp().getSaleAgentCodeLoanDetails())
                .build();

        CRM_VapDetailsDTO vapDetails = CRM_VapDetailsDTO.builder()
                .vapProduct(saleQueueList.getFullInfoApp().getVapProduct())
                .vapTreatment(saleQueueList.getFullInfoApp().getVapTreatment())
                .insuranceCompany(saleQueueList.getFullInfoApp().getInsuranceCompany())
                .build();

        CRM_LoanDetailsDTO loanDetails = CRM_LoanDetailsDTO.builder()
                .sourcingDetails(sourcingDetails)
                .vapDetails(vapDetails)
                .build();
        map.put("VapDetailsDTO", loanDetails);


        ////********************************END LOAN DETAIL DTO************************************////

        ///******************************** REFERENCE DTO ************************************////

        List<CRM_ReferencesListDTO> referenceDTOs = new ArrayList<>();
        for(CRM_ReferencesDTO reference : saleQueueList.getFullInfoApp().getReferences()) {
            CRM_ReferencesListDTO referenceDTO = CRM_ReferencesListDTO.builder()
                    .fullName(reference.getName())
                    .relationShip(reference.getRelationship())
                    .mobilePhoneNumber(reference.getPhoneNumber())
                    .build();
            referenceDTOs.add(referenceDTO);
        }
        map.put("ReferenceDTO", referenceDTOs);
        ////********************************END REFERENCE DTO************************************////

        //------------------------- UPDATE SET BANKCCARD THEM SCHEME - 24-09-20 -----------------

        if(saleQueueList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu01_gold")||
                saleQueueList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu02_titan") ||
                saleQueueList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu03_silver") ||
                saleQueueList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu04_brown") ||
                saleQueueList.getFullInfoApp().getSchemeCode().toLowerCase().equals("tu05_blue"))
        {
            saleQueueList.getFullInfoApp().setNewBankCardNumber("5555555555555555");
        }

        //------------------------- END ---------------------------------------------------------

        ////******************************** DYNAMIC FORM ************************************////
        CRM_DynamicFormDTO dynamicForms = CRM_DynamicFormDTO.builder()
                .loanPurpose(saleQueueList.getFullInfoApp().getLoanPurpose())
                .loanPurposeOther(saleQueueList.getFullInfoApp().getLoanPurposeOther())
                .numberOfDependents(saleQueueList.getFullInfoApp().getNumberOfDependents())
                .monthlyRental(saleQueueList.getFullInfoApp().getMonthlyRental())
                .houseOwnership(saleQueueList.getFullInfoApp().getHouseOwnership())
                .newBankCardNumber(saleQueueList.getFullInfoApp().getNewBankCardNumber())
                .saleAgentCode(saleQueueList.getFullInfoApp().getSaleAgentCodeDynamicForm())
                .courierCode(saleQueueList.getFullInfoApp().getCourierCode())
                .maximumInterestedRate(saleQueueList.getFullInfoApp().getMaximumInterestedRate())
                .remark(saleQueueList.getFullInfoApp().getRemarksDynamicForm())
                .build();
        map.put("MiscFrmAppDtlDTO", dynamicForms);

        ////********************************ENDDYNAMIC FORM************************************////

        ////******************************** DOCUMENT DTO ************************************////
        List<CRM_DocumentsDTO> documentDTOS = new ArrayList<>();
        if(saleQueueList.getFullInfoApp().getDocuments()!=null && saleQueueList.getFullInfoApp().getDocuments().size()>0){
            for(CRM_DocumentsDTO document : saleQueueList.getFullInfoApp().getDocuments()) {
                CRM_DocumentsDTO documentDTO = CRM_DocumentsDTO.builder ()
                        .originalname(document.getOriginalname())
                        .filename(document.getFilename())
                        .type(document.getType()).build();
                documentDTOS.add(documentDTO);
            }
        }
        map.put("DocumentDTO", documentDTOS);
        ////********************************END DOCUMENT DTO************************************////

        return map;
    }

    public static Map<String, Object> getDataFromCRM_QL(CRM_ExistingCustomerDTO application) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("ApplicationDTO", application);
        return map;
    }

    public static Map<String, Object> getDataFrom_Auto_Allocation(AutoAllocationDTO autoAllocationDTOList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("AutoAllocationList", autoAllocationDTOList);

        ////********************************REASSIGN USER************************************////
        List<AutoReassignUserDTO> autoReassignUserDTOs = new ArrayList<>();
        for(AutoReassignUserDTO autoReassignUser : autoAllocationDTOList.getReassign()) {
            AutoReassignUserDTO autoReassignUserDTO = AutoReassignUserDTO.builder()
                    .project(autoAllocationDTOList.getProject())
                    .reference_id(autoAllocationDTOList.getReference_id())
                    .amountApp(autoReassignUser.getAmountApp())
                    .stageApp(autoReassignUser.getStageApp())
                    .inQueueStatus(autoReassignUser.getInQueueStatus())
                    .reassignUser(autoReassignUser.getReassignUser())
                    .build();
            autoReassignUserDTOs.add(autoReassignUserDTO);
        }
        map.put("AutoReassignUser", autoReassignUserDTOs);

        return map;
    }

    public static Map<String, Object> getDataFrom_AutoAssign_Allocation(AutoAllocationDTO autoAssignAllocationDTOList) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("AutoAssignAllocationList", autoAssignAllocationDTOList);
        ////********************************REASSIGN USER************************************////
        List<AutoAssignAllocationDTO> autoAssignAllocationDTOs = new ArrayList<>();
        for(AutoAssignAllocationDTO autoAssignAllocation : autoAssignAllocationDTOList.getAutoAssign()) {
            AutoAssignAllocationDTO autoAssignAllocationDTO = AutoAssignAllocationDTO.builder()
                    .project(autoAssignAllocationDTOList.getProject())
                    .appId(autoAssignAllocation.getAppId())
                    .userName(autoAssignAllocation.getUserName())
                    .build();
            autoAssignAllocationDTOs.add(autoAssignAllocationDTO);
        }
        map.put("AutoAssignAllocation", autoAssignAllocationDTOs);
        return map;
    }
}
