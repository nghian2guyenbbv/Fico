package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "fico_partner", schema = "payoo")
@Builder
@Data
@AllArgsConstructor
public class FicoPartner {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id")
    private int id;

    @Column(name="partner_name")
    @JsonProperty("partner_name")
    private String partnerName;

    @Column(name="ip")
    @JsonProperty("ip")
    private String ip;

    @Column(name="prefix")
    @JsonProperty("prefix")
    private String prefix;

    @Column(name="description")
    @JsonProperty("description")
    private String description;

    @Column(name="status")
    @JsonProperty("status")
    private int status;

    @Column(name="created_date")
    @JsonProperty("created_date")
    private Timestamp createdDate;

    @Column(name="updated_date")
    @JsonProperty("updated_date")
    private Timestamp updatedDate;

    @Column(name="created_by")
    @JsonProperty("created_by")
    private String createdBy;

    @Column(name="updated_by")
    @JsonProperty("updated_by")
    private String updatedBy;

    @Column(name="source_account_number")
    @JsonProperty("source_account_number")
    private String sourceAccountNumber;

    @Column(columnDefinition = "integer default 0")
    private long fee;

    public FicoPartner() {
    }
}
