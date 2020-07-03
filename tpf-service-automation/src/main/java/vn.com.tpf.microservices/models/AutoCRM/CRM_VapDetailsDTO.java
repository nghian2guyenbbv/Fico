package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_VapDetailsDTO {

    public String vapProduct;
    public String vapTreatment;
    public String insuranceCompany;

}
