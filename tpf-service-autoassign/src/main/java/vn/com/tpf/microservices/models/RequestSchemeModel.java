package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class RequestSchemeModel {
    private String request_id;
    private String date_time;
    private List<AutoAssignScheme> data;

}
