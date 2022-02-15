package vn.com.tpf.microservices.models.Automation;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiscFptDTO {
    private List<MiscFtp_ProductDetails> productDetails;
    private String downPayment;
    private String employeeCardNum;
    private String creditLimit;
}
