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

    private String gender;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private String placeOfIssue;
    private String maritalStatus;
    private String national;
    private String education;
    private String email;
    private List<CRM_IdentificationsListDTO> identification;
    private List<CRM_AddressListDTO> address;
    private List<CRM_FamilyDTO> family;
    private CRM_EmploymentDetailsListDTO employmentDetails;
    public CRM_CommunicationDetailsDTO communicationDetails;

}
