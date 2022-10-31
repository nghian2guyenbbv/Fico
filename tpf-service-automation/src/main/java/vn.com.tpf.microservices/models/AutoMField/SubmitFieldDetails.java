package vn.com.tpf.microservices.models.AutoMField;


import lombok.Data;

import java.util.List;

@Data
public class SubmitFieldDetails {
    public String appId;
    public String phoneConfirmed;
    public String resultHomeVisit;
    public String resultOfficeVisit;
    public String result2ndHomeVisit;
    public List<SubmitFieldDocumentDTO> attachmentField;
    public String timeOfVisit;
    public String verificationDate;
    public String remarksDecisionFiv;
    public String remarksDecisionFic;
    public String resonDecisionFic;
}
