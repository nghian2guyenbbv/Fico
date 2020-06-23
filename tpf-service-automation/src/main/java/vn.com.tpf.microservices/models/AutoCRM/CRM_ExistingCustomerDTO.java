package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class CRM_ExistingCustomerDTO {
    @Id
    private String id;
    private int status=0;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastDate;

    private String neoCustID;
    private String cifNumber;
    private String idNumber;
    private String reference_id;
    private String project;
    private String transaction_id;
    private String automation_result;
    private String comment;

    public CRM_ApplicationInformationsDTO applicationInformation;
    public CRM_LoanDetailsDTO loanDetails;
    public List<CRM_DocumentsDTO> documents = null;
    public List<CRM_ReferencesDTO> references = null;
    public List<CRM_DynamicFormDTO> dynamicForm = null;

}
