package vn.com.tpf.microservices.models;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RequestAssignRobot<T> implements Serializable {

    private String func;
    private BodyAssignRobot body;

}

