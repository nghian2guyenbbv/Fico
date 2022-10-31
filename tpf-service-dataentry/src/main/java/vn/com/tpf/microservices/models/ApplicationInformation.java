package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class ApplicationInformation {

    @Valid
    private PersonalInformation personalInformation;
    @Valid
    private EmploymentDetails employmentDetails;
    @Valid
    private List<FinancialDetail> financialDetails;

}
