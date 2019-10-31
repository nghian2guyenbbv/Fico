package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.Date;

@Data
public class CommentModel {
    private String commentId;
    private String state;
    private String type;
    private String code;
    private String request;
    private CommentResponseModel response;
    private Date createdDate;
    private Date lastModifiedDate;
}