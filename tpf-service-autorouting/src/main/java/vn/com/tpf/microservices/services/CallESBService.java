package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class CallESBService {
    private RestTemplate restTemplate;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;


    @PostConstruct
    private void init() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(300_000);
        simpleClientHttpRequestFactory.setReadTimeout(300_000);
        ClientHttpRequestFactory clientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);
        restTemplate = new RestTemplate(clientHttpRequestFactory);

    }

    public JsonNode callApiF1(String url, JsonNode body) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("clientId", "1");
        headers.add("sign", "1");
        headers.add("Content-Type", "application/json");
        ObjectNode logInfo = mapper.createObjectNode();

        try {
            HttpEntity<?> payload = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, payload , String.class);

            String bodyString = response.getBody();
            if(StringUtils.hasLength(bodyString)){
                bodyString = bodyString.replaceAll("\u00A0", "");
            }

            ObjectNode log = mapper.createObjectNode();
            JsonNode result = mapper.readTree(bodyString);

            log.put("type", "[==RESPONSE-ESB-PARSED==]");
            log.put("body", bodyString);
            logInfo.set("response-parse", log);
            return result;

        } catch (Exception e) {
            ObjectNode log = mapper.createObjectNode();
            log.put("type", "[==EXCEPTION==]");
            log.put("body", e.toString());
                logInfo.set("exception", log);
            return mapper.createObjectNode().put("errMsg", e.toString());
        } finally {
            log.info("{}", logInfo);
        }
    }
}
