package vn.com.tpf.microservices.models.AutoReturnQuery;


import lombok.Data;

import java.util.List;

@Data
public class SaleQueueDetails {

    public String appId;
    public String project;
    public String transaction_id;
    public String commentText;
    public List<SaleQueueDocumentDTO> dataDocuments;
    public String reference_id;
    public String userCreatedSalesQueue;
}
