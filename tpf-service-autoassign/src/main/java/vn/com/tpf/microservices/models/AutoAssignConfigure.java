package vn.com.tpf.microservices.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Array;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Entity
@Table(name = "autoassign_configure", schema = "auto_assign")
public class AutoAssignConfigure {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private String vendorName;

    private Timestamp quote;

    private long totalQuanlity;

    private long actualTotalQuanlity;

    private long quanlityInDay;

    private long actualQuanlityInDay;

    private long priority;

    @Generated(GenerationTime.INSERT)
    private long idSeq;

    private long vendorId;

    private Timestamp createdDate;

    private Timestamp updatedDate;

    private boolean isAssign;

    public AutoAssignConfigure() {
    }

    private String createdBy;
}
