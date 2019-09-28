package vn.com.tpf.microservices.models;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@NamedStoredProcedureQuery(
        name = "getListTrans",
        procedureName = "payoo.getListTrans",
        resultClasses = FicoPayooImp.class,
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Date.class)
        }
)
@Data
@Entity
@Table(name = "fico_payoo_imp", schema = "payoo")
public class FicoPayooImp implements java.io.Serializable {

    @Column(name="trans_date")
    private Date transDate;

    @Column(name="create_date")
    private Date createDateTrans;

    @Transient
    private String createDate;

    @Column(name="vendor_code")
    private String vendorCode;

    @Id
    private String orderCode;

    @Column(name="amount")
    private long amount;

    @Column(name="full_name")
    private String fullName;

    @Column(name="is_completed")
    private int isCompleted;

    @Column(name="client_code")
    private String clientCode;

}
