package vn.com.tpf.microservices.models.AutoAllocation;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="automation_allocation")
public class AutoAllocationResponseDTO {
    private String idAutoAllocation;
    private String appId;
    private String userName;
    private String userAuto;
    private String automationResult;
    private String automationResultMessage;
}
