package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class ViewTeamConfig {

    private int maxPending;
    private int maxQuota;
    private String userName;
    private String userRole;
    private String teamName;
    private String assignFlag;
}
