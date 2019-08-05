package vn.com.tpf.microservices.models;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Account {

  @Id
  private String id;

  @Indexed(unique = true, sparse = true)
  private String userId;
  @Indexed(unique = true, sparse = true)
  private String username;
  @Indexed(unique = true, sparse = true)
  private String email;
  private String authorities;
  private Set<String> departments;
  private Set<String> projects;
  private boolean enabled;

}