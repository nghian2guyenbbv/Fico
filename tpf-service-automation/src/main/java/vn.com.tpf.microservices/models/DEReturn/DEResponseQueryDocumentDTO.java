package vn.com.tpf.microservices.models.DEReturn;


import lombok.Data;

@Data
public class DEResponseQueryDocumentDTO {
    private String queryCode;
    private String fileName;
    private String comments;
}
