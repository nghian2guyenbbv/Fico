package vn.com.tpf.microservices.commons;

public enum AllocationTeamConfig {
    MAX_PENDING("maxPending", "MAX_PENDING"),
    MAX_QUOTA("maxQuota", "MAX_QUOTA"),
    ASSIGN_FLAG("assignFlag", "ASSIGN_FLAG"),
    USER_NAME("userName", "USER_NAME"),
    TEAM_NAME("teamName", "TEAM_NAME");

    private String code;
    private String name;

    AllocationTeamConfig(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getCode(String name){
        for (AllocationTeamConfig b : AllocationTeamConfig.values()) {
            if (b.name.equalsIgnoreCase(name)) {
                return b.code;
            }
        }
        return null;
    }
}
