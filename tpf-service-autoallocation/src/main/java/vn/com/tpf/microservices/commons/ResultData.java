package vn.com.tpf.microservices.commons;

public enum ResultData {
    SUCCESS("200", "Success"),
    FAIL("888", "Fail"),
    DATA_EMPTY("150", "Data empty"),
    OTHER_ERROR("500", "Other Error"),
    NOT_EXIST("301", "Property is not exist"),
    IS_REQUITED("998", "Property is required"),
    PERMISSION_FAILED("999", "Permission is denied"),
    NOT_EXIST_STATUS("205", "Application not exist or status_assign difference assigning"),
    NOT_HOLD("201", "Application can not be hold"),
    PENDING_LIMIT("202", "Number of pending app is limited"),
    APP_NOT_EXIST("203", "Application not exist"),
    STATUS_PENDING("204", "Application is pending status assign"),
    ADD_ERROR("206", "User Exist in Team Name "),
    ERROR_RE_ASSIGN("207", "Error Re-assign")
            ;
    private String resultCode;
    private String resultMessage;

    ResultData(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public static ResultData findResultData(String code) {
        for (ResultData r : ResultData.values()) {
            if (r.resultCode.equals(code)) {
                return r;
            }
        }
        return null;
    }



}
