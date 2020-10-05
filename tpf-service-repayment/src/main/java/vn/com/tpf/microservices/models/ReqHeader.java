package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
public class ReqHeader {

    @JsonProperty("tenantId")
    private int tenantId;

    private UserDetail userDetail;
}
