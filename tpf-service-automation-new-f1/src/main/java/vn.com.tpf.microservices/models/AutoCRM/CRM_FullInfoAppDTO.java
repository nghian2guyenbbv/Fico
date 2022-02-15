package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Data;
import vn.com.tpf.microservices.models.Identification;

import java.util.List;

@Data
public class CRM_FullInfoAppDTO {
    //---applicationInformation
    //personalInformation
    public String firstName;
    public String middleName;
    public String lastName;
    public String fullName;
    public String gender;
    public String dateOfBirth;
    public String nationality;
    public String maritalStatus;
    public String customerCategoryCode;
    public String issuePlace;

    //identifications
    public List<CRM_IdentificationsDTO> identifications = null;
    public List<CRM_IdentificationsDTO> identificationNews = null;

    //addresses
    public List<CRM_AddressDTO> addresses = null;

    //communicationDetails
    public String primaryAddress;
    public String phoneNumber;

    //Family
    public List<CRM_FamilyDTO> family = null;

    //financialDetails
    public String incomeExpense;
    public String amount;
    public String modeOfPayment;
    public String dayOfSalaryPayment;

    //Employment
    public String occupationType;
    public String isMajorEmployment;
    public String natureOfOccupation;
    public String employmentName;

    //---loanDetails
    //sourcingDetails
    public String sourcingChannel;
    public String sourcingBranch;
    public String applicationNumber;
    public String loanApplicationType;
    public String productCode;
    public String schemeCode;
    public String loanAmountRequested;
    public String requestedTenure;
    public String interestRate;
    public String loanPurposeDesc;
    public String saleAgentCodeLoanDetails;
    //vapDetails
    public String vapProduct;
    public String vapTreatment;
    public String insuranceCompany;

    //references
    public List<CRM_ReferencesDTO> references = null;

    //--dynamicForm
    //frmAppDtl
    public String loanPurpose;
    public String loanPurposeOther;
    public String numberOfDependents;
    public String monthlyRental;
    public String houseOwnership;
    public String newBankCardNumber;
    public String saleAgentCodeDynamicForm;
    public String courierCode;
    public String maximumInterestedRate;
    public String zalo;
    public String companyName;
    public String contractNumber;
    public String monthlyFee;
    public String otherLoanPurposeDetail;
    public String loanAtWork;
    public String internalCode;
    public String totalMonthlyPayable;
    public String remarksDynamicForm;
    public String oldContractLoanAmount;

    //--Document
    public List<CRM_DocumentsDTO> documents = null;

}
