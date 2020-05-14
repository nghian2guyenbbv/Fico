package vn.com.tpf.microservices.models.AutoField;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestAutomationDTO {
    private String reference_id;
    private String project;
    private String transaction_id;
    private List<WaiveFieldDTO> waiveFieldDTO;
}
