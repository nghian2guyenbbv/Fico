package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class RequestImportFile {

    private String userLogin;
    private MultipartFile urlFile;
    private String teamName;
}
