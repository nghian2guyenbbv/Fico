package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "schedule_routing", schema = "routing")
@Data
public class ScheduleRoute {

    @Id
    @Column(name = "id_schedule")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal idSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_config", nullable = false)
    @JsonIgnore
    private ConfigRouting configRouting;

    @Column(name = "quota")
    private Long quota;

    @Column(name = "chanel_start")
    private Timestamp timeStart;

    @Column(name = "chanel_end")
    private Timestamp timeEnd;

    @Column(name = "day_id")
    private String dayId;

    @Column(name = "chanel_config")
    private String chanelConfig;

    @Column(name = "day_name")
    private String dayName;

    @JsonIgnore
    @Column(name = "create_date")
    private Timestamp createDate;

    @JsonIgnore
    @Column(name = "update_date")
    private Timestamp updateDate;

    @Override
    public String toString() {
        return "ScheduleRoute{" +
                "idSchedule=" + idSchedule +
                ", quota=" + quota +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", dayId='" + dayId + '\'' +
                ", chanelConfig='" + chanelConfig + '\'' +
                ", dayName='" + dayName + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
