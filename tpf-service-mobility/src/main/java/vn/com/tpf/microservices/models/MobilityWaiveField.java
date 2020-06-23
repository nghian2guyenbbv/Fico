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

public class MobilityWaiveField {
	@Id
	private String id;
	@Indexed
	private String appId;
	@Builder.Default
	private String project = "mobility";
	@Builder.Default
	private String chanel = "DIRECT";
	@Builder.Default
	private String branch = "SMARTNET";
	@Builder.Default
	private List<Object> automationResults = Arrays.asList();
	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;
}


