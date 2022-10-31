package vn.com.tpf.microservices.models.AutoQuickLead;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Builder
@Document(collection="automation_application")
public class QuickLeadDTO {
    @Id
    private String id;
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastModifiedDate;

    private String applicationId;
    private String quickLeadId;
    private String leadApp;
    private String project;
    private QuickLeadDetails quickLead;
    private String reference_id;
    private String automationAcc;
    private String status;
    private String description;
    private String stage;
    private String error;

}
