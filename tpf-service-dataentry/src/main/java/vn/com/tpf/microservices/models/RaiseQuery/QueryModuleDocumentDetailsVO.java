package vn.com.tpf.microservices.models.RaiseQuery;

import lombok.Builder;

@Builder
public class QueryModuleDocumentDetailsVO{
    private String documentName;
    private byte[] document;
}
