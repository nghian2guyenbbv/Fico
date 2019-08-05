package vn.com.tpf.microservices.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table
public class OauthCode {

	@Id
	private String code;
	private byte[] authentication;

}