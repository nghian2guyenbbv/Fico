package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class RequestGetUserDetail {

    private List<String> teamName;
    private int page = 0;
    private int itemPerPage = 10;
    private String sortItem = "userId";
    private String typeSort = "ASC";
    private String roleUserLogin;
    private String userName;
    private String userLogin;
    private String userLeader;
}