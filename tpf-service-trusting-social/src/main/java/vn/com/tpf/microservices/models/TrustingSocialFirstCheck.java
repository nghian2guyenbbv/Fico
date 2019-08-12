package vn.com.tpf.microservices.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Document
@CompoundIndexes({
	@CompoundIndex(name  ="nationalId_phoneNumber", def = "{'nationalId' : 1, 'phoneNumber': 1}")
})
@Builder
public class TrustingSocialFirstCheck {
	
	@Id
	private String id;

	private String phoneNumber;

	private String nationalId;

	private String firstName;

	private String middleName;

	private String lastName;

	private Date dob;

	private String gender;

	private String provinceCode;

	private String districtCode;

	private String addressNo;


	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;
}
