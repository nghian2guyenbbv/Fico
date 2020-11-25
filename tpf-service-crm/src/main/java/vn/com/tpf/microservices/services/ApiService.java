package vn.com.tpf.microservices.services;

import java.net.URLConnection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Crm;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.url.crm-user-login}")
	private String urlCrmUserLogin;

	@Value("${spring.url.crm-push-app-id}")
	private String urlCrmPushAppId;
	
	@Value("${spring.url.crm-push-data-field}")
	private String urlCrmPushDataField;

	@Value("${spring.crm-username}")
	private String crmUsername;

	@Value("${spring.crm-password}")
	private String crmPassword;

	@Value("${spring.url.webview-crm}")
	private String urlWebViewCrm;

	@Value("${spring.url.webview-crm-replace}")
	private String urlWebViewCrmReplace;

	@Value("${spring.url.uploadfile}")
	private String urlUploadfile;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;

	public JsonNode uploadDocumentInternal(JsonNode document, JsonNode documentDbInfo) {
		return uploadDocument(urlUploadfile, (ObjectNode) document, documentDbInfo);
	}

	private JsonNode uploadDocument(String url, ObjectNode data, JsonNode documentDbInfo) {
		try {
			data.put("documentUrlDownload",
					data.path("documentUrlDownload").asText().replace(urlWebViewCrm, urlWebViewCrmReplace));
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.set("payload", data);

			final String documentCode = data.path("documentCode").asText();
			final String documentFilename = data.path("documentFilename").asText();
			final String documentFileExtension = data.path("documentFileExtension").asText();
			final String documentUrlDownload = data.path("documentUrlDownload").asText();
			
			String fileName = "";
			if (documentCode.isBlank()) {
				fileName = documentFilename.concat(".").concat(documentFileExtension);
				
			} else {
				fileName = documentCode.concat(".").concat(documentFileExtension);
			}
			
			log.info("{}", dataLogReq);
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<byte[]> responseDownload = restTemplate.exchange(documentUrlDownload, HttpMethod.GET, entity,
					byte[].class);
			if (!responseDownload.getStatusCode().is2xxSuccessful())
				return mapper.createObjectNode().put("resultCode", 404).put("message",
						String.format("Cann't download file % from partnet % ", fileName, documentUrlDownload));
			byte[] byteArrayBase64 = responseDownload.getBody();
			if (!validFileSize(byteArrayBase64.length, documentDbInfo.path("sizeLimit").asDouble()))
				return mapper.createObjectNode().put("resultCode", 404).put("message",
						String.format("%s file size %s over limit %s", documentCode, byteArrayBase64.length,
								String.format("%sMb", documentDbInfo.path("sizeLimit").asDouble())));
			headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(fileName).build();
			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			fileMap.add("Content-Type", URLConnection.getFileNameMap().getContentTypeFor(fileName));
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(byteArrayBase64, fileMap);

			MultiValueMap<String, Object> bodyRequest = new LinkedMultiValueMap<>();
			bodyRequest.add("file", fileEntity);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyRequest, headers);

			ResponseEntity<?> responseUpload = restTemplate.exchange(url, HttpMethod.POST, requestEntity, List.class);
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
			dataLogRes.set("status", mapper.convertValue(responseUpload.getStatusCode(), JsonNode.class));
			dataLogRes.set("payload", data);
			dataLogRes.set("result", mapper.convertValue(responseUpload.getBody(), ArrayNode.class));

			log.info("{}", dataLogRes);
			if (responseUpload.getStatusCode().is2xxSuccessful())
				return mapper.createObjectNode().put("resultCode", 200).set("data",
						mapper.convertValue(responseUpload.getBody(), ArrayNode.class).get(0));
			return mapper.createObjectNode().put("resultCode", 500).put("message",
					String.format("upload tpf failed  %s", mapper.writeValueAsString(responseUpload.getBody())));
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

	/// validFileSize
	private boolean validFileSize(int byteInput, double mbFileLimit) {
		final int WEIGHT = 1000000;
		return byteInput <= mbFileLimit * WEIGHT;
	}

	public ObjectNode pushAppIdOfLeadId(Crm crm, String request_id) {
		ObjectNode request = mapper.createObjectNode();

		try {

			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			request.put("request_id", request_id);
			request.put("date_time", ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
			requestHeaders.set("clientId", "1");
			requestHeaders.set("sign", "1");
			ObjectNode data = mapper.createObjectNode();
			data.put("leadId", String.format("%s", crm.getLeadId()));
			data.put("appId", crm.getAppId());
			data.put("description", crm.getAutomationDescription());
			request.set("data", data);

			HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(request, requestHeaders);

			ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(urlCrmPushAppId, HttpMethod.POST,
					requestEntity, ObjectNode.class);
			
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("bodyRequest", urlCrmPushAppId + request);
			dataLogReq.put("func", "[Req] Push AppId Of LeadId to ESB");
			dataLogReq.set("payload", request);
			log.info("{}", dataLogReq);

			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-Crm==]");
			dataLogReq.put("func", "[Res] Push AppId Of LeadId to ESB");
			dataLogRes.set("status", mapper.convertValue(responseEntity.getStatusCode(), JsonNode.class));
			dataLogRes.set("Body response", mapper.convertValue(responseEntity.getBody(), JsonNode.class));
			dataLogRes.set("payload", request);
			log.info("{}", dataLogRes);
			
			ObjectNode response = mapper.createObjectNode();
			if (responseEntity.getStatusCode().is2xxSuccessful())
				response.put("resultCode", 200);
			else
				response.put("resultCode", responseEntity.getStatusCodeValue());
			return response;
		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-Crm==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", request);
			log.info("{}", dataLogRes);
			ObjectNode response = mapper.createObjectNode();
			response.put("resultCode", 500);
			return response;
		}
	}
	
	
	public ObjectNode pushCeateFieldEsb(ArrayNode crmFields, String request_id) {

		ObjectNode request =  mapper.createObjectNode(); 

		try {
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			requestHeaders.set("clientId", "1");
			requestHeaders.set("sign", "1");
			request.put("request_id", request_id);
			request.put("date_time", ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
			request.set("data", crmFields);
			HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(request, requestHeaders);
			ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(urlCrmPushDataField, HttpMethod.POST,
					requestEntity, ObjectNode.class);
			

			
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST-CrmField==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("bodyRequest", urlCrmPushDataField + request);
			dataLogReq.put("func", "Push CeateField to ESB");
			dataLogReq.set("payload", request);
			log.info("{}", dataLogReq);

			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-CrmField==]");
			dataLogReq.put("func", "Push CeateField to ESB");
			dataLogRes.set("status", mapper.convertValue(responseEntity.getStatusCode(), JsonNode.class));
			dataLogRes.set("Body response", mapper.convertValue(responseEntity.getBody(), JsonNode.class));
			dataLogRes.set("payload", request);
			log.info("{}", dataLogRes);
			
			ObjectNode response = mapper.createObjectNode();
			if (responseEntity.getStatusCode().is2xxSuccessful())
				response.put("resultCode", 200);
			else
				response.put("resultCode", responseEntity.getStatusCodeValue());
			return response;
		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-CrmField==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", request);
			log.info("{}", dataLogRes);
			ObjectNode response = mapper.createObjectNode();
			response.put("resultCode", 500);
			return response;
		}
	}


//	private ObjectNode getPartnerAccessToken() {
//		ObjectNode user = mapper.createObjectNode();
//		try {
//			HttpHeaders requestHeaders = new HttpHeaders();
//			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//			requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//			user.put("username", mobilityUsername);
//			user.put("password", mobilityPassword);
//
//			HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(user, requestHeaders);
//
//			ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(urlMobilityUserLogin, HttpMethod.POST,
//					requestEntity, ObjectNode.class);
//			JsonNode responseApi = mapper.convertValue(responseEntity.getBody(), JsonNode.class);
//			
//			ObjectNode dataLogRes = mapper.createObjectNode();
//			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
//			dataLogRes.set("status", mapper.convertValue(responseEntity.getStatusCode(), JsonNode.class));
//			dataLogRes.set("payload", user);
//			dataLogRes.set("result",responseApi);
//
//			log.info("{}", dataLogRes);
//
//			ObjectNode response = mapper.createObjectNode();
//
//			response.set("data", responseApi);
//
//			if (responseEntity.getStatusCode().is2xxSuccessful())
//				response.put("resultCode", 200);
//			else
//				response.put("resultCode", responseEntity.getStatusCodeValue());
//			return response;
//		} catch (Exception e) {
//			ObjectNode dataLogRes = mapper.createObjectNode();
//			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
//			dataLogRes.put("status", 500);
//			dataLogRes.put("result", e.toString());
//			dataLogRes.set("payload", user);
//			log.info("{}", dataLogRes);
//			
//			ObjectNode response = mapper.createObjectNode();
//			response.put("resultCode", 500);
//			response.set("data", mapper.convertValue(Map.of("messagess", e.getMessage()), JsonNode.class));
//			return response;
//		}
//	}

}