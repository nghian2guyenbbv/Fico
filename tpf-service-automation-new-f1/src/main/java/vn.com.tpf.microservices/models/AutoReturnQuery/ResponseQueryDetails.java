package vn.com.tpf.microservices.models.AutoReturnQuery;

import lombok.Data;

@Data
public class ResponseQueryDetails {

    private String appId;
    private String userAuto;
    private String commentText;
    private String reference_id;
    private String project;
    private String transaction_id;
    private String queryName;
    private ResponseQueryDocumentDTO dataDocument;

}
