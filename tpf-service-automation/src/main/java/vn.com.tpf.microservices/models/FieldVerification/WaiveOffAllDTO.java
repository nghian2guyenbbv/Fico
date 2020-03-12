package vn.com.tpf.microservices.models.FieldVerification;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@Builder
public class WaiveOffAllDTO {
    private String appId;
    private int status = 0;
    private String reasonCode;
    private String reference_id;
    private String project;
    private String transaction_id;
}
