package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class FirstCheckRequest {

    private String project_name;
    private String request_id;
    private String full_name;
    private String birthday;
    private String gender;
    private String customer_id;
    private String dsa_code;
    private String bank_card_number;
    private String current_address;
    private String area_code;
    private String phoneNumber;

//    private String requestID;
//    private String customerName;
//    private String customerId;
//    private String dsaCode;
//    private String bankCardNumber;
//    private String currentAddress;
//    private String areaId;

}