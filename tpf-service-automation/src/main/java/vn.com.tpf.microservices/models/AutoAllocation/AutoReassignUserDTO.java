package vn.com.tpf.microservices.models.AutoAllocation;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection="automation_allocation")
public class AutoReassignUserDTO {
    @Id
    private String id;
    private String userAuto;
    private int status = 0;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;

    private String reference_id;
    private String project;
    private String stageApp;
    private String inQueueStatus;
    private String amountApp;
    private String amountReassignedApp;
    private String reassignUser;
    private List<String> appId = null;
    private String automation_result;
    private String automation_result_message;
}
