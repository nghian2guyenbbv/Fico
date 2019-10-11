package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "dataentry_firstcheck")
public class FirstCheck {

    private FirstCheckRequest request;
    private FirstCheckResponse response;

}