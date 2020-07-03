package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Data;

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
    public String identificationType;
    public String identificationNumber;
    public String issuingCountry;
    public String placeOfIssue;
    public String issueDate;
    public String expiryDate;

    //addresses
    public List<CRM_AddressDTO> addresses = null;

    //communicationDetails
    public String primaryAddress;
    public String phoneNumber;

    //financialDetails
    public String incomeExpense;
    public String amount;
    public String modeOfPayment;
    public String dayOfSalaryPayment;

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
    public String remark;
    public String oldContractLoanAmount;

    //--Document
    public List<CRM_DocumentsDTO> documents = null;

}
