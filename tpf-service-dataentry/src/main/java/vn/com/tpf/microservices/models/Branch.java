package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "branch")
public class Branch {

    private String _id;
    private String branchName;
    private String branchCode;

}