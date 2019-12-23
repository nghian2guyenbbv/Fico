package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "dataentry_firstcheck")
public class FirstCheck {

    private FirstCheckRequest request;
    private FirstCheckResponse response;
    private String createdBy;
    private String description;
    @CreatedDate
    private Date createdDate;

}