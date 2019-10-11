package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class FirstCheckResponse {

    private String responseid;
    private String cmnd;
    private boolean bank_card;
    private boolean dsa;
    private boolean white_area;
    private boolean pcb;
    private boolean all;
    private String decision_date;

}