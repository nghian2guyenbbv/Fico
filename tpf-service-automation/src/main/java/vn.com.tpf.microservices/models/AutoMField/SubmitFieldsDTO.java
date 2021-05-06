package vn.com.tpf.microservices.models.AutoMField;

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
public class SubmitFieldsDTO {
    @Id
    private String id;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;

    private String applicationId;
    private String project;
    private String referenceId;
    private String transactionId;

    private String phoneConfirmed;
    private String resultHomeVisit;
    private String resultOfficeVisit;
    private String result2ndHomeVisit;
    private List<SubmitFieldDocumentDTO> attachmentField;
    private String noOfAttempts;
    private String verificationAgent;
    private String resultDecisionFiv;
    private String remarksDecisionFiv;
    private String decisionFic;
    private String resonDecisionFic;
    private String remarksDecisionFic;
    private String timeOfVisit;
    private String verificationDate;
    private int status=0;

}
