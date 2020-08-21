package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "log_choice_routing", schema = "routing")
@Data
public class LogChoiceRouting {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "id_log")
    private String idLog;

    @Column(name = "chanel_id")
    private String chanelId;

    @Column(name = "routing_number")
    private String routingNumber;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "app_number")
    private String appNumber;
}
