package vn.com.tpf.microservices.models.DEReturn;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
//@Document(collection="automation_smartnet")
public class DESaleQueueDTO {
    private String appId;
    private String userName;
    private int status=0;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;
    private String lastUpdate;
    private int checkACCA = 0;

    private List<DESaleQueueDocumentDTO> dataDocuments;

    private String commentText;
    private String reference_id;
    private String project;
    private String transaction_id;
}
