package vn.com.tpf.microservices.models.RaiseQuery;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseQueryModel {
    private String productProcessor;
    private List<QmrServiceRequestParamsVoList> qmrServiceRequestParamsVoList;
}

