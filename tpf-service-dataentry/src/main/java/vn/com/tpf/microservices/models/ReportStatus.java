package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
public class ReportStatus {
    private String appNo;
    private String status;

}
