package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoutingF1 {
    private Long idLog;
    private String chanelId;
    private int routingNumber;
    private Date createDate;
    private String appNumber;
    private String vendorId;
}
