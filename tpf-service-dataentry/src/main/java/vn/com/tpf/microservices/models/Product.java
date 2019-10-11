package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "dataentry_product")
public class Product {
    private String productName;
    private List<String> documentName;
}