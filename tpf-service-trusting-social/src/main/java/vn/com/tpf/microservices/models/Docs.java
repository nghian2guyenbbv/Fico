package vn.com.tpf.microservices.models;

import java.util.Date;

import lombok.Data;

@Data
public class Docs {

	private String document_type;
	private String view_url;
	private String download_url;
	private Date updatedAt;

}
