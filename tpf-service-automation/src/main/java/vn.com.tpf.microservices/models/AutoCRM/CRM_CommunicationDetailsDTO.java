package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_CommunicationDetailsDTO {

    public String primaryAddress;
    public String phoneNumbers;
//    public String primaryEmailId;
//    public List<CRM_PhonenumberDTO> phoneNumbers;

}
