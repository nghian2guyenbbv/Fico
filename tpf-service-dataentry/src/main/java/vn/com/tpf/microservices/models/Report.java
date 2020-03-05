package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "dataentry_report")
public class Report {
    @Id
    private String id;
    private String quickLeadId;
    private String applicationId;
    private String status;
    private String description;
    private String function;
    private String createdBy;
    private String processBy;
    private Date createdDate;

    private List<Application> applications;

    private QuickLead quickLead;
    private ApplicationInformation applicationInformation;
    private LoanDetails loanDetails;
    private List<Reference> references;
    private List<DynamicForm> dynamicForm;
    private List<CommentModel> comment;
    private List<QLDocument> documents;

    private String fullName;
    private String identificationNumber;
    private String identificationNumberFull;
    private long duration;

    private String commentDescription;
    private String branch;
    private String lastName;
    private String firstName;
    private String sourcingBranch;

    private String partnerId;
    private String partnerName;

}
