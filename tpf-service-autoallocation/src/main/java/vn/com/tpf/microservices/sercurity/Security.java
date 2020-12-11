package vn.com.tpf.microservices.sercurity;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public class Security {
    public boolean checkUserNameFromToken(JsonNode token, String userName) {
        return token.path("user_name").asText("").equals(userName);
    }
}
