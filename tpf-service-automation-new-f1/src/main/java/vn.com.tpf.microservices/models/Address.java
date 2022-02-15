package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class Address {
    public String addressType;
    public String country;
    public String state;
    public String city;
    public String zipcode;
    public String area;
    public String addressLine1;
    public String addressLine2;
    public String addressLine3;

    private String landMark;
    public void setLandMark(String landMark) {
        if (landMark == null || landMark.isEmpty())
            landMark = "";
        this.landMark = landMark;
    }

    public String yearsInCurrentAddress;
    public String monthsInCurrentAddress;
    public List<Phonenumber> phoneNumbers = null;

}