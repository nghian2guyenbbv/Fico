package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routing_config", schema = "routing")
@Data
public class ConfigRouting {

    @Id
    @Column(name = "id_config")
    private String idConfig;

    @Column(name = "chanel_name")
    private String chanelName;

    // 0 - Auto & 1 - API
    @Column(name = "chanel_config")
    private String chanelConfig;

    @Column(name = "create_date")
    private Timestamp create_date;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "configRouting")
    private List<ScheduleRoute> scheduleRoutes = new ArrayList<>();

    @Override
    public String toString() {
        return "ConfigRouting{" +
                "idConfig='" + idConfig + '\'' +
                ", chanelName='" + chanelName + '\'' +
                ", chanelConfig='" + chanelConfig + '\'' +
                ", create_date=" + create_date +
                ", scheduleRoutes=" + scheduleRoutes +
                '}';
    }
}
