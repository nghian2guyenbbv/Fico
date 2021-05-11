package vn.com.tpf.microservices.models.apiFin1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LendingDocumentMoList {
    private PrimaryDocumentDefinition primaryDocumentDefinition;
    private boolean mandatory;
    private String forEntityName;
    private int forEntityId;
    private String referenceType;
    private String applicantBusinessPartnerType;
    private String stage;
    private boolean originalRequired;
    private int lendingRefernceId;
    private boolean documentGroup;
    private List<DocEntryList> docEntryList;
    private String receiveState;
    private int orderNumber;
}
