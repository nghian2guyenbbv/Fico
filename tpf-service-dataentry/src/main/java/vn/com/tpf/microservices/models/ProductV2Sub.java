package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
public class ProductV2Sub {
    private String name;
    private String type;
}