package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;


@Data
public class RequestImportFile {

    private String userLogin;
    private List<UserChecking> listUser;
    private String teamNameUser;
}