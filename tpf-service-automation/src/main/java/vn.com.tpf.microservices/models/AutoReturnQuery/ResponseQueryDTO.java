package vn.com.tpf.microservices.models.AutoReturnQuery;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection="automation_return_query")
public class ResponseQueryDTO {

    @Id
    private String id;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;

    private int status = 0;
    private String applicationId;
    private String userAuto;
    private String commentText;
    private String referenceId;
    private String project;
    private String transactionId;
    private String queryName;
    private ResponseQueryDocumentDTO dataDocument;

}
