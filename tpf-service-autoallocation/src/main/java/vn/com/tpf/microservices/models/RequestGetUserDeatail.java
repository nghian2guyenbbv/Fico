package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class RequestGetUserDeatail {

    private String teamName;
    private int page;
    private int itemPerPage;
    private String sortItem;
    private String typeSort;
    private String roleUserLogin;
}
