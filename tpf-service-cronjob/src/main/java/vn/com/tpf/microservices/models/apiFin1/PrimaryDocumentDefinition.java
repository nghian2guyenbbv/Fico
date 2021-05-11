package vn.com.tpf.microservices.models.apiFin1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrimaryDocumentDefinition {
    private String name;
    private String description;
    private String documentType;
    private String documentClassificationType;
    private boolean validityRequired;
    private boolean verificationRequired;
}
