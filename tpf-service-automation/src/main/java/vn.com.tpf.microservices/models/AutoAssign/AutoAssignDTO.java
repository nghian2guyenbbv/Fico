package vn.com.tpf.microservices.models.AutoAssign;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection="automation_autoassign")
public class AutoAssignDTO {
    @Id
    private String id;

    private String appid;
    private String username;
    private String userauto;
    private int status=0;
    private Date createDate;
}
