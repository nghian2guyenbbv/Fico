package vn.com.tpf.microservices.models;

public class RequestModel{
    private String request_id;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d{4}-[01]d-[0-3]dT[0-2]d:[0-5]d:[0-5]d(?:.d+)?Z?", locale = "en_GB")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private String date_time;

    private ConfigRouting data;
    private LogChoiceRouting logChoiceRouting;

    public RequestModel() {
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public ConfigRouting getData() {
        return data;
    }

    public LogChoiceRouting getLogChoiceRouting() {
        return logChoiceRouting;
    }
}

