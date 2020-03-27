package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
//@Document(collection = "dataentry")
public class QuickLead {
    private String quickLeadId;
//    private String applicationId;
    private String identificationNumber;
    private String productTypeCode;
    private String customerType;
    private String productCode;
    private String loanAmountRequested;
    private String firstName;
    private String lastName;
    private String city;
    private String sourcingChannel;
    private String dateOfBirth;
    private String sourcingBranch;
    private String natureOfOccupation;
    private String schemeCode;
    private String comment;
    private String preferredModeOfCommunication;
    private String leadStatus;
    private String communicationTranscript;
    private Date createdDate;
    private Date lastModifiedDate;
    private List<QLDocument> documents;
    private List<QLDocument> documentsComment;
    private List<QLDocument> documentsAfterSubmit;

    private String partnerId;
}