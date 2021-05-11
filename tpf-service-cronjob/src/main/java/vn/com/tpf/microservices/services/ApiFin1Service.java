package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.models.apiFin1.AppStatusModel;
import vn.com.tpf.microservices.models.apiFin1.ResponseApiFin1Model;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ApiFin1Service {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;

    private RestTemplate restTemplateESB;

    @Value("${spring.url.esb.get-status}")
    private String urlGetStatus;

    @Value("${spring.url.esb.getDocumentsAttachedToApplication}")
    private String urlGetDocumentsAttachedToApplication;

    @Value("${spring.url.esb.getDocumentImage}")
    private String urlGetDocumentImage;

    @PostConstruct
    private void init() {
        SimpleClientHttpRequestFactory callESB = new SimpleClientHttpRequestFactory();
        callESB.setConnectTimeout(600_000);
        callESB.setReadTimeout(600_000);
        restTemplateESB = new RestTemplate(callESB);
    }

    public JsonNode callApiF1(String url, JsonNode application){
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("clientId", "1");
        headers.add("sign", "1");
        headers.add("Content-Type", "application/json");
        ObjectNode logInfo = mapper.createObjectNode();
        try {
            HttpEntity<?> payload = new HttpEntity<>(application, headers);

            ObjectNode log = mapper.createObjectNode();
            log.put("type", "[==REQUEST-ESB==]");
            log.put("url", url);
            log.put("headers", headers.toString());
            ObjectNode appLog = application.deepCopy();

            if (appLog.hasNonNull("documents")){
                appLog.put("documents", "have document");
            }
            if (!appLog.findPath("queryModuleDocumentDetailsVO").isMissingNode() && appLog.findPath("queryModuleDocumentDetailsVO").hasNonNull("document")){
                appLog.findParent("document").put("document", "have document");
            }
            log.set("body", appLog);
            logInfo.set("request", log);

            ResponseEntity<String> response = restTemplateESB.postForEntity(url, payload , String.class);

            log = mapper.createObjectNode();
            log.put("type", "[==RESPONSE-ESB==]");
            log.put("headers", headers.toString());
            if (StringUtils.hasLength(response.toString()) && response.toString().length() >= 2000){
                log.put("body", response.toString().substring(0, 2000));
            }else{
                log.put("body", response.toString());
            }
            logInfo.set("response", log);

            String bodyString = response.getBody();
            if(StringUtils.hasLength(bodyString)){
                bodyString = bodyString.replaceAll("\u00A0", "");
            }

            JsonNode result = mapper.readTree(bodyString);

            log = mapper.createObjectNode();
            log.put("type", "[==RESPONSE-ESB-PARSED==]");
            log.put("body", bodyString);
            logInfo.set("response-parse", log);
            return result;
        }catch (Exception e){
            ObjectNode log = mapper.createObjectNode();
            log.put("type", "[==EXCEPTION==]");
            log.put("body", e.toString());
            logInfo.set("exception", log);
            return mapper.createObjectNode().put("errMsg", e.toString()).set("appConvert", application);
        }finally {
           //log.info("{}", logInfo);
        }
    }

    public AppStatusModel apiF1_AppStatus(String applicationId)
    {
        String sLog="apiF1_AppStatus: " + applicationId;
        try
        {
            JsonNode body = mapper.convertValue(Map.of("loanApplicationNumber", applicationId), JsonNode.class);
            JsonNode result = callApiF1(urlGetStatus, body);

            sLog +=";RESPONSE: " + mapper.writeValueAsString(result);

            AppStatusModel appStatusModel=mapper.convertValue(result,AppStatusModel.class);

            return  appStatusModel;
        }catch (Exception e)
        {
            sLog +="; ERROR: " + e.toString();
            return null;
        }
        finally {
           // log.info(sLog);
        }
    }

    public ResponseApiFin1Model apiF1_getDocumentsAttachedToApplication(String applicationId)
    {
        String sLog="apiF1_getDocumentsAttachedToApplication: " + applicationId;
        try
        {
            ObjectNode getDocuments = mapper.createObjectNode();

            List<String> listOfStatus = Arrays.asList("Received", "Pending");
            ArrayNode listOfStatusNode  = mapper.convertValue(listOfStatus, ArrayNode.class);

            getDocuments.put("applicationSourcingType","APPLICATION");
            getDocuments.put("applicationSourcingNumber",applicationId);
            getDocuments.put("referenceType","Customer");
            getDocuments.put("productProcessor","EXTERNAL");
            getDocuments.put("listOfStatus",listOfStatusNode);

            JsonNode body = mapper.convertValue(getDocuments, JsonNode.class);
            JsonNode result = callApiF1(urlGetDocumentsAttachedToApplication, body);

            sLog +=";RESPONSE: " + mapper.writeValueAsString(result);

            ResponseApiFin1Model responseApiFin1Model=mapper.convertValue(result,ResponseApiFin1Model.class);

            System.out.println(sLog);

            return responseApiFin1Model;
        }catch (Exception e)
        {
            sLog +="; ERROR: " + e.toString();
            return null;
        }
        finally {
           // log.info(sLog);
        }
    }

    public ResponseApiFin1Model apiF1_getDocuments(String applicationId, String documentId)
    {
        String sLog="apiF1_getDocuments: " + applicationId +", documentId: " + documentId;
        try
        {
            ObjectNode getDocuments = mapper.createObjectNode();

            getDocuments.put("traceNo", UUID.randomUUID().toString());
            getDocuments.put("storedDocumentRefId",documentId);
            getDocuments.put("productProcessor","EXTERNAL");

            JsonNode body = mapper.convertValue(getDocuments, JsonNode.class);
            JsonNode result = callApiF1(urlGetDocumentImage, body);

            sLog +=";RESPONSE: " + mapper.writeValueAsString(result);

            ResponseApiFin1Model responseApiFin1Model=mapper.convertValue(result,ResponseApiFin1Model.class);

            System.out.println(sLog);

            return responseApiFin1Model;
        }catch (Exception e)
        {
            sLog +="; ERROR: " + e.toString();
            return null;
        }
        finally {
            //log.info(sLog);
        }
    }
}
