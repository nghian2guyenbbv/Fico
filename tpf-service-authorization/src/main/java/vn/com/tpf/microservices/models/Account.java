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
public class Account {

	@Id
	private String id;

	@Indexed(unique = true, sparse = true)
	private String userId;
	@Indexed(unique = true, sparse = true)
	private String username;
	@Indexed(unique = true, sparse = true)
	private String email;
	private String authorities;
	private Map<String, Object> optional;
	private Set<String> departments = new HashSet<>();
	private Set<String> projects = new HashSet<>();
	private Set<String> branches = new HashSet<>();
	private boolean enabled;

	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

}