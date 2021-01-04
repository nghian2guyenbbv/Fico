package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "partner")
public class Partner {

    @Id
    private String id;
    private String partnerId;
    private String partnerName;
    private String ip;
    private String project;
    private String clientID;
    private String pgpPublicKey;
    private String status;
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastUpdated;
    private String token;
    private Map<String, Object> account;
    private Map<String, Object> url;
    private int version;
}
