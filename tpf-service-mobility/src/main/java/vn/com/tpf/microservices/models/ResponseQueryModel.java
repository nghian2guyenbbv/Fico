package vn.com.tpf.microservices.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseQueryModel {
    private String productProcessor;
    private List<QmrServiceRequestParamsVoList> qmrServiceRequestParamsVoList;
}

