package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class QLDocument {
//    private String documentName;
    private String type;
    private String originalname;
    private String filename;
    private String md5;
    private String urlid;
    private String contentType;
}