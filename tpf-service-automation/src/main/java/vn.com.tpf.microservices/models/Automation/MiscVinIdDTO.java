package vn.com.tpf.microservices.models.Automation;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiscVinIdDTO {
    private String model;
    private String goodCode;
    private String goodType;
    private String quantity;
    private String goodPrice;
    private String downPayment;
    private String employeeCardNum;
    private String vinCode;
    private String venCode;
    private String saleChannel;
    private String dealerCode;
    private String productName;
}
