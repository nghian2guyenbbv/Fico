package vn.com.tpf.microservices.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "autoassign_configure_scheme", schema = "auto_assign")
public class AutoAssignScheme {
        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        private int id;

        private String scheme;

        private Timestamp createddate;

        private int status;

        private String createdby;

        private Timestamp lastdate;
}
