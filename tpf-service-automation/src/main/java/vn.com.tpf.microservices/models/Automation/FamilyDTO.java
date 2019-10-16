package vn.com.tpf.microservices.models.Automation;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDTO {
    private String memberName;
    private String relationshipType;
    private String phoneNumber;
    private String educationStatus;
    private String comName;
    private String isDependent;
}
