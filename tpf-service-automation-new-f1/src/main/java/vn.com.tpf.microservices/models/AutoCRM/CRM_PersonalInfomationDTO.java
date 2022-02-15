package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Data;

import java.util.List;

@Data
public class CRM_PersonalInfomationDTO {

    public CRM_PersonalInfoDTO personalInfo;
    public List<CRM_IdentificationsDTO> identifications = null;
    public List<CRM_AddressDTO> addresses = null;
    public CRM_CommunicationDetailsDTO communicationDetails;
    public List<CRM_FamilyListDTO> family = null;

}
