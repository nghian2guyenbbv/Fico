package vn.com.tpf.microservices.models.GetAppStatus;

import lombok.Data;

@Data
public class MlSpecificInfoVO{
    private String mlPropertyCity;
    private String mlPropertyDetails;
    private String mlCostOfHome;
    private String mlCostOfLand;
    private String mlCostOfConstruction;
}
