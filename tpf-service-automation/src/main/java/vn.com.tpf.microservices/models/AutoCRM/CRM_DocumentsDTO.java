package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CRM_DocumentsDTO {
    public String originalname;
    public String filename;
    public String type;
    public String link;
}
