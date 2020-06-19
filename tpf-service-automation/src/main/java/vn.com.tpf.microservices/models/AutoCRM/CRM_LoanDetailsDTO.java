package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Data;

@Data
public class CRM_LoanDetailsDTO {
    public CRM_SourcingDetailsDTO sourcingDetails;
    public CRM_VapDetailsDTO vapDetails;
}
