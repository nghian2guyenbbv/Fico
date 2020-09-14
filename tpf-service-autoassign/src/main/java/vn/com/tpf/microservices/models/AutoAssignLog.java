package vn.com.tpf.microservices.models;


import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "autoassign_log", schema = "auto_assign")
public class AutoAssignLog {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private String requestId;

    private String referenceId;

    private String data;

    private Timestamp createdDate;

    public AutoAssignLog() {
    }

}
