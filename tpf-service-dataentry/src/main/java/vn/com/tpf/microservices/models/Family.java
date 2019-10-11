package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Family {
    @NotNull
    @NotEmpty(message = "memberName not null")
    private String memberName;

    @NotNull
    @NotEmpty(message = "phoneNumber not null")
    private String phoneNumber;

    @NotNull
    @NotEmpty(message = "relationship not null")
    private String relationship;

}
