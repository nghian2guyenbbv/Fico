package vn.com.tpf.microservices.models.QuickLead;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Application {
    private String applicationId;
    private UUID quickLeadId;
    private QuickLead quickLead;
}
