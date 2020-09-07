package vn.com.tpf.microservices.models.AutoCRM;


import lombok.Data;

import java.util.List;

@Data
public class CRM_AddressDTO {

    public String addressType;
    public String country;
    public String state;
    public String city;
    public String zipcode;
    public String area;
    public String addressLine1;
    public String addressLine2;
    public String addressLine3;
    public String landMark;
    public String yearsInCurrentAddress;
    public String monthsInCurrentAddress;
    public String phoneNumbers;
    public String phoneNumber;

//    public List<CRM_PhonenumberDTO> phoneNumbers = null;

}
