package vn.com.tpf.microservices.models.AutoMField;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MFieldRequest {
    private String reference_id;
    private String project;
    private String transaction_id;
    private List<WaiveFieldDetails> waiveFieldDTO;
    private List<SubmitFieldDetails> submitFieldDTO;
}
