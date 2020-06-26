package vn.com.tpf.microservices.models.AutoCRM;

import lombok.Data;

import java.util.List;

@Data
public class CRM_CommunicationDetailsDTO {

    public String primaryAddress;
    public String primaryEmailId;
    public List<CRM_PhonenumberDTO> phoneNumbers;

}
