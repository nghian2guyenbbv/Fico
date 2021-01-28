package vn.com.tpf.microservices.models;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BodyAssignRobot<T> implements Serializable {

    private String project;
    private String reference_id;
    private List<AutoAssignModel> autoAssign;

}

