package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "dataentry_raisequery")
public class RaiseQueryModel {
        @Id
        private String id;

        private String queryId;
        private String queryCode;
        private String queryName;
        private boolean tatHold;
        private String raiseBy;
        private String raiseTo;
        private String raiseOn;
        private String queryStatus;
        private String applicationNo;
        private String stage;
        private String comment;
        private String remarks;
        private int clientId;
        @CreatedDate
        private Date createdDate;
        @LastModifiedDate
        private Date lastModifiedDate;
}
