package vn.com.tpf.microservices.models;

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
    private String idHistory;

    @Column(name = "chanel_name")
    private String chanelName;

    @Column(name = "chanel_config")
    private String chanelConfig;

    @Column(name = "quota")
    private String quota;

    @Column(name = "chanel_start")
    private Timestamp timeStart;

    @Column(name = "chanel_end")
    private Timestamp timeEnd;

    @Column(name = "day_id")
    private Timestamp dayId;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @Column(name = "user_udapte")
    private Timestamp userUpdate;
}
