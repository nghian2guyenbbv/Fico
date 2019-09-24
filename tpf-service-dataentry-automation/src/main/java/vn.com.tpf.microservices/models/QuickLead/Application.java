package vn.com.tpf.microservices.models.QuickLead;

import lombok.Builder;
import lombok.Data;
import vn.com.tpf.microservices.models.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Application {
    private String applicationId;
    private UUID quickLeadId;
    private QuickLead quickLead;

    public ApplicationInformation applicationInformation;
    public LoanDetails loanDetails;
    public List<Reference> references = null;
    public List<DynamicForm> dynamicForm = null;
    public List<Document> documents = null;
    private String status;
    private String description;
    private String stage;
}
