package vn.com.tpf.microservices.models.Automation;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection="automation_application")
public class Application {
    private String applicationId;
    private String quickLeadId;

    public ApplicationInformation applicationInformation;
    public LoanDetails loanDetails;
    public List<Reference> references = null;
    public List<DynamicForm> dynamicForm = null;
    public List<vn.com.tpf.microservices.models.Automation.Document> documents = null;
    private String status;
    private String description;
    private String stage;
    private String error;
    private String reference_id;
}
