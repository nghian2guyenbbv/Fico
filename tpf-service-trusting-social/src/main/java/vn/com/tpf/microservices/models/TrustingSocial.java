package vn.com.tpf.microservices.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "trusting_social")
public class TrustingSocial {

	@Id
	private String id;

	@Indexed(unique = true, sparse = true)
	private String tsLeadId;
	private String productCode;
	private String scoreRange;
	private String dsaCode;
	private String tsaCode;
	private String phoneNumber;
	private String nationalId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String dob;
	private String gender;
	private String district;
	private String districtCode;
	private String province;
	private String provinceCode;
	private String addressNo;
	private String ward;
	private Set<Map<?, ?>> documents = new HashSet<>();
	private Set<Map<?, ?>> comments = new HashSet<>();

	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

}
