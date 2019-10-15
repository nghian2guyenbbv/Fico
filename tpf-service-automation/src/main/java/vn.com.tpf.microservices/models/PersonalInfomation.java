package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class PersonalInfomation {

    public PersonalInfo personalInfo;
    public List<Identification> identifications = null;
    public List<Address> addresses = null;
    public CommunicationDetails communicationDetails;
    public List<Family> family = null;

}
