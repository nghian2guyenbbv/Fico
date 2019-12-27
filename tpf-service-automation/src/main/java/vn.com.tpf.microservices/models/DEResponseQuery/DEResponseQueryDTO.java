package vn.com.tpf.microservices.models.DEResponseQuery;


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
@Document(collection="automation_autoassign")
public class DEResponseQueryDTO {
    @Id
    private String id;

    private String appid;
    private String username;
    private String userauto;
    private int status=0;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;

    private List<DEResponseQueryDocumentDTO> document;
}
