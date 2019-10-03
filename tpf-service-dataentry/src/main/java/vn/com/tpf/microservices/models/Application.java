package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
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
    private UUID quickLeadId;
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

    private String status;
    private String description;
    private String stage;

    private Date createdDate;
    private Date lastModifiedDate;

}
