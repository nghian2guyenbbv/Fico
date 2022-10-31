package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "dataentry")
public class Application {
    @Id
    private String id;

    private String applicationId;
    private String quickLeadId;
    private QuickLead quickLead;

    @Valid
    private ApplicationInformation applicationInformation;
    @Valid
    private LoanDetails loanDetails;
    @Valid
    private List<Reference> references;
    @Valid
    private List<DynamicForm> dynamicForm;

    private List<CommentModel> comment;
    private List<QLDocument> documents;

    private String status;
    private String description;
    private String stage;
    private String error;
    private String userName;
    private String userName_DE;
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastModifiedDate;

    private String partnerId;
    private String partnerName;
}
