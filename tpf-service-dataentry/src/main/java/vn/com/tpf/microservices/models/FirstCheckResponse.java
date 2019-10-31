package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class FirstCheckResponse {

    private String request_ID;
    private String full_name;
    private String birthday;
    private String gender;
    private String first_check_result;
    private String reject_code;
    private String reject_desc;

    private List<FirstCheckLoanOffer> loan_offer;
    private FirstCheckLoanInfor loan_infor;

}