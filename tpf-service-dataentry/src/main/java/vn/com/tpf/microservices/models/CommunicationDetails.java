package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CommunicationDetails {
    @NotNull
    @NotEmpty(message = "primaryAddress not null")
    private String primaryAddress;

    @NotNull
//    @NotEmpty(message = "primaryEmailId not null")
    private String primaryEmailId;

    @Valid
    private List<PhoneNumber> phoneNumbers;

}
