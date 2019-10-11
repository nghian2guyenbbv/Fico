package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class CommentRequestModel {
    private String errorCode;
    private String description;

}