package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class Phonenumber {

    public String phoneType;
    public String extension;
    public String isdCode;
    public String phoneNumber;
    public String countryCode;
    public String stdCode;

}
