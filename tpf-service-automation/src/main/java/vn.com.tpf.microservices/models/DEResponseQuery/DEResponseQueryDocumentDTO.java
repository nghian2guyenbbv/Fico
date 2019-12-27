package vn.com.tpf.microservices.models.DEResponseQuery;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection="automation_autoassign")
public class DEResponseQueryDocumentDTO {
    @Id
    private String querycode;
    private String urlfile;
    private String comment;

}
