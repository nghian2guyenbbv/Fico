package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "history_config", schema = "routing")
@Data
public class HistoryConfig {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "id_history")
    private Long idHistory;

    @Column(name = "id_config")
    private String idConfig;

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


    @Column(name = "user_updated")
    private String userUpdated;


    @Column(name = "user_update_date")
    private Timestamp userUpdateDate;
}
