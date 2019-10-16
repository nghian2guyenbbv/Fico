package vn.com.tpf.microservices.models.Automation;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDetailsVapDTO {
    private String vapProduct;
    private String vapTreatment;
    private String vapAmount;
    private String insuranceCompany;
}
