package vn.com.tpf.microservices.models.AutoQuickLead;

import lombok.Data;

import java.util.List;

@Data
public class QuickLeadDetails {

    public String quickLeadId;
    public String identificationNumber;
    public String productTypeCode;
    public String customerType;
    public String productCode;
    public String loanAmountRequested;
    public String firstName;
    public String lastName;
    public String city;
    public String sourcingChannel;
    public String alternateChannelMode;
    public String dateOfBirth;
    public String sourcingBranch;
    public String natureOfOccupation;
    public String schemeCode;
    public String comment;
    public String preferredModeOfCommunication;
    public String leadStatus;
    public String communicationTranscript;
    public String ememployeeName;
    public String ememployeeNumber;
    public List<DocumentDTO> documents = null;
}
