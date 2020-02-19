package vn.com.tpf.microservices.models.DEReturn;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection="response_query")
public class DEResponseQueryDocumentDTO {
    private String queryCode;
    private String urlFile;
    private String comments;

}
