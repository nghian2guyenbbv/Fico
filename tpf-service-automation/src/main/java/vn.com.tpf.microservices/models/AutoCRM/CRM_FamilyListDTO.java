package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_FamilyListDTO {

    public String memberName;
    public String phoneNumber;
    public String relationship;

}
