package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_VapDetailsListDTO {

    private String vapProduct;
    private String vapTreatment;
    private String vapAmount;
    private String insuranceCompany;

}
