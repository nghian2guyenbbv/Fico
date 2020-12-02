package vn.com.tpf.microservices.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QueryModuleDocumentDetailsVO{
    private String documentName;
    private byte[] document;
}
