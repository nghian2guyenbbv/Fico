package vn.com.tpf.microservices.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class AutoAssignModel<T> implements Serializable {

    private String idAutoAllocation;
    private String appId;
    private String userName;
    private String userAuto;
    private String automationResult;
    private String automationResultMessage;

}

