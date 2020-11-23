package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.com.tpf.microservices.models.QuickLead.QuickLead;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection="automation_application")
public class CRM_ExistingCustomerDTO {
    @Id
    private String id;
    private String status;
    private String stage;
    private String error;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;

    private String quickLeadId;
    private String appId;
    private String neoCustID;
    private String cifNumber;
    private String idNumber;
    private String reference_id;
    private String project;
    private String transaction_id;
    private String automation_result;
    private String comment;
    private CRM_QuickLeadDTO quickLead;
    private String leadApp;
    private String automationAcc;
    private String description;
    private CRM_FullInfoAppDTO fullInfoApp;

}
