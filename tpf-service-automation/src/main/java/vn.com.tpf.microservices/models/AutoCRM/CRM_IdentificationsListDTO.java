package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_IdentificationsListDTO implements Serializable {

    private String documentType;
    private String documentNumber;
    private String issueDate;
    private String expirationDate;
    private String placeOfIssue;
    private String countryOfIssue;

}
