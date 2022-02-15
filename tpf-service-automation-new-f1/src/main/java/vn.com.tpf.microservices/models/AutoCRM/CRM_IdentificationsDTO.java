package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_IdentificationsDTO {

    public String identificationType;
    public String identificationNumber;
    public String issuingCountry;
    public String placeOfIssue;
    public String issueDate;
    public String expiryDate;

}
