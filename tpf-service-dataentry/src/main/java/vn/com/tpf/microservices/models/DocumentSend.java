package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DocumentSend {
    @JsonProperty("document-type")
    private String document_type;
    @JsonProperty("file-type")
    private String file_type;
    @JsonProperty("file-content")
    private String file_content;
    @JsonProperty("file-checksum")
    private String file_checksum;
}