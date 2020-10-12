package vn.com.tpf.microservices.models;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "ALLOCATION_PENDING_CODE")
@Immutable
@Entity
@Data
public class AllocationPendingCode {
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "CODE_PENDING")
    private String codePending;
    @Column(name = "CODE_DESC")
    private String codeDesc;
    @Column(name = "STAGE_NAME")
    private String stageName;
    @Column(name = "DEL_FLAG")
    private String delFlag;
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "USER_CREATED")
    private String userCreated;
    @Column(name = "UPDATED_TIME")
    private Timestamp updatedTime;
    @Column(name = "USER_UPDATED")
    private String userUpdated;
}
