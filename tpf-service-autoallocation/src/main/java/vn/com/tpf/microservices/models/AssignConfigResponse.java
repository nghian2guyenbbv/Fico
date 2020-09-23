package vn.com.tpf.microservices.models;

import java.util.Map;

public class AssignConfigResponse {
    private String stageName;
    private Map<String, AssignConfigProductResponse> products;

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Map<String, AssignConfigProductResponse> getProducts() {
        return products;
    }

    public void setProducts(Map<String, AssignConfigProductResponse> products) {
        this.products = products;
    }
}
