package vn.com.tpf.microservices.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadDoc {
    public int index;
    public String urlPhoto;
}
