package vn.com.tpf.microservices.models.DEReturn;


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
@Document(collection="sale_queue")
public class DESaleQueueDTO {
    @Id
    private String id;

    private String appId;
    private String userName;
    private String userauto;
    private int status=0;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;
    private String lastupdate;
    private int checkACCA = 0;

    private List<DESaleQueueDocumentDTO> datadocument;
    private String commentText;
    private String reference_id;
}
