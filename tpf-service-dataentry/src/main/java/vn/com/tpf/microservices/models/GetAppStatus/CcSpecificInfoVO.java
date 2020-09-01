package vn.com.tpf.microservices.models.GetAppStatus;


import lombok.Data;

@Data
public class CcSpecificInfoVO{
    private String benefits;
    private String cardLimit;
    private String cardType;
    private String networkGateway;
    private String product;
}
