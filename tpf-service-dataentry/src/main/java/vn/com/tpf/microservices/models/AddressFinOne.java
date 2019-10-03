package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "address")
public class AddressFinOne {

    private String _id;
    @JsonProperty("cityName")
    private String cityName;
    private List<AddressFinOneSub> data;

}