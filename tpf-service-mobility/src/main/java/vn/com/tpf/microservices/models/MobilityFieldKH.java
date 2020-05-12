package vn.com.tpf.microservices.models;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
//import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Document
@Builder
@JsonInclude(Include.NON_NULL)

public class MobilityFieldKH {
	@Indexed
	private String appId;
	private String fullName;
	private String phone;
	private String dateOfBirth;
	private String sex;
	private String nationalId;
	private String bankCard;
	private String spouseName;
	private String spouseIdCard;
	private String spousePhone;
	private String homeAddress;
	private String cityCodeHome;
	private String districtCodeHome;
	private String homeCom;
	private String cityCodeCom;
	private String districtCodeCom;
	private String comName;
	private String position;
	private String kycNotes;
	private int fieldType;
	private String comment;
}
