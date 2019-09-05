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
	private String ts_lead_id;
	private String product_code;
	private String score_range;
	private String dsa_code;
	private String tsa_code;
	private String phone_number;
	private String national_id;
	private String first_name;
	private String middle_name;
	private String last_name;
	private String dob;
	private String gender;
	private String district;
	private String district_code;
	private String province;
	private String province_code;
	private String address_no;
	private String ward;
	private String stage;
	private Set<Map<String, Object>> documents = new HashSet<>();
	private Set<Map<String, Object>> comments = new HashSet<>();

	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

}
