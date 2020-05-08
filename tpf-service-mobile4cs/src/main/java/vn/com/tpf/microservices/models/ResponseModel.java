package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import vn.com.tpf.microservices.commons.Response;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ResponseModel {
    private Map<String, Object> mapData = new LinkedHashMap();
    private String request_id;
    private String reference_id;
    private String result_code;
    private Integer valid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String result_message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;

    public void setRequest_id(String request_id) {
        mapData.put(Response.REQUEST_ID, request_id);
    }

    public void setReference_id(String reference_id) {
        mapData.put(Response.REFERENCE_ID, reference_id);
    }

    public void setResult_code(String result_code) {
        mapData.put(Response.RESULT_CODE, result_code);
    }

    public void setResult_message(String result_message) {
        mapData.put(Response.RESULT_MESSAGE, result_message);
    }

    public void setData(Object data) {
        mapData.put(Response.DATA, data);
    }

    public void setValid(Integer valid) {
        mapData.put("valid", valid);
    }

    public JsonNode getResponseModel(ObjectMapper mapper) {
        ObjectNode data = mapper.createObjectNode();
        mapData.forEach((k, v) -> {
            if (v != null) {
                data.putPOJO(k, v);
            }
        });

        return mapper.createObjectNode().put("status", 200).set(Response.DATA, data);
    }
}
