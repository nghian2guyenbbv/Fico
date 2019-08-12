package vn.com.tpf.microservices.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Document
@Builder
public class Assets {

  @Id
  private String key;
  
  @Builder.Default
  private Set<Map<?, ?>> assets = new HashSet<>();

  @CreatedDate
  private Date createdAt;
  @LastModifiedDate
  private Date updatedAt;

}