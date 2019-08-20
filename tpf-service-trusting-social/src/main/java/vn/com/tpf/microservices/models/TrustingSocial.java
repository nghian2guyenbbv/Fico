package vn.com.tpf.microservices.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class TrustingSocial {

	@Id
	private String id;

	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

}
