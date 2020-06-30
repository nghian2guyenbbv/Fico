package vn.com.tpf.microservices.models.AutoCRM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRM_FamilyDTO {
    private String memberName;
    private String relationshipType;
    private String phoneNumber;
    private String educationStatus;
    private String comName;
    private String isDependent;
}
