package vn.com.tpf.microservices.commons;

public enum ResultData {
    SUCCESS("0","Success"),
    OTHER_ERROR("500","Other Error");

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
}
