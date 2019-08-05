package vn.com.tpf.microservices.models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class OauthClientDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(unique = true)
	private String clientId;
	private String clientSecret;
	private String secret;
	private String scope;
	private String authorities;
	private String resourceIds;
	private String authorizedGrantTypes;
	private String webServerRedirectUri;
	private String additionalInformation;
	private String autoapprove;
	private Integer accessTokenValidity;
	private Integer refreshTokenValidity;

	@CreationTimestamp
	private Date createdAt;
	@UpdateTimestamp
	private Date updatedAt;

}