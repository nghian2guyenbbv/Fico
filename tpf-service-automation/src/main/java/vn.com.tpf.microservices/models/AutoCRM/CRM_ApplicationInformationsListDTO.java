package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_ApplicationInformationsListDTO implements Serializable {

    private CRM_PersonalInfoDTO personalInfo;
    private CRM_IdentificationsDTO identification;
    private List<CRM_AddressListDTO> address;
    private List<CRM_FamilyListDTO> family;
    private CRM_CommunicationDetailsDTO communicationDetail;
    private CRM_EmploymentDetailsDTO employmentDetail;
    private CRM_FinancialDetailsDTO financialDetail;

}
