package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class CommentResponseModel {
    private String comment;
    private Application data;
    private List<Document> documents;
}