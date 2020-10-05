package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_CONFIG_ROBOT")
@Data
public class ConfigRobotProcedure {

    @Id
    @Column(name = "CHANEL")
    private String chanel;

    @Column(name = "FROM_TIME")
    private String fromTime;

    @Column(name = "TO_TIME")
    private String toTime;

    @Column(name = "CONFIG")
    private String config;

    @Column(name = "LIMIT")
    private int limit;
}
