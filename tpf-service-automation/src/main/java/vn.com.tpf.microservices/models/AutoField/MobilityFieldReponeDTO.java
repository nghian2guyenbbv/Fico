package vn.com.tpf.microservices.models.AutoField;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="automation_mobility_field")
public class MobilityFieldReponeDTO {
    private String appId;
    private String automation_result;
}
