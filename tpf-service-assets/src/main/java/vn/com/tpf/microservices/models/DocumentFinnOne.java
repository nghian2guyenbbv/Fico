package vn.com.tpf.microservices.models;




import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Document
@Builder
@JsonInclude(Include.NON_NULL)
public class DocumentFinnOne {

	@Id
	private String id;
	@Indexed
	private String productCode;
	@Indexed(unique = true)
	private String schemeCode;
	
	@Indexed
	private String valueProductFinnOne;
	
	@Indexed(unique = true)
	private String valueShemeFinnOne;
	
	private Object documents;

	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

}
