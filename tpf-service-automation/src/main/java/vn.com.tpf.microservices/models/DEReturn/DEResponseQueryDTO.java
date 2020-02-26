package vn.com.tpf.microservices.models.DEReturn;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@Builder
public class DEResponseQueryDTO {
    private String appId;
    private String userName;
    private int status=0;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;
    private DEResponseQueryDocumentDTO dataDocument;
    private String commentText;
    private String reference_id;
    private String project;
    private String transaction_id;
}
