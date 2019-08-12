package vn.com.tpf.microservices.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class App {

  @Id
  private String id;

  private String project;
  @Indexed(unique = true, sparse = true)
  private String uuid;
  @Indexed(unique = true, sparse = true)
  private String appId;
  private String status;
  private String fullName;
  private String partnerId;
  private String productCode;
  private String schemeCode;
  private String automationResult;
  private String assigned;
  private Map<?, ?> optional;
  private Set<Map<?, ?>> photos = new HashSet<>();
  private Set<Map<?, ?>> statusHistory = new HashSet<>();
  private Set<Map<?, ?>> assignedHistory = new HashSet<>();
  private Set<Map<?, ?>> automationHistory = new HashSet<>();

  @CreatedDate
  private Date createdAt;
  @LastModifiedDate
  private Date updatedAt;

}