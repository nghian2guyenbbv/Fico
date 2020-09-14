package vn.com.tpf.microservices.models;


import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "autoassign_configure_history", schema = "auto_assign")
public class AutoAssignConfigureHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private Timestamp quote;

    private Timestamp createdDate;

    private String createdBy;

    private String data;

    public AutoAssignConfigureHistory() {
    }

}
