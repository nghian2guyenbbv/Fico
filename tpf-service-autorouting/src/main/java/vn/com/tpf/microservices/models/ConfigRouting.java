package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "routing_config", schema = "routing")
@Data
public class ConfigRouting {

    @Id
    @Column(name = "id_config")
    private String idConfig;

    @Column(name = "chanel_name")
    private String chanelName;

    @Column(name = "chanel_config")
    private String chanelConfig;

    @Column(name = "quota")
    private String quota;

    @Column(name = "create_date")
    private Timestamp create_date;

    @Column(name = "chanel_start")
    private Timestamp timeStart;

    @Column(name = "chanel_end")
    private Timestamp timeEnd;

    @Column(name = "day_id")
    private Timestamp dayId;
}
