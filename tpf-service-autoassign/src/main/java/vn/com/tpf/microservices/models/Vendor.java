package vn.com.tpf.microservices.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "vendor", schema = "auto_assign")
public class Vendor {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("queue")
    private String queue;

    @JsonProperty("createdDate")
    private Timestamp createdDate;

    @JsonProperty("updatedDate")
    private Timestamp updatedDate;

    public Vendor() {
    }

}
