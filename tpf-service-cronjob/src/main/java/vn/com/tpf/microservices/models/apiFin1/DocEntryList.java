package vn.com.tpf.microservices.models.apiFin1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocEntryList {
    private String storedDocumentRefId;
}
