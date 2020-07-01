package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Data;

import java.util.List;

@Data
public class CRM_FullInfoAppDTO {
    public CRM_ApplicationInformationsDTO applicationInformation;
    public CRM_LoanDetailsDTO loanDetails;
    public List<CRM_DocumentsDTO> documents = null;
    public List<CRM_ReferencesDTO> references = null;
    public List<CRM_DynamicFormDTO> dynamicForm = null;
}
