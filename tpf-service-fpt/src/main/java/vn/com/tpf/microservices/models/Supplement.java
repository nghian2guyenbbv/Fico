package vn.com.tpf.microservices.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Supplement {

	private String code;
	private String commentTpf;
	private String commentFpt;
	private boolean isPending;
	private String type;
	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

	public void SupplementFpt(String code, String commentFpt, String commentTpf, String type, Date updatedAt,Date createdAt) {

		this.code = code;
		this.commentFpt = commentFpt;
		this.commentTpf = commentTpf;
		this.updatedAt = updatedAt;
		this.isPending = true;
		this.createdAt = createdAt;
	}

	public void SupplemenTPBank(String code, String commentTpf, String type, Date createdAt) {
		this.code = code;
		this.commentTpf = commentTpf;
		this.isPending = false;
		this.type = type;
		this.createdAt = createdAt;
	}

}