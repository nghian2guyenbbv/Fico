package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDetail {

    @JsonProperty("branchId")
    private int branchId;

    @JsonProperty("userCode")
    private String userCode;
}
