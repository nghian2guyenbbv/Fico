package vn.com.tpf.microservices.models.apiFin1;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "application_data_push", schema = "eform")
public class ApplicationDataPushModel {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "application_data_push_iq_seq"
    )
    @SequenceGenerator(name="application_data_push_iq_seq", sequenceName="eform.application_data_push_iq_seq",allocationSize=1)
    private int id;

    @Column(name = "product")
    private String product;

    @Column(name = "clientid")
    private String clientId;

    @Column(name = "scheme")
    private String scheme;

    @Column(name = "applicationno")
    private String applicationNo;

    @Column(name = "sourcechannel")
    private String sourceChannel;

    @Column(name = "loanamountrequested")
    private long loanAmountRequested;

    @Column(name = "customername")
    private String customerName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "createddate")
    private Timestamp createdDate;

    @Column(name = "stage")
    private String stage;

    @Column(name = "customerid")
    private String customerID;

    @Column(name = "createduser")
    private String createdUser;

    @Column(name = "leadid")
    private String leadId;

    @Column(name = "status")
    private String status;
}
