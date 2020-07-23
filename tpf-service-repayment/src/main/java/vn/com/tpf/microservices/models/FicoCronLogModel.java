package vn.com.tpf.microservices.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fico_cron_log", schema = "payoo")
public class FicoCronLogModel {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id")
    private long id;

    @Column(name="filename")
    private String fileName;

    @Column(name="file_created_date")
    private Date fileCreatedDate;

    @Column(name="created_date")
    @CreatedDate
    private Date createdDate;

    @Column(name="last_modified_date")
    @LastModifiedDate
    private Date lastModifiedDate;
}
