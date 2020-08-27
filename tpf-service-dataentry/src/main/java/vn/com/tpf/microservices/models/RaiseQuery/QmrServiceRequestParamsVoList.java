package vn.com.tpf.microservices.models.RaiseQuery;

import lombok.Builder;

@Builder
public class QmrServiceRequestParamsVoList
{
    private QueryModuleLocationDetailsVO queryModuleLocationDetailsVO;
    private String queryCode;
    private String applicationNumber;
    private String responseBy;
    private String queryId;
    private String response;
}
