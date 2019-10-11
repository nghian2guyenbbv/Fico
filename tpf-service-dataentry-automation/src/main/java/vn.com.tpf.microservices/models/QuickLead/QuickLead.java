package vn.com.tpf.microservices.models.QuickLead;

import lombok.Data;

import java.util.List;

@Data
public class QuickLead {
    public String productTypeCode;
    public String customerType;
    public String productCode;
    public String loanAmountRequested;
    public String firstName;
    public String lastName;
    public String city;
    public String sourcingChannel;
    public String dateOfBirth;
    public String sourcingBranch;
    public String natureOfOccupation;
    public String schemeCode;
    public String comment;
    public String preferredModeOfCommunication;
    public String leadStatus;
    public String communicationTranscript;
    public List<QL_Document> documents = null;
}
