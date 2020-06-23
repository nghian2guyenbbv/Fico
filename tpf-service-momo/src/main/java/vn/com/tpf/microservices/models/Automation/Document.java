package vn.com.tpf.microservices.models.Automation;

import lombok.Data;

@Data
public class Document {
    public String originalname;
    public String filename;
    public String type;
    public String link;
}
