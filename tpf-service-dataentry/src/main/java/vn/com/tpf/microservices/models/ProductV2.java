package vn.com.tpf.microservices.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "dataentry_product_v2")
public class ProductV2 {
//    private String productName;
//    private List<Map<String, List<String>>> documentName;
//    int countProductName;

    private String productName;
    private List<ProductV2Sub> documentName;
    private List<ProductV2Sub> documentOthers;
    int countProductName;
}