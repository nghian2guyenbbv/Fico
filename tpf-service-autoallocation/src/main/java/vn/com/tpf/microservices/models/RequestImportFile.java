package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class RequestImportFile {

    private String userLogin;
    private MultipartFile urlFile;
    private String teamNameUser;
}
