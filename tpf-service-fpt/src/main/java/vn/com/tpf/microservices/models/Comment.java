package vn.com.tpf.microservices.models;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@Document(collection = "comments")
@JsonInclude(Include.NON_NULL)
public class Comment {

	private String code;
	private String comment;
	private Set<Subcode> subcode;
	private String type;

}