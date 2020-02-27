
package vn.com.tpf.microservices.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "address_finnone")
public class AddressFinnone {

	@Id
	private String id;

	@Indexed(unique = true)
	private String areaCode;
	private String areaName;
	private String postCode;
	private String cityName;
	private String region;
    private String f1AreaId;
    private String f1AreaCode;
    private String f1ZipCode;

	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

}