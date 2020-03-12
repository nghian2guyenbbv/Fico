package vn.com.tpf.microservices.models.FieldVerification;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class FieldInvestigationDTO {
    private String appId;
    private String phoneConfirmed;
    private String resultHomeVisit;
    private String resultOfficeVisit;
    private String result2ndHomeVisit;
    private List<FieldInvestigationAttachmentDTO> attachmentField;
    private String noOfAttempts;
    private String verificationAgent;
    private String resultDecisionFiv;
    private String remarksDecisionFiv;
    private String decisionFic;
    private String resonDecisionFic;
    private String remarksDecisionFic;
    private String timeOfVisit;
    private String verificationDate;
    private String statusField;

    private String reference_id;
    private String project;
    private String transaction_id;
}
