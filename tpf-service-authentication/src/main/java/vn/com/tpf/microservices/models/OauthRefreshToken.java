package vn.com.tpf.microservices.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table
public class OauthRefreshToken {

	@Id
	private String tokenId;
	private byte[] token;
	private byte[] authentication;

}