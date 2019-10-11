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
@Document
public class App {

	@Id
	private String id;

	private String project;
	@Indexed(unique = true, sparse = true)
	private String uuid;
	@Indexed(unique = true, sparse = true)
	private String appId;
	private String assigned;
	private String partnerId;
	private String fullName;
	private String status;
	private String automationResult;
	private Map<String, Object> optional;
	private Set<Map<String, Object>> documents = new HashSet<>();
	private Set<Map<String, Object>> comments = new HashSet<>();
	private Set<Map<String, Object>> statusHistory = new HashSet<>();
	private Set<Map<String, Object>> assignedHistory = new HashSet<>();
	private Set<Map<String, Object>> automationHistory = new HashSet<>();

	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

}