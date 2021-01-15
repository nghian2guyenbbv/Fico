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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Mobility;
import vn.com.tpf.microservices.models.MobilityField;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.url.mobility-push-app-id}")
	private String urlMobilityPushAppId;

	@Value("${spring.url.mobility-push-data-field}")
	private String urlMobilityPushDataField;

	@Value("${spring.url.uploadfile}")
	private String urlUploadfile;

	@Value("${spring.url.mobility-push-comment-app}")
	private String urlMobilityPushCommentApp;

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
					data.path("documentUrlDownload").asText());
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

	public ObjectNode pushAppIdOfLeadId(Mobility mobility, String request_id) {
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
			data.put("leadId", String.format("%s", mobility.getLeadId()));
			data.put("appId", mobility.getAppId());
			request.set("data", data);

			HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(request, requestHeaders);

			ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(urlMobilityPushAppId, HttpMethod.POST,
					requestEntity, ObjectNode.class);

			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("bodyRequest", urlMobilityPushAppId + request);
			dataLogReq.put("func", "[Req] Push AppId Of LeadId to ESB");
			dataLogReq.set("payload", request);
			log.info("{}", dataLogReq);

			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-Mobility==]");
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
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-Mobility==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", request);
			log.info("{}", dataLogRes);
			ObjectNode response = mapper.createObjectNode();
			response.put("resultCode", 500);
			return response;
		}
	}


	public ObjectNode pushCeateFieldEsb(ArrayNode mobilityFields, String request_id) {

		ObjectNode request =  mapper.createObjectNode();

		try {
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			requestHeaders.set("clientId", "1");
			requestHeaders.set("sign", "1");
			request.put("request_id", request_id);
			request.put("date_time", ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
			request.set("data", mobilityFields);
			HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(request, requestHeaders);
			ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(urlMobilityPushDataField, HttpMethod.POST,
					requestEntity, ObjectNode.class);



			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST-MobilityField==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("bodyRequest", urlMobilityPushDataField + request);
			dataLogReq.put("func", "Push CeateField to ESB");
			dataLogReq.set("payload", request);
			log.info("{}", dataLogReq);

			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-MobilityField==]");
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
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-MobilityField==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", request);
			log.info("{}", dataLogRes);
			ObjectNode response = mapper.createObjectNode();
			response.put("resultCode", 500);
			return response;
		}
	}

	public ObjectNode pushCommentApp(JsonNode CommentApp) {
		ObjectNode request = mapper.createObjectNode();

		try {

			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			requestHeaders.set("clientId", "1");
			requestHeaders.set("sign", "1");
			ObjectNode data = mapper.createObjectNode();
			data.put("commentId",  CommentApp.path("data").path("comment").path(0).path("commentId"));
			data.put("type",  CommentApp.path("data").path("comment").path(0).path("type"));
			data.put("code",  CommentApp.path("data").path("comment").path(0).path("code"));
			data.put("stage",  CommentApp.path("data").path("comment").path(0).path("stage"));
			data.put("request",  CommentApp.path("data").path("comment").path(0).path("request"));
			request.set("applicationId", CommentApp.path("data").path("applicationId"));
			request.set("comment", data);

			HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(request, requestHeaders);

			ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(urlMobilityPushCommentApp, HttpMethod.POST,
					requestEntity, ObjectNode.class);

			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("bodyRequest", urlMobilityPushCommentApp + request);
			dataLogReq.put("func", "[Req] Push comment app to ESB");
			dataLogReq.set("payload", request);
			log.info("{}", dataLogReq);

			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-Mobility==]");
			dataLogReq.put("func", "[Res] Push comment app  to ESB");
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
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE-Mobility==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", request);
			log.info("{}", dataLogRes);
			ObjectNode response = mapper.createObjectNode();
			response.put("resultCode", 500);
			return response;
		}
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

			log.set("body", appLog);
			logInfo.set("request", log);
			ResponseEntity<String> response = restTemplate.postForEntity(url, payload , String.class);
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
			log.info("{}", logInfo);
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