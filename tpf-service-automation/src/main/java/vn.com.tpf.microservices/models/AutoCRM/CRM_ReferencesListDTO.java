package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_ReferencesListDTO {

    private String fullName;
    private String relationShip;
    private String mobilePhoneNumber;

    //so dien thoai ban
    private String priStd;
    private String priNumber;
    private String priExt;

}
