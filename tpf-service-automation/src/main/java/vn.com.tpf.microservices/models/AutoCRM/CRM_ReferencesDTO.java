package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
public class CRM_ReferencesDTO implements Serializable {

    public String name;
    public String relationship;
    public String phoneNumber;
//    public List<CRM_PhonenumberDTO> phoneNumbers = null;

}
