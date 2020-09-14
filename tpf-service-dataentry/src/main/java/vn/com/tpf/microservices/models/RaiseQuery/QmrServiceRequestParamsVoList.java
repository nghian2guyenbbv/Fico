package vn.com.tpf.microservices.models.RaiseQuery;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QmrServiceRequestParamsVoList
{
    private QueryModuleLocationDetailsVO queryModuleLocationDetailsVO;
    private QueryModuleDocumentDetailsVO queryModuleDocumentDetailsVO;
    private String queryCode;
    private String applicationNumber;
    private String responseBy;
    private String queryId;
    private String response;
}

