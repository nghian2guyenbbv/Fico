package vn.com.tpf.microservices.models.AutoReturnQuery;

import lombok.Data;

@Data
public class SaleQueueDocumentDTO {
    private String documentName;
    private String statusDocument;
    private String fileName;
}
