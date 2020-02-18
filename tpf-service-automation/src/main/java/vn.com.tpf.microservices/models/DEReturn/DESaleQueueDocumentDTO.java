package vn.com.tpf.microservices.models.DEReturn;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection="sale_queue")
public class DESaleQueueDocumentDTO {
    @Id
    private String documentName;
    private String statusDocument;
    private String urlFile;
}
