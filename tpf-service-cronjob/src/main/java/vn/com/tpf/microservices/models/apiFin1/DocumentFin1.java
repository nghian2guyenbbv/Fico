package vn.com.tpf.microservices.models.apiFin1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentFin1 {
    private List<LendingDocumentMoList> lendlingDocumentMoList;
}
