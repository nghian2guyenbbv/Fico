package vn.com.tpf.microservices.models.Automation;

import lombok.*;
import vn.com.tpf.microservices.models.CommunicationDetails;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInfoDTO implements Serializable {
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
    private List<IdentificationDTO> identification;
    private List<AddressDTO> address;
    private List<FamilyDTO> family;
    private EmploymentDTO employmentDetails;
    private List<IncomeDetailDTO> incomeDetails;
    private List<BankCreditCardDetailsDTO> bankCreditCardDetails;

    public CommunicationDetails communicationDetails;
}
