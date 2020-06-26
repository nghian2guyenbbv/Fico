package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_LoanDetailsListDTO {
    private List<CRM_SourcingDetailsDTO> sourcingDetails;
    private List<CRM_VapDetailsDTO> vapDetails;
}
