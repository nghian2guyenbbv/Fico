package vn.com.tpf.microservices.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table
public class OauthClientToken {

	@Id
	private String tokenId;
	private String authenticationId;
	private String clientId;
	private String userName;
	private byte[] token;

}