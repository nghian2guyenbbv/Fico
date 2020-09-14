package vn.com.tpf.microservices.models.GetAppStatus;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.print.attribute.standard.MediaSize;
import java.util.Date;

@Data
@Document(collection = "dataentry_appstatusinfo")
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

