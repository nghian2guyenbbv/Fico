package vn.com.tpf.microservices.models.FieldVerification;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@Builder
public class InitiateVerificationDTO {

    private String appId;
    private String userName;
    private String userAuto;
    private int status=0;
    private String agencyCode;
    private String reference_id;
    private String project;
    private String transaction_id;
}
