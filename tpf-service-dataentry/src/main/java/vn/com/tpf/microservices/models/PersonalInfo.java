package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PersonalInfo {

    @NotNull
    @NotEmpty(message = "firstName not null")
    private String firstName;

//    @NotNull
//    @NotEmpty(message = "middleName not null")
    private String middleName;

    @NotNull
    @NotEmpty(message = "lastName not null")
    private String lastName;

    @NotNull
    @NotEmpty(message = "fullName not null")
    private String fullName;

    @NotNull
    @NotEmpty(message = "gender not null")
    private String gender;

    @NotNull
    @NotEmpty(message = "dateOfBirth not null")
    private String dateOfBirth;

    @NotNull
    @NotEmpty(message = "nationality not null")
    private String nationality;

    @NotNull
    @NotEmpty(message = "maritalStatus not null")
    private String maritalStatus;

    @NotNull
    @NotEmpty(message = "customerCategoryCode not null")
    private String customerCategoryCode;

}
