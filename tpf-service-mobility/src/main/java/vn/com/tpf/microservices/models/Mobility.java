package vn.com.tpf.microservices.models;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

public class Mobility {
	@Id
	private String id;
	@Indexed
	private String appId;
	@Indexed(unique = true, sparse = true)
	private long leadId;
	private String firstName;
	private String lastName;
	private String nationalId;
	private String dateOfBirth;
	private String dsaCode;
	private String district;
	private String districtFinnOne;
	private String city;
	private String cityFinnOne;
	private String bankCard;
	@Builder.Default
	private String address = "*";
	@Builder.Default
	private String loanRequest = "10000000";
	@Builder.Default
	private String chanel = "DIRECT";
	@Builder.Default
	private String branch = "SMARTNET";
	private String scheme;
	private String schemeFinnOne;
	private String product;	
	private String productFinnOne;
	private String stage;
	private String status;
	@Builder.Default
	private List<Object> automationResults = Arrays.asList();
	private List<Object> filesUpload;
	private String viewLastUpdated;
	private String userCreatedQueue;
    private Map<String, Object> preChecks;
    private Map<String, Object> returns;
	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;
	private String communicationTranscript;
}


