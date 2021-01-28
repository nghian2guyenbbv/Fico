package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "schedule_routing", schema = "routing")
@Data
public class ScheduleRoute {

    @Id
    @Column(name = "id_schedule")
    private String idSchedule;

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

    // 0 - Auto & 1 - API
    @Column(name = "chanel_config")
    private String chanelConfig;

    @Column(name = "day_name")
    private String dayName;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "update_date")
    private Timestamp updateDate;


    @Column(name = "user_updated")
    private String userUpdated;


    @Column(name = "user_update_date")
    private Timestamp userUpdateDate;
}
