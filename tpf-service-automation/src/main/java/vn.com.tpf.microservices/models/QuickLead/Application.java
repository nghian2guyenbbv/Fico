package vn.com.tpf.microservices.models.QuickLead;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.com.tpf.microservices.models.ApplicationInformation;
import vn.com.tpf.microservices.models.DynamicForm;
import vn.com.tpf.microservices.models.LoanDetails;
import vn.com.tpf.microservices.models.Reference;

import java.util.List;

@Data
@Builder
@Document(collection="automation_application")
public class Application {
    private String applicationId;
    private String quickLeadId;
    private QuickLead quickLead;

    public ApplicationInformation applicationInformation;
    public LoanDetails loanDetails;
    public List<Reference> references = null;
    public List<DynamicForm> dynamicForm = null;
    public List<vn.com.tpf.microservices.models.Document> documents = null;
    private String status;
    private String description;
    private String stage;
    private String error;
    private String reference_id;
}
