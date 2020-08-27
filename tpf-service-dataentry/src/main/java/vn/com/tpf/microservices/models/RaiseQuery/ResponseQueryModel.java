package vn.com.tpf.microservices.models.RaiseQuery;

import lombok.Builder;

import java.util.List;

@Builder
public class ResponseQueryModel {
    private String productProcessor;
    private List<QmrServiceRequestParamsVoList> qmrServiceRequestParamsVoListList;
}

