package vn.com.tpf.microservices.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fico_trans_cancel", schema = "payoo")
public class FicoTransCancel implements Serializable {

    @Id
    @SequenceGenerator(name="fico_trans_cancel_sequence", sequenceName="hibernate_sequence", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String transactionId;

    private String transactionCancelId;

    @Column(name="loanid", nullable = false)
    private long loanId;

    private String loanAccountNo;

    private String identificationNumber;

    private long amount;

    private Timestamp transDate;

    private String reason;

    private String reasonReject;

    private Timestamp cancelDate;

    private int status;

    private int approveStatus;
}
