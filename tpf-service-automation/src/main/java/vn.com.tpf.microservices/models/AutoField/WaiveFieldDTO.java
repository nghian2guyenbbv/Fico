package vn.com.tpf.microservices.models.AutoField;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection="automation_field_mobility")
public class WaiveFieldDTO {
    private String appId;
    private String userName;
    private String userAuto;
    private int status=0;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;
    private String reasonCode;
    private String reference_id;
    private String project;
    private String transaction_id;
}
