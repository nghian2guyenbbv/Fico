package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Data;

@Data
public class CRM_ApplicationInformationsDTO {

    public CRM_PersonalInfomationDTO personalInformation;
    public CRM_EmploymentDetailsDTO employmentDetails;

}
