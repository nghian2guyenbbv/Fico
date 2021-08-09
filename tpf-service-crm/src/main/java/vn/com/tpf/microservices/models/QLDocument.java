package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QLDocument {
//    private String documentName;
    private String type;
    private String originalname;
    private String filename;
    private String md5;
    private String urlid;
    private String contentType;
}