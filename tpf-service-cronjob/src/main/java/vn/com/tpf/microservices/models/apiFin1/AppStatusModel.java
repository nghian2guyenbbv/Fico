package vn.com.tpf.microservices.models.apiFin1;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
public class AppStatusModel {
        @Id
        private String id;

        private String transactionId;
        private String responseCode;
        private String responseMessage;
        private String responseDate;
        private ResponseData responseData;
        @CreatedDate
        private Date createdDate;
        @LastModifiedDate
        private Date lastModifiedDate;

}

