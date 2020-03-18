package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class CommunicationDetails {

    public String primaryAddress;
    public String primaryEmailId;
    public List<Phonenumber> phoneNumbers;

}
