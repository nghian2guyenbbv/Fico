package vn.com.tpf.microservices.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import vn.com.tpf.microservices.models.QuickLead.Application;

import java.util.HashMap;
import java.util.Map;

public class DataInitial {
    public static Map<String, Object> getDataFromDE_QL(Application application) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("ApplicationDTO", application);
        return map;
    }
}
