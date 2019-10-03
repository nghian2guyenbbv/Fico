package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@Document(collection = "dataentry_report")
public class Report {
    private UUID quickLeadId;
    private String applicationId;
    private String status;
    private String description;
    private String stage;
    private Date createdDate;
    private Date lastModifiedDate;

}
