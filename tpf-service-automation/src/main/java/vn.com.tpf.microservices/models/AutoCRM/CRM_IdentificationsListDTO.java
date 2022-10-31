package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_IdentificationsListDTO implements Serializable {

    private String identificationType;
    private String identificationNumber;
    private String issuingCountry;
    private String placeOfIssue;
    private String issueDate;
    private String expiryDate;

}
