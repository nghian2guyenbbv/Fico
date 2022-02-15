package vn.com.tpf.microservices.models.Automation;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection="automation_accountfinone")
public class AccountFinOneDTO {
        @Id
        private String id;

        private String username;
        private String password;
        private String project;
        private int active;
        private int status;
        private Date createDate;
        private String funcAutomation;
}
