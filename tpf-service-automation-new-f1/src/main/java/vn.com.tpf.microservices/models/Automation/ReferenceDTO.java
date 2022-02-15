package vn.com.tpf.microservices.models.Automation;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceDTO implements Serializable {
    private String fullName;
    private String relationShip;
    private String mobilePhoneNumber;

    //so dien thoai ban
    private String priStd;
    private String priNumber;
    private String priExt;
}
