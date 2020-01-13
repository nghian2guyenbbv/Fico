package vn.com.tpf.microservices.models.Automation;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO implements Serializable {
	private String addressType;
	private String country;
	private String region;
	private String city;
	private String pinCode;
	private String area;
	private String building;
	private String house;
	private String ward;
	private String residentDurationYear;
	private String residentDurationMonth;
	private String cityDurationYear;
	private String cityDurationMonth;
	private String mobilePhone;


	//so dien thoai ban
	private String priStd;
	private String priNumber;
}
