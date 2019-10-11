package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Reference {
    @NotNull
    @NotEmpty(message = "name not null")
    private String name;

    @NotNull
    @NotEmpty(message = "relationship not null")
    private String relationship;

    @Valid
    private List<PhoneNumber> phoneNumbers;

}