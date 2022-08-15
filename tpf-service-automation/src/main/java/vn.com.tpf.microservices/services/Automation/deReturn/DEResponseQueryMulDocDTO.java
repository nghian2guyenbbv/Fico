package vn.com.tpf.microservices.services.Automation.deReturn;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDocumentDTO;

import java.util.Date;
import java.util.List;

@Data
@Builder

@Document(collection="automation_return_smartnet")
public class DEResponseQueryMulDocDTO {
    private String appId;
    private DEResponseQueryDocumentDTO dataDocument;
    private List<DEResponseQueryDocumentDTO> dataDocuments;
    private String userAuto;
    private String status;


    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;
    private String commentText;
    private String reference_id;
    private String project;
    private String transaction_id;
    private String queryName;
}