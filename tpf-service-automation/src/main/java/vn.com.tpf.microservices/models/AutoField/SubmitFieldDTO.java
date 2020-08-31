package vn.com.tpf.microservices.models.AutoField;

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
@Document(collection="automation_mobility_field")
public class SubmitFieldDTO {
    @Id
    private String id;
    private String appId;
    private String phoneConfirmed;
    private String resultHomeVisit;
    private String resultOfficeVisit;
    private String result2ndHomeVisit;
    private List<SubmitFieldAttachmentDTO> attachmentField;
    private String noOfAttempts;
    private String verificationAgent;
    private String resultDecisionFiv;
    private String remarksDecisionFiv;
    private String decisionFic;
    private String resonDecisionFic;
    private String remarksDecisionFic;
    private String timeOfVisit;
    private String verificationDate;

    private String userName;
    private String userAuto;
    private int status=0;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;

    private String reference_id;
    private String project;
    private String transaction_id;
    private int checkUpdate = 0;

    private String stageApplication;
}
