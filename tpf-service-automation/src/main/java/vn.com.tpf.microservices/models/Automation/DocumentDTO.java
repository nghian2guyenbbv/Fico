package vn.com.tpf.microservices.models.Automation;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DocumentDTO {
    public String originalname;
    public String filename;
    public String type;
}
