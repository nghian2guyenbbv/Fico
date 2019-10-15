package vn.com.tpf.microservices.models.Automation;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentificationDTO implements Serializable {
	private String documentType;
	private String documentNumber;
	private String issueDate;
	private String expirationDate;
	private String countryOfIssue;
}
