package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_PersonalInfoDTO {

    public String firstName;
    public String middleName;
    public String lastName;
    public String fullName;
    public String gender;
    public String dateOfBirth;
    public String nationality;
    public String maritalStatus;
    public String customerCategoryCode;
    public String issuePlace;

}
