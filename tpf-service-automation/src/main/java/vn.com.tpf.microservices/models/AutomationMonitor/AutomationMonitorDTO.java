package vn.com.tpf.microservices.models.AutomationMonitor;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection="automation_monitor")
public class AutomationMonitorDTO {
    @Id
    private String id;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastModifiedDate;


    private String project;
    private String funcAutomationa;
    private String quickLeadId;
    private String applicationId;
    private String identificationNumber;
    private String customerId;
    private String referentId;
    private String transactionId;
    private String accountAuto;
    private String status;
    private String stage;
    private String error;
    private String description;
    private int flagRetry = 0;
}
