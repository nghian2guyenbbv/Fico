package vn.com.tpf.microservices.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Photo {

	private String documentType;
	private String link;
	private Date updatedAt;

}