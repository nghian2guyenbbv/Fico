package vn.com.tpf.microservices.models.AutoReturnQuery;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection="automation_return_query")
public class SaleQueueDTO {

    @Id
    private String id;
    private String appId;
    private String userAuto;
    private String status;
    private String userCreatedSalesQueue;

    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;
    private String lastUpdate;
    private int checkACCA = 0;

    private List<SaleQueueDocumentDTO> dataDocuments;

    private String commentText;
    private String reference_id;
    private String project;
    private String transaction_id;
}
