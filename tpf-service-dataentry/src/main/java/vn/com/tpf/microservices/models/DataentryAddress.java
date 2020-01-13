package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "dataentry_address")
public class DataentryAddress {

    private String _id;
    private String cityName;
    private String postCode;
    private String areaName;
    private String areaCode;
    private String region;
    private String f1AreaId;
    private String f1AreaCode;
    private String f1ZipCode;

}