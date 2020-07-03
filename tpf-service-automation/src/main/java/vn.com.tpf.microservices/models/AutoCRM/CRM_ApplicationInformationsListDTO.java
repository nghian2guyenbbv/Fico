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
    private CRM_CommunicationDetailsDTO communicationDetail;
    private CRM_FinancialDetailsDTO financialDetail;

}
