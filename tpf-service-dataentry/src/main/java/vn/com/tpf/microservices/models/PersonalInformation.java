package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class PersonalInformation {

    @Valid
    private PersonalInfo personalInfo;
    @Valid
    private List<Identification> identifications;
//    @Valid
    private List<Address> addresses;
    @Valid
    private CommunicationDetails communicationDetails;
    @Valid
    private List<Family> family;

}
