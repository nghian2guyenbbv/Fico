package vn.com.tpf.microservices.models.AutoMField;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection="automation_mobility_field")
public class WaiveFieldsDTO {
    @Id
    private String id;

    private String applicationId;
    private String project;
    private String projectAuto;
    private int status = 0;
    private String referenceId;
    private String transactionId;

    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;
}
