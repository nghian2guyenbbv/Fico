package vn.com.tpf.microservices.services;

import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Smartnet;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.url.smartnet-user-login}")
	private String urlSmartnetUserLogin;

	@Value("${spring.url.smartnet-push-app-id}")
	private String urlSmartnetPushAppId;
	
	@Value("${spring.smartnet-username}")
	private String smartnetUsername;
	
	@Value("${spring.smartnet-password}")
	private String smartnetPassword;
	
	@Value("${spring.url.uploadfile}")
	private String urlUploadfile;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;
	
	@PostConstruct
	public void init() throws JsonProcessingException {
		 	
	}
	
	
	
	
	public JsonNode uploadDocumentInternal(JsonNode document , JsonNode  documentDbInfo ) {
		return uploadDocument(urlUploadfile, document ,documentDbInfo);
	}

	
	private JsonNode uploadDocument(String url, JsonNode data , JsonNode documentDbInfo ) {
		try {		
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.set("payload",data);
			log.info("{}", dataLogReq);
			
			final String documentCode = data.path("documentCode").asText();
			final String documentFileExtension = data.path("documentFileExtension").asText();
			final String documentUrlDownload = data.path("documentUrlDownload").asText();	
			final String fileName = documentCode.concat(".").concat(documentFileExtension);
						
			
			HttpHeaders headers = new HttpHeaders();
	           headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
	           HttpEntity<String> entity = new HttpEntity<>(headers);
	           ResponseEntity<byte[]> responseDownload = restTemplate
	                                                         .exchange(documentUrlDownload, HttpMethod.GET, entity, byte[].class);
	           if(!responseDownload.getStatusCode().is2xxSuccessful())
	            	return 	 mapper.createObjectNode().put("resultCode", 404).put("message", String.format("Cann't download file % from partnet % ",fileName, documentUrlDownload));	   
	           byte[] byteArrayBase64  = responseDownload.getBody();	
	           if(!validFileSize(byteArrayBase64.length, documentDbInfo.path("sizeLimit").asDouble()))
	        	   return  mapper.createObjectNode().put("resultCode", 404).put("message", String.format("%s file size %s over limit %s", documentCode ,byteArrayBase64.length,String.format("%sMb", documentDbInfo.path("sizeLimit").asDouble())));
	           headers = new HttpHeaders();
	           headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	           MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
	           ContentDisposition contentDisposition = ContentDisposition
	                .builder("form-data").name("file")
	                .filename(fileName).build();
		        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
		        fileMap.add("Content-Type", URLConnection.getFileNameMap().getContentTypeFor(fileName));
		        HttpEntity<byte[]> fileEntity = new HttpEntity<>(byteArrayBase64, fileMap);
	
		        MultiValueMap<String, Object> bodyRequest = new LinkedMultiValueMap<>();
		        bodyRequest.add("file", fileEntity);
		
		        HttpEntity<MultiValueMap<String, Object>> requestEntity =
	                new HttpEntity<>(bodyRequest, headers);
	   
	            ResponseEntity<?> responseUpload = restTemplate.exchange(
	            		url,
	                    HttpMethod.POST,
	                    requestEntity,
	                    List.class);
	            ObjectNode dataLogRes = mapper.createObjectNode();
				dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
				dataLogRes.set("status", mapper.convertValue(responseUpload.getStatusCode(), JsonNode.class));
				dataLogRes.set("payload", data);
				dataLogRes.set("result", mapper.convertValue(responseUpload.getBody(), ArrayNode.class));
				
				log.info("{}", dataLogRes);
	            if(responseUpload.getStatusCode().is2xxSuccessful()) 
	            	return 	 mapper.createObjectNode().put("resultCode", 200).set("data", mapper.convertValue( responseUpload.getBody(), ArrayNode.class).get(0));
            return mapper.createObjectNode().put("resultCode", 500).put("message", String.format("upload tpf failed  %s", mapper.writeValueAsString(responseUpload.getBody())));
		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", data);
			log.info("{}", dataLogRes);

			return mapper.createObjectNode().put("resultCode", 500).put("message", e.getMessage());
		}
	}
	
	
	
	private boolean validFileSize(int byteInput, double mbFileLimit) {
		final int WEIGHT = 1000000;
		return byteInput <= mbFileLimit * WEIGHT;
	}
	

	public ObjectNode pushAppIdOfLeadId(Smartnet smartnet) {
		try {
		ObjectNode authenInfo = getPartnerAccessToken();
		if(authenInfo.path("resultCode").asInt() != 200) 
			return authenInfo;
		
			
		HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization", String.format("Bearer %s", authenInfo.path("data").path("access_token").asText()));
     
        ObjectNode request = mapper.createObjectNode();
        request.put("LEAD_ID",String.format("%s",smartnet.getLeadId()));
        request.put("APP_ID",smartnet.getAppId());
      
        HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(request, requestHeaders);

        ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(
        		urlSmartnetPushAppId,
                HttpMethod.POST,
                requestEntity,
                ObjectNode.class
        );
        JsonNode responseApi =   mapper.convertValue(responseEntity.getBody(), JsonNode.class);
        ObjectNode response   = mapper.createObjectNode();
        response   = mapper.createObjectNode();
        
        response.set("data", responseApi);
        
        if (responseEntity.getStatusCode().is2xxSuccessful())
        	response.put("resultCode", 200);
        else
        	response.put("resultCode", responseEntity.getStatusCodeValue());
        	
    	return response;
		} catch (Exception e) {
			ObjectNode response = mapper.createObjectNode();
			response.put("resultCode", 500);
			response.set("data", mapper.convertValue(Map.of("messagess", e.getMessage()), JsonNode.class));
			return response;
		}
	}
	
	
	private ObjectNode getPartnerAccessToken() {
		try {
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			ObjectNode user = mapper.createObjectNode();
			user.put("username", smartnetUsername);
			user.put("password", smartnetPassword);

			HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(user, requestHeaders);

			ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(urlSmartnetUserLogin, HttpMethod.POST,
					requestEntity, ObjectNode.class);
			JsonNode responseApi = mapper.convertValue(responseEntity.getBody(), JsonNode.class);

			ObjectNode response = mapper.createObjectNode();
			
			response.set("data", responseApi);

			if (responseEntity.getStatusCode().is2xxSuccessful())
				response.put("resultCode", 200);
			else
				response.put("resultCode", responseEntity.getStatusCodeValue());
			return response;
		} catch (Exception e) {
			ObjectNode response = mapper.createObjectNode();
			response.put("resultCode", 500);
			response.set("data", mapper.convertValue(Map.of("messagess", e.getMessage()), JsonNode.class));
			return response;
		}
	}

}