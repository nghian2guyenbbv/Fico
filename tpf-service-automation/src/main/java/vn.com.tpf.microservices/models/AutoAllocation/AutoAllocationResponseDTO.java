package vn.com.tpf.microservices.models.AutoAllocation;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection="automation_mobility_field")
public class AutoAllocationResponseDTO {
    private List<String> appId = null;
    private String amountApp;
    private String amountRemainingApp;
    private String reassignUser;
    private String automation_result;
    private String automation_result_message;
}
