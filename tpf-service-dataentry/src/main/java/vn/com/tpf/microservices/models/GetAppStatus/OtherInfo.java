package vn.com.tpf.microservices.models.GetAppStatus;

import lombok.Data;

@Data
public class OtherInfo{
    private String deviationsRaised;
    private String fiResult;
    private String underWritterResult;
    private String currentProcessingStage;
}
