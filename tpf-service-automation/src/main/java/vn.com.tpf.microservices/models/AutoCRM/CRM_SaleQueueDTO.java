package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection="automation_crm")
public class CRM_SaleQueueDTO {
    private String appId;
    private String userAuto;
    private String status;
    private String userCreatedSalesQueue;
    private String commentText;

    private String reference_id;
    private String project;
    private String transaction_id;
    private String stage;
    private String error;

    private CRM_FullInfoAppDTO fullInfoApp;
}
