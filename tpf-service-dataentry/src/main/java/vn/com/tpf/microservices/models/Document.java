package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class Document {
    private String type;
    private String comment;
    private Link link;
}