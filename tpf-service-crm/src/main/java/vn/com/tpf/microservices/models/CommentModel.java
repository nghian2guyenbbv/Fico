package vn.com.tpf.microservices.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentModel {
    private String commentId;
    private String stage;
    private String type;
    private String code;
    private String description;
    private String request;
    private CommentResponseModel response;
    private Date createdDate;
    private Date lastModifiedDate;
    private String queryId;
}