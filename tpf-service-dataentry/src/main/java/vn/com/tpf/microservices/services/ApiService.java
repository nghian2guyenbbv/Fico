package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import vn.com.tpf.microservices.models.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.url.firstcheck}")
	private String urlFirstcheck;

	@Value("${spring.url.uploadfile}")
	private String urlUploadfile;

	@Value("${spring.url.digitex-documentapi}")
	private String urlDigitexDocumentApi;

	@Value("${spring.url.digitex-resumitdocumentapi}")
	private String urlDigitexResumitDocumentApi;

	@Value("${spring.url.digitex-cminfoapi}")
	private String urlDigitexCmInfoApi;

	@Value("${spring.url.digitex-resubmitcommentapi}")
	private String urlDigitexResubmitCommentApi;

	@Value("${spring.url.digitex-feedbackapi}")
	private String urlDigitexFeedbackApi;

	@Value("${spring.url.digitex-token}")
	private String digitexToken;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private RabbitMQService rabbitMQService;

	private RestTemplate restTemplateDownload;

	private RestTemplate restTemplate;

	private RestTemplate restTemplateFirstCheck;

	@PostConstruct
	private void init() {
		ClientHttpRequestFactory factoryDow = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplateDownload = new RestTemplate(factoryDow);

		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Arrays.asList(new HttpLogService()));

		SimpleClientHttpRequestFactory scrf = new SimpleClientHttpRequestFactory();
		scrf.setConnectTimeout(1000*120);
		scrf.setReadTimeout(1000*120);
		ClientHttpRequestFactory factoryFirstCheck = new BufferingClientHttpRequestFactory(scrf);
		restTemplateFirstCheck = new RestTemplate(factoryFirstCheck);
		restTemplateFirstCheck.setInterceptors(Arrays.asList(new HttpLogService()));
	}

//	public String firstCheck(JsonNode request, JsonNode token) {
//		Map<?, ?> data = Map.of("file", request.path("body"));
//		try {
//			Assert.notNull(request.get("body"), "no body");
//
//			List<DataentryAddress> dataAdd = new ArrayList<>();
//			Query query = new Query();
//			query.addCriteria(Criteria.where("areaCode").is(request.path("body").path("data").path("areaId").textValue()));
//			dataAdd = mongoTemplate.find(query, DataentryAddress.class);
//
//
////			FirstCheckRequest requestFirstCheck = mapper.treeToValue(request.path("body").path("data"), FirstCheckRequest.class);
//			FirstCheckRequest requestFirstCheck = new FirstCheckRequest();
//			requestFirstCheck.setProject_name("de");
//
//			requestFirstCheck.setRequest_id("de-"+ UUID.randomUUID().toString().substring(0,11));
//			requestFirstCheck.setFull_name(request.path("body").path("data").path("customerName").textValue());
//			requestFirstCheck.setCustomer_id(request.path("body").path("data").path("customerId").textValue());
//			requestFirstCheck.setDsa_code(request.path("body").path("data").path("dsaCode").textValue());
//			requestFirstCheck.setBank_card_number(request.path("body").path("data").path("bankCardNumber").textValue());
//			requestFirstCheck.setCurrent_address(request.path("body").path("data").path("currentAddress").textValue());
//			if (dataAdd.size() > 0) {
//				requestFirstCheck.setArea_code(dataAdd.get(0).getF1AreaCode());
//			}
//			requestFirstCheck.setBirthday("");
//			requestFirstCheck.setGender("");
//			requestFirstCheck.setPhoneNumber("");
//
//			String logsTimeOut = "TimeOut two minute: ";
//			try{
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//				HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(requestFirstCheck), headers);
//				ResponseEntity<?> res = restTemplateFirstCheck.postForEntity(urlFirstcheck, entity, Object.class);
//				JsonNode body = mapper.valueToTree(res.getBody());
//				logsTimeOut = "";
//
//				FirstCheckResponse firstCheckResponse = mapper.treeToValue(body, FirstCheckResponse.class);
//				FirstCheck firstCheck = new FirstCheck();
//				firstCheck.setRequest(requestFirstCheck);
//				firstCheck.setResponse(firstCheckResponse);
//				firstCheck.setCreatedBy(token.path("user_name").textValue());
//				mongoTemplate.save(firstCheck);
//
//				if (firstCheckResponse.getFirst_check_result().toUpperCase().equals("PASS")){
//					return "pass";
//				}else {
//					return "fail";
//				}
//			}catch (Exception ex){
//				FirstCheck firstCheck = new FirstCheck();
//				firstCheck.setRequest(requestFirstCheck);
//				firstCheck.setDescription(logsTimeOut + ex.toString());
//				firstCheck.setCreatedBy(token.path("user_name").textValue());
//				mongoTemplate.save(firstCheck);
//
//				return "pass";
//			}
//
//		} catch (HttpClientErrorException e) {
//			log.info("[==HTTP-LOG-RESPONSE==] : {}",
//					Map.of("payload", data, "status", e.getStatusCode(), "result", e.getResponseBodyAsString()));
//			return e.getMessage();
//
//		} catch (Exception e) {
//			log.info("[==HTTP-LOG-RESPONSE==] : {}", Map.of("payload", data, "status", 500, "result", e.getMessage()));
//			return e.getCause().toString();
//		}
//	}

	public String firstCheck(JsonNode request, JsonNode token) {
		Map<?, ?> data = Map.of("file", request.path("body"));
		try {
			Assert.notNull(request.get("body"), "no body");
			String areaCode = "";

			List<DataentryAddress> dataAdd = new ArrayList<>();
			Query query = new Query();
			query.addCriteria(Criteria.where("areaCode").is(request.path("body").path("data").path("areaId").textValue()));
			dataAdd = mongoTemplate.find(query, DataentryAddress.class);

//			FirstCheckRequest requestFirstCheck = mapper.treeToValue(request.path("body").path("data"), FirstCheckRequest.class);
			FirstCheckRequest requestFirstCheck = new FirstCheckRequest();
			requestFirstCheck.setProject_name("de");
			requestFirstCheck.setRequest_id("de-"+ UUID.randomUUID().toString().substring(0,11));
			requestFirstCheck.setFull_name(request.path("body").path("data").path("customerName").textValue());
			requestFirstCheck.setCustomer_id(request.path("body").path("data").path("customerId").textValue());
			requestFirstCheck.setDsa_code(request.path("body").path("data").path("dsaCode").textValue());
			requestFirstCheck.setBank_card_number(request.path("body").path("data").path("bankCardNumber").textValue());
			requestFirstCheck.setCurrent_address(request.path("body").path("data").path("currentAddress").textValue());
			if (dataAdd.size() > 0) {
				requestFirstCheck.setArea_code(dataAdd.get(0).getF1AreaCode());
				areaCode = dataAdd.get(0).getF1AreaCode();
			}
			requestFirstCheck.setBirthday("");
			requestFirstCheck.setGender("");
			requestFirstCheck.setPhoneNumber("");

			try{
				JsonNode resultFirstCheck = null;
				FirstCheckResponse firstCheckResponse = new FirstCheckResponse();
				JsonNode checkList = rabbitMQService.sendAndReceive("tpf-service-esb",
						Map.of("func", "getCheckList", "reference_id", request.path("body").path("request_id").textValue(), "param",
								Map.of("bank_card_number", request.path("body").path("data").path("bankCardNumber").textValue(),
										"dsa_code",request.path("body").path("data").path("dsaCode").textValue(),
										"area_code",areaCode)));

//				if (checkList.path("status").asInt() == 200) {
//					resultFirstCheck = mapper.valueToTree(checkList.path("data"));
//				}

				firstCheckResponse.setFirst_check_result(mapper.writeValueAsString(checkList));
				firstCheckResponse.setStatus(checkList.path("status").asInt());
				firstCheckResponse.setResult(checkList.path("data").path("result").asText(null));
				firstCheckResponse.setDescription(checkList.path("data").path("description").asText(null) +" : "+checkList.path("data").path("message").asText(null));

				FirstCheck firstCheck = new FirstCheck();
				firstCheck.setRequest(requestFirstCheck);
				firstCheck.setResponse(firstCheckResponse);
				firstCheck.setCreatedBy(token.path("user_name").textValue());
				mongoTemplate.save(firstCheck);

				if (checkList.path("status").asInt() == 200) {
					if (checkList.path("data").path("result").asInt() == 0){
						return "pass";
					}else {
						return "fail";
					}
				}else{
					return "pass";
				}
			}catch (Exception ex){
				FirstCheck firstCheck = new FirstCheck();
				firstCheck.setRequest(requestFirstCheck);
				firstCheck.setDescription(ex.toString());
				firstCheck.setCreatedBy(token.path("user_name").textValue());
				mongoTemplate.save(firstCheck);

				return "pass";
			}
		} catch (Exception e) {
			FirstCheck firstCheck = new FirstCheck();
			firstCheck.setDescription(e.toString());
			firstCheck.setCreatedBy(token.path("user_name").textValue());
			mongoTemplate.save(firstCheck);
			return "pass";
		}
	}

	public JsonNode retryUploadDigiTex(JsonNode request) {
		Map<?, ?> data = Map.of("file", request.path("body"));
		JsonNode jNode = null;

		boolean checkIdCard = false;
		boolean checkHousehold = false;
		try {
			if (request.path("body").path("data").path("applicationId").textValue() == null){
				ArrayNode documents = mapper.createArrayNode();
				MultiValueMap<String, Object> parts_02 =
						new LinkedMultiValueMap<String, Object>();

				Query query = new Query();
				query.addCriteria(Criteria.where("quickLeadId").is(request.path("body").path("data").path("quickLeadId").textValue()));
				Application checkExist = mongoTemplate.findOne(query, Application.class);

				List<QLDocument> dataUpload = checkExist.getQuickLead().getDocuments();
				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())){
						doc.put("file-name", "Personal-Image_" + item.getOriginalname());
						doc.put("md5", item.getMd5());
						documents.add(doc);

						HttpHeaders headers = new HttpHeaders();
						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
						HttpEntity<String> entity = new HttpEntity<>(headers);
						ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

						MultipartFile multipartFileToSend = new MockMultipartFile("Personal-Image_" + item.getOriginalname(),
								"Personal-Image_" + item.getOriginalname(), item.getContentType(), response.getBody());
						parts_02.add("Personal-Image", multipartFileToSend.getResource());
					}else if (item.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())){
						doc.put("file-name", "ACCA-Form_" + item.getOriginalname());
						doc.put("md5", item.getMd5());
						documents.add(doc);

						HttpHeaders headers = new HttpHeaders();
						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
						HttpEntity<String> entity = new HttpEntity<>(headers);
						ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

						MultipartFile multipartFileToSend = new MockMultipartFile("ACCA-Form_" + item.getOriginalname(),
								"ACCA-Form_" + item.getOriginalname(), item.getContentType(), response.getBody());
						parts_02.add("ACCA-Form", multipartFileToSend.getResource());
					}
				}

				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase())){
						if (!checkIdCard) {
							doc.put("file-name", "ID-Card_" + item.getOriginalname());

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
									"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


							MessageDigest md5 = MessageDigest.getInstance("MD5");
							byte[] digest = md5.digest(multipartFileToSend.getBytes());
							String hashString = new BigInteger(1, digest).toString(16);
							doc.put("md5", item.getMd5());
							documents.add(doc);


							parts_02.add("ID-Card", multipartFileToSend.getResource());

							checkIdCard = true;

						}
					}
					else if (item.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase())){
						if (!checkHousehold) {
							doc.put("file-name", "Household_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
									"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("Household", multipartFileToSend.getResource());

							checkHousehold = true;
						}
					}
				}

				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())){
						if (!checkIdCard) {
							doc.put("file-name", "ID-Card_" + item.getOriginalname());

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
									"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


							MessageDigest md5 = MessageDigest.getInstance("MD5");
							byte[] digest = md5.digest(multipartFileToSend.getBytes());
							String hashString = new BigInteger(1, digest).toString(16);
							doc.put("md5", item.getMd5());
							documents.add(doc);


							parts_02.add("ID-Card", multipartFileToSend.getResource());

							checkIdCard = true;
						}
					}
					else if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())){
						if (!checkHousehold) {
							doc.put("file-name", "Household_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
									"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("Household", multipartFileToSend.getResource());

							checkHousehold = true;
						}
					}
				}


				parts_02.add("description", Map.of("files", documents));

				try{
					HttpHeaders headers_DT = new HttpHeaders();
					headers_DT.set("authkey", digitexToken);
					headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<?> entity_DT = new HttpEntity<>(parts_02, headers_DT);
					ResponseEntity<?> res_DT = restTemplateDownload.postForEntity(urlDigitexDocumentApi, entity_DT, Object.class);

//					Map<String, List> map = mapper.readValue(res_DT.getBody().toString().replaceAll("\"\\{\\[","\\[").replaceAll("\\]\\}\"","\\]"), new TypeReference<Map<String, List>>() {});
//					Map<String, List> map = mapper.readValue(res_DT.getBody().toString(), new TypeReference<Map<String, List>>() {});
					Object map = mapper.valueToTree(res_DT.getBody());

					JsonNode outputDT = mapper.readTree(mapper.writeValueAsString(((JsonNode) map).get("output")));
					ArrayNode array = mapper.valueToTree(dataUpload);
					jNode = mergeFile(array, outputDT);

				} catch (Exception e) {
					log.info("[==HTTP-LOG-RESPONSE==Exception] : {}",
							Map.of("Exception: ", e.toString(), "payload", request));
					ObjectNode doc = mapper.createObjectNode();
					doc.put("uploadDigiTex", "FAIL");
					return doc;
				}

			}else{
				ArrayNode documents = mapper.createArrayNode();
				MultiValueMap<String, Object> parts_02 =
						new LinkedMultiValueMap<String, Object>();

				Query query = new Query();
				query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("data").path("applicationId").textValue()));
				Application checkExist = mongoTemplate.findOne(query, Application.class);
				List<QLDocument> dataUploadOld = checkExist.getQuickLead().getDocuments();
				List<QLDocument> dataUpload = checkExist.getQuickLead().getDocumentsComment();
				List<QLDocument> dataUploadAfterSubmit = checkExist.getQuickLead().getDocumentsAfterSubmit();

				if (checkExist.getApplicationInformation() != null) {//retry document after submit
					for (QLDocument item : dataUploadAfterSubmit) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())) {
							doc.put("file-name", "Personal-Image_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Personal-Image_" + item.getOriginalname(),
									"Personal-Image_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("Personal-Image", multipartFileToSend.getResource());
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
							doc.put("file-name", "ACCA-Form_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ACCA-Form_" + item.getOriginalname(),
									"ACCA-Form_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("ACCA-Form", multipartFileToSend.getResource());
						}
					}

					for (QLDocument item : dataUploadAfterSubmit) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase())) {
							if (!checkIdCard) {
								doc.put("file-name", "ID-Card_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
										"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


								parts_02.add("ID-Card", multipartFileToSend.getResource());

								checkIdCard = true;
							}
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase())) {
							if (!checkHousehold) {
								doc.put("file-name", "Household_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
										"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
								parts_02.add("Household", multipartFileToSend.getResource());

								checkHousehold = true;
							}
						}
					}

					for (QLDocument item : dataUploadAfterSubmit) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
							if (!checkIdCard) {
								doc.put("file-name", "ID-Card_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
										"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


								parts_02.add("ID-Card", multipartFileToSend.getResource());

								checkIdCard = true;

							}
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
							if (!checkHousehold) {
								doc.put("file-name", "Household_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
										"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
								parts_02.add("Household", multipartFileToSend.getResource());

								checkHousehold = true;
							}
						}
					}
				}else {
					for (QLDocument item : dataUpload) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())) {
							doc.put("file-name", "Personal-Image_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Personal-Image_" + item.getOriginalname(),
									"Personal-Image_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("Personal-Image", multipartFileToSend.getResource());
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
							doc.put("file-name", "ACCA-Form_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ACCA-Form_" + item.getOriginalname(),
									"ACCA-Form_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("ACCA-Form", multipartFileToSend.getResource());
						}
					}

					for (QLDocument item : dataUpload) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase())) {
							if (!checkIdCard) {
								doc.put("file-name", "ID-Card_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
										"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


								parts_02.add("ID-Card", multipartFileToSend.getResource());

								checkIdCard = true;
							}
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase())) {
							if (!checkHousehold) {
								doc.put("file-name", "Household_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
										"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
								parts_02.add("Household", multipartFileToSend.getResource());

								checkHousehold = true;
							}
						}
					}

					for (QLDocument item : dataUpload) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
							if (!checkIdCard) {
								doc.put("file-name", "ID-Card_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
										"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


								parts_02.add("ID-Card", multipartFileToSend.getResource());

								checkIdCard = true;

							}
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
							if (!checkHousehold) {
								doc.put("file-name", "Household_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
										"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
								parts_02.add("Household", multipartFileToSend.getResource());

								checkHousehold = true;
							}
						}
					}
				}


				parts_02.add("description", Map.of("files", documents));

				try{
					HttpHeaders headers_DT = new HttpHeaders();
					headers_DT.set("authkey", digitexToken);
					headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<?> entity_DT = new HttpEntity<>(parts_02, headers_DT);
					ResponseEntity<?> res_DT = restTemplateDownload.postForEntity(urlDigitexResumitDocumentApi, entity_DT, Object.class);

//					Map<String, List> map = mapper.readValue(res_DT.getBody().toString().replaceAll("\"\\{\\[","\\[").replaceAll("\\]\\}\"","\\]"), new TypeReference<Map<String, List>>() {});
//					Map<String, List> map = mapper.readValue(res_DT.getBody().toString(), new TypeReference<Map<String, List>>() {});
					Object map = mapper.valueToTree(res_DT.getBody());

					JsonNode outputDT = mapper.readTree(mapper.writeValueAsString(((JsonNode) map).get("output")));
					ArrayNode array = mapper.valueToTree(dataUpload);
					jNode = mergeFile(array, outputDT);

				} catch (Exception e) {
					log.info("[==HTTP-LOG-RESPONSE==Exception] : {}",
							Map.of("Exception: ", e.toString(), "payload", request));
					ObjectNode doc = mapper.createObjectNode();
					doc.put("uploadDigiTex", "FAIL");
					return doc;
				}
			}
		} catch (HttpClientErrorException e) {
			log.info("[==HTTP-LOG-RESPONSE==Exception] : {}",
					Map.of("Exception: ", e.toString()));
			ObjectNode doc = mapper.createObjectNode();
			doc.put("uploadDigiTex", "FAIL");
			return doc;

		} catch (Exception e) {
			log.info("[==HTTP-LOG-RESPONSE==Exception] : {}",
					Map.of("Exception: ", e.toString()));
			ObjectNode doc = mapper.createObjectNode();
			doc.put("uploadDigiTex", "FAIL");
			return doc;
		}
		return jNode;
	}

	public static ByteArrayInputStream statusReportToExcel (List<ReportStatus> report) throws IOException {
		String[] COLUMNs = {"App no.", "Status"};
		try(
				Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
		){
			CreationHelper createHelper = workbook.getCreationHelper();

			Sheet sheet = workbook.createSheet("statusreport");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(COLUMNs[col]);
				cell.setCellStyle(headerCellStyle);
			}

			// CellStyle for Age
			CellStyle ageCellStyle = workbook.createCellStyle();
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

			int rowIdx = 1;
			for (ReportStatus item : report) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(item.getAppNo());
				row.createCell(1).setCellValue(item.getStatus());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	public JsonNode mergeFile(JsonNode mainNode, JsonNode updateNode) {
		boolean checkIdCard = false;
		boolean checkHousehold = false;
		boolean checkPersonalImage = false;
		boolean checkACCAForm = false;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode resultNode = mapper.createArrayNode();
		for (JsonNode item : mainNode) {
			if (updateNode != null) {
				for (JsonNode item2 : updateNode) {
					if (item.findPath("originalname").textValue().toUpperCase().contains("TPF_ID Card".toUpperCase()) ||
							item.findPath("originalname").textValue().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
						if (item2.findPath("document-type").textValue().equals("ID-Card")) {
							if (!checkIdCard) {
								ObjectNode doc = mapper.createObjectNode();
								doc.put("originalname", item.findPath("originalname").textValue());
								doc.put("filename", item.findPath("filename").textValue());
								doc.put("md5", item.findPath("md5").textValue());
								doc.put("contentType", item.findPath("contentType").textValue());
								doc.put("urlid", item2.findPath("document-id").asText(null));
								((ArrayNode) resultNode).add(doc);

								checkIdCard=true;
							}
						}
					} else if (item.findPath("originalname").textValue().toUpperCase().contains("TPF_Family Book".toUpperCase()) ||
							item.findPath("originalname").textValue().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
						if (item2.findPath("document-type").textValue().equals("Household")) {
							if (!checkHousehold) {
								ObjectNode doc = mapper.createObjectNode();
								doc.put("originalname", item.findPath("originalname").textValue());
								doc.put("filename", item.findPath("filename").textValue());
								doc.put("md5", item.findPath("md5").textValue());
								doc.put("contentType", item.findPath("contentType").textValue());
								doc.put("urlid", item2.findPath("document-id").asText(null));
								((ArrayNode) resultNode).add(doc);

								checkHousehold = true;
							}
						}
					} else if (item.findPath("originalname").textValue().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())) {
						if (item2.findPath("document-type").textValue().equals("Personal-Image")) {
							if (!checkPersonalImage) {
								ObjectNode doc = mapper.createObjectNode();
								doc.put("originalname", item.findPath("originalname").textValue());
								doc.put("filename", item.findPath("filename").textValue());
								doc.put("md5", item.findPath("md5").textValue());
								doc.put("contentType", item.findPath("contentType").textValue());
								doc.put("urlid", item2.findPath("document-id").asText(null));
								((ArrayNode) resultNode).add(doc);

								checkPersonalImage = true;
							}
						}
					} else if (item.findPath("originalname").textValue().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
						if (item2.findPath("document-type").textValue().equals("ACCA-Form")) {
							if (!checkACCAForm) {
								ObjectNode doc = mapper.createObjectNode();
								doc.put("originalname", item.findPath("originalname").textValue());
								doc.put("filename", item.findPath("filename").textValue());
								doc.put("md5", item.findPath("md5").textValue());
								doc.put("contentType", item.findPath("contentType").textValue());
								doc.put("urlid", item2.findPath("document-id").asText(null));
								((ArrayNode) resultNode).add(doc);

								checkACCAForm = true;
							}
						}
					} else {
						ObjectNode doc = mapper.createObjectNode();
						doc.put("originalname", item.findPath("originalname").textValue());
						doc.put("filename", item.findPath("filename").textValue());
						doc.put("md5", item.findPath("md5").textValue());
						doc.put("contentType", item.findPath("contentType").textValue());
						((ArrayNode) resultNode).add(doc);

						break;
					}
				}
			}else{
				ObjectNode doc = mapper.createObjectNode();
				doc.put("originalname", item.findPath("originalname").textValue());
				doc.put("filename", item.findPath("filename").textValue());
				doc.put("md5", item.findPath("md5").textValue());
				doc.put("contentType", item.findPath("contentType").textValue());
				((ArrayNode) resultNode).add(doc);
			}
		}
		return resultNode;
	}


	public String callApiDigitexx2(String url, JsonNode data) {
		try {
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==DIGITEXX==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.set("payload", data);
			log.info("{}", dataLogReq);

//			String dataString = mapper.writeValueAsString(data);
//
//			JsonNode encrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
//					Map.of("func", "pgpEncrypt", "body", Map.of("project", "digitex", "data", data)));
//
//			ObjectNode dataLogReq2 = mapper.createObjectNode();
//			dataLogReq2.put("type", "[==HTTP-LOG-REQUEST==DIGITEXX=PGP=]");
//			dataLogReq2.set("payload", encrypt);
//			log.info("{}", dataLogReq2);
//
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Accept", "application/pgp-encrypted");
//			headers.set("Content-Type", "application/pgp-encrypted");
//			HttpEntity<String> entity = new HttpEntity<String>(encrypt.path("data").asText(), headers);
//			ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);
//
//			ObjectNode dataLogReq3 = mapper.createObjectNode();
//			dataLogReq3.put("type", "[==HTTP-LOG-RESPONSE==DIGITEXX=PGP=]");
//			dataLogReq3.set("payload", mapper.readTree(res.getBody().toString()));
//			log.info("{}", dataLogReq3);
//
//			JsonNode decrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
//					Map.of("func", "pgpDecrypt", "body", Map.of("project", "digitex", "data", res.getBody().toString())));
//
//			ObjectNode dataLogRes = mapper.createObjectNode();
//			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==DIGITEXX==]");
//			dataLogRes.set("status", mapper.convertValue(res.getStatusCode(), JsonNode.class));
//			dataLogRes.set("payload", data);
//			dataLogRes.put("result", decrypt.path("body").path("data").asText());
//
//			log.info("{}", dataLogRes);
//			return decrypt.path("data");

			//------------- test khong pgp -------

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.set("authkey", digitexToken);
			HttpEntity<?> entity = new HttpEntity<>(data.textValue(), headers);
			ResponseEntity<?> res = restTemplate.postForEntity(url, entity, Object.class);
			JsonNode body = mapper.valueToTree(res.getBody());

			if (body.path("error-code").asText(null) == null || body.path("error-code").textValue() == null){
				return null;
			}else{
				return body.path("error-code").asText(null) +  ": " + body.path("error-description").asText(null);
			}

		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==DIGITEXX==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", data);
			log.info("{}", dataLogRes);

			return "Call DigiTexx 500!";
		}
	}

	public JsonNode callApiDigitexx(String url, JsonNode data) {
		String referenceId = UUID.randomUUID().toString();
		try {
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==DATAENTRY-DIGITEXX==REQUEST==]");
			dataLogReq.put("referenceId", referenceId);
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.put("request_data", data);
			log.info("{}", dataLogReq);

			String dataString = mapper.writeValueAsString(data);

			JsonNode encrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
					Map.of("func", "pgpEncrypt", "body", Map.of("project", "digitex", "data", data)));

			ObjectNode dataLogReq2 = mapper.createObjectNode();
			dataLogReq2.put("type", "[==DATAENTRY-DIGITEXX==REQUEST=PGP=]");
			dataLogReq2.put("referenceId", referenceId);
			dataLogReq2.put("request_encrypt", encrypt);
			log.info("{}", dataLogReq2);

			HttpHeaders headers = new HttpHeaders();
			headers.set("authkey", digitexToken);
			headers.set("Accept", "application/pgp-encrypted");
			headers.set("Content-Type", "application/pgp-encrypted");
			HttpEntity<String> entity = new HttpEntity<String>(encrypt.path("data").asText(), headers);
			ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);

//			ObjectNode dataLogReq3 = mapper.createObjectNode();
//			dataLogReq3.put("type", "[==DATAENTRY-DIGITEXX==RESPONSE=PGP=]");
//			dataLogReq3.put("referenceId", referenceId);
//			dataLogReq3.set("payload", mapper.readTree(res.getBody()));
//			log.info("{}", dataLogReq3);

			JsonNode decrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
					Map.of("func", "pgpDecrypt", "body", Map.of("project", "digitex", "data", res.getBody().toString())));

			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==DATAENTRY-DIGITEXX==RESPONSE==]");
			dataLogRes.put("referenceId", referenceId);
			dataLogRes.put("status", mapper.convertValue(res.getStatusCode(), JsonNode.class));
			dataLogRes.put("response", res.getBody());
			dataLogRes.put("response_decrypt", decrypt);
			log.info("{}", dataLogRes);
			return decrypt.path("data");

			//------------- test khong pgp -------

//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//			headers.set("authkey", digitexToken);
//			HttpEntity<?> entity = new HttpEntity<>(data.textValue(), headers);
//			ResponseEntity<?> res = restTemplate.postForEntity(url, entity, Object.class);
//
//			ObjectNode dataLogRes = mapper.createObjectNode();
//			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==DIGITEXX==]");
//			dataLogRes.put("status", 200);
//			dataLogRes.put("data", data.textValue());
//			dataLogRes.put("result", res.getBody().toString());
//			dataLogRes.set("payload", data);
//			log.info("{}", dataLogRes);
//
//			JsonNode body = mapper.valueToTree(res.getBody());
//			return body;

		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==DIGITEXX==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", data);
			log.info("{}", dataLogRes);

			return mapper.createObjectNode().put("resultCode", 500).put("message", e.getMessage())
					.put("error-code", "api error: ").put("error-description", e.toString());
		}
	}

	public JsonNode retryUploadPartner(JsonNode request, Map partner) {
		Map<?, ?> data = Map.of("file", request.path("body"));
		JsonNode jNode = null;

		boolean checkIdCard = false;
		boolean checkHousehold = false;
		try {
			if (request.path("body").path("data").path("applicationId").textValue() == null){
				ArrayNode documents = mapper.createArrayNode();
				MultiValueMap<String, Object> parts_02 =
						new LinkedMultiValueMap<String, Object>();

				Query query = new Query();
				query.addCriteria(Criteria.where("quickLeadId").is(request.path("body").path("data").path("quickLeadId").textValue()));
				Application checkExist = mongoTemplate.findOne(query, Application.class);

				List<QLDocument> dataUpload = checkExist.getQuickLead().getDocuments();
				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())){
						doc.put("file-name", "Personal-Image_" + item.getOriginalname());
						doc.put("md5", item.getMd5());
						documents.add(doc);

						HttpHeaders headers = new HttpHeaders();
						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
						HttpEntity<String> entity = new HttpEntity<>(headers);
						ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

						MultipartFile multipartFileToSend = new MockMultipartFile("Personal-Image_" + item.getOriginalname(),
								"Personal-Image_" + item.getOriginalname(), item.getContentType(), response.getBody());
						parts_02.add("Personal-Image", multipartFileToSend.getResource());
					}else if (item.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())){
						doc.put("file-name", "ACCA-Form_" + item.getOriginalname());
						doc.put("md5", item.getMd5());
						documents.add(doc);

						HttpHeaders headers = new HttpHeaders();
						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
						HttpEntity<String> entity = new HttpEntity<>(headers);
						ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

						MultipartFile multipartFileToSend = new MockMultipartFile("ACCA-Form_" + item.getOriginalname(),
								"ACCA-Form_" + item.getOriginalname(), item.getContentType(), response.getBody());
						parts_02.add("ACCA-Form", multipartFileToSend.getResource());
					}
				}

				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase())){
						if (!checkIdCard) {
							doc.put("file-name", "ID-Card_" + item.getOriginalname());

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
									"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


							MessageDigest md5 = MessageDigest.getInstance("MD5");
							byte[] digest = md5.digest(multipartFileToSend.getBytes());
							String hashString = new BigInteger(1, digest).toString(16);
							doc.put("md5", item.getMd5());
							documents.add(doc);


							parts_02.add("ID-Card", multipartFileToSend.getResource());

							checkIdCard = true;

						}
					}
					else if (item.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase())){
						if (!checkHousehold) {
							doc.put("file-name", "Household_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
									"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("Household", multipartFileToSend.getResource());

							checkHousehold = true;
						}
					}
				}

				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())){
						if (!checkIdCard) {
							doc.put("file-name", "ID-Card_" + item.getOriginalname());

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
									"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


							MessageDigest md5 = MessageDigest.getInstance("MD5");
							byte[] digest = md5.digest(multipartFileToSend.getBytes());
							String hashString = new BigInteger(1, digest).toString(16);
							doc.put("md5", item.getMd5());
							documents.add(doc);


							parts_02.add("ID-Card", multipartFileToSend.getResource());

							checkIdCard = true;
						}
					}
					else if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())){
						if (!checkHousehold) {
							doc.put("file-name", "Household_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
									"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("Household", multipartFileToSend.getResource());

							checkHousehold = true;
						}
					}
				}


				parts_02.add("description", Map.of("files", documents));

				try{
					HttpHeaders headers_DT = new HttpHeaders();

					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
					String partnerId = (String) mapper.convertValue(partner.get("data"), Map.class).get("partnerId");
					if(partnerId.equals("1")){
						headers_DT.set("authkey", (String) (mapper.convertValue(partner.get("data"), Map.class).get("token")));
						headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
						HttpEntity<?> entity_DT = new HttpEntity<>(parts_02, headers_DT);

						String documentApi = (String) mapper.convertValue(url, Map.class).get("documentApi");
						System.out.println("documentApi line 812: " + documentApi);

						ResponseEntity<?> res_DT = restTemplateDownload.postForEntity(documentApi, entity_DT, Object.class);

						Object map = mapper.valueToTree(res_DT.getBody());

						JsonNode outputDT = mapper.readTree(mapper.writeValueAsString(((JsonNode) map).get("output")));
						ArrayNode array = mapper.valueToTree(dataUpload);
						jNode = mergeFile(array, outputDT);


					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));

						String tokenPartner = this.getTokenSaigonBpo(urlGetToken, account);

						if(StringUtils.isEmpty(tokenPartner)){
							return mapper.convertValue(Map.of("result_code", 3, "message","Not get token saigon-bpo"), JsonNode.class);
						}

						headers_DT.set("authkey", tokenPartner);
						headers_DT.setBearerAuth(tokenPartner);
						parts_02.remove("description");
						parts_02.add("Description", Map.of("files", documents));
						parts_02.set("access_token", tokenPartner);
						headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
						HttpEntity<?> entity_DT = new HttpEntity<>(parts_02, headers_DT);

						String documentApi = (String) mapper.convertValue(url, Map.class).get("documentApi");

						ResponseEntity<?> res_DT = restTemplateDownload.postForEntity(documentApi, entity_DT, Object.class);

						Object map = mapper.valueToTree(res_DT.getBody());

						JsonNode outputDT = mapper.readTree(mapper.writeValueAsString(((JsonNode) map).get("output")));
						ArrayNode array = mapper.valueToTree(dataUpload);
						jNode = mergeFile(array, outputDT);
					}

				} catch (Exception e) {
					log.info("[==HTTP-LOG-RESPONSE==Exception] : {}",
							Map.of("Exception: ", e.toString(), "payload", request));
					ObjectNode doc = mapper.createObjectNode();
					doc.put("uploadDigiTex", "FAIL");
					return doc;
				}

			}else{
				ArrayNode documents = mapper.createArrayNode();
				MultiValueMap<String, Object> parts_02 =
						new LinkedMultiValueMap<String, Object>();

				Query query = new Query();
				query.addCriteria(Criteria.where("applicationId").is(request.path("body").path("data").path("applicationId").textValue()));
				Application checkExist = mongoTemplate.findOne(query, Application.class);
				List<QLDocument> dataUploadOld = checkExist.getQuickLead().getDocuments();
				List<QLDocument> dataUpload = checkExist.getQuickLead().getDocumentsComment();
				List<QLDocument> dataUploadAfterSubmit = checkExist.getQuickLead().getDocumentsAfterSubmit();

				if (checkExist.getApplicationInformation() != null) {//retry document after submit
					for (QLDocument item : dataUploadAfterSubmit) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())) {
							doc.put("file-name", "Personal-Image_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Personal-Image_" + item.getOriginalname(),
									"Personal-Image_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("Personal-Image", multipartFileToSend.getResource());
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
							doc.put("file-name", "ACCA-Form_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ACCA-Form_" + item.getOriginalname(),
									"ACCA-Form_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("ACCA-Form", multipartFileToSend.getResource());
						}
					}

					for (QLDocument item : dataUploadAfterSubmit) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase())) {
							if (!checkIdCard) {
								doc.put("file-name", "ID-Card_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
										"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


								parts_02.add("ID-Card", multipartFileToSend.getResource());

								checkIdCard = true;
							}
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase())) {
							if (!checkHousehold) {
								doc.put("file-name", "Household_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
										"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
								parts_02.add("Household", multipartFileToSend.getResource());

								checkHousehold = true;
							}
						}
					}

					for (QLDocument item : dataUploadAfterSubmit) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
							if (!checkIdCard) {
								doc.put("file-name", "ID-Card_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
										"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


								parts_02.add("ID-Card", multipartFileToSend.getResource());

								checkIdCard = true;

							}
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
							if (!checkHousehold) {
								doc.put("file-name", "Household_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
										"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
								parts_02.add("Household", multipartFileToSend.getResource());

								checkHousehold = true;
							}
						}
					}
				}else {
					for (QLDocument item : dataUpload) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())) {
							doc.put("file-name", "Personal-Image_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().contains("TPF_Customer Photograph".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Personal-Image_" + item.getOriginalname(),
									"Personal-Image_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("Personal-Image", multipartFileToSend.getResource());
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
							doc.put("file-name", "ACCA-Form_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().contains("TPF_Application cum Credit Contract (ACCA)".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ACCA-Form_" + item.getOriginalname(),
									"ACCA-Form_" + item.getOriginalname(), item.getContentType(), response.getBody());
							parts_02.add("ACCA-Form", multipartFileToSend.getResource());
						}
					}

					for (QLDocument item : dataUpload) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase())) {
							if (!checkIdCard) {
								doc.put("file-name", "ID-Card_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
										"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


								parts_02.add("ID-Card", multipartFileToSend.getResource());

								checkIdCard = true;
							}
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase())) {
							if (!checkHousehold) {
								doc.put("file-name", "Household_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
										"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
								parts_02.add("Household", multipartFileToSend.getResource());

								checkHousehold = true;
							}
						}
					}

					for (QLDocument item : dataUpload) {
						ObjectNode doc = mapper.createObjectNode();
						if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
							if (!checkIdCard) {
								doc.put("file-name", "ID-Card_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_ID Card".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of ID card".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
										"ID-Card_" + item.getOriginalname(), item.getContentType(), response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


								parts_02.add("ID-Card", multipartFileToSend.getResource());

								checkIdCard = true;

							}
						} else if (item.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
							if (!checkHousehold) {
								doc.put("file-name", "Household_" + item.getOriginalname());
								doc.put("md5", item.getMd5());
								String docId = null;
								for (QLDocument item2 : dataUploadOld) {
									if (item2.getOriginalname().toUpperCase().contains("TPF_Family Book".toUpperCase()) ||
											item2.getOriginalname().toUpperCase().contains("TPF_Notarization of Family Book".toUpperCase())) {
										docId = item2.getUrlid();
									}
								}
								doc.put("document-id", docId);
								documents.add(doc);

								HttpHeaders headers = new HttpHeaders();
								headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
								HttpEntity<String> entity = new HttpEntity<>(headers);
								ResponseEntity<byte[]> response = restTemplateDownload.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

								MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
										"Household_" + item.getOriginalname(), item.getContentType(), response.getBody());
								parts_02.add("Household", multipartFileToSend.getResource());

								checkHousehold = true;
							}
						}
					}
				}


				parts_02.add("description", Map.of("files", documents));

				try{
					HttpHeaders headers_DT = new HttpHeaders();

					Object url = mapper.convertValue(partner.get("data"), Map.class).get("url");
					Map<String, Object> account = mapper.convertValue(mapper.convertValue(partner.get("data"), Map.class).get("account"), Map.class);
					String partnerId = (String) mapper.convertValue(partner.get("data"), Map.class).get("partnerId");
					if(partnerId.equals("1")){
						headers_DT.set("authkey", (String) (mapper.convertValue(partner.get("data"), Map.class).get("token")));
						headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
						HttpEntity<?> entity_DT = new HttpEntity<>(parts_02, headers_DT);

						String resubmitDocumentApi = (String) mapper.convertValue(url, Map.class).get("resumitDocumentApi");
						log.info("{}", entity_DT.toString());
						ResponseEntity<?> res_DT = restTemplateDownload.postForEntity(resubmitDocumentApi, entity_DT, Object.class);
						log.info("{}", res_DT.toString());
						Object map = mapper.valueToTree(res_DT.getBody());

						JsonNode outputDT = mapper.readTree(mapper.writeValueAsString(((JsonNode) map).get("output")));
						ArrayNode array = mapper.valueToTree(dataUpload);
						jNode = mergeFile(array, outputDT);
					} else if(partnerId.equals("2")){
						String urlGetToken = (String) (mapper.convertValue(url, Map.class).get("getToken"));
						String tokenPartner = this.getTokenSaigonBpo(urlGetToken, account);
						if(StringUtils.isEmpty(tokenPartner)){
							return mapper.convertValue(Map.of("result_code", 3, "message","Not get token saigon-bpo"), JsonNode.class);
						}
						headers_DT.set("authkey", tokenPartner);
						headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
						headers_DT.setBearerAuth(tokenPartner);
						parts_02.remove("description");
						parts_02.add("Description", Map.of("files", documents));
						parts_02.set("access_token", tokenPartner);
						HttpEntity<?> entity_DT = new HttpEntity<>(parts_02, headers_DT);

						String resubmitDocumentApi = (String) mapper.convertValue(url, Map.class).get("resumitDocumentApi");
						log.info("{}", entity_DT.toString());
						ResponseEntity<?> res_DT = restTemplateDownload.postForEntity(resubmitDocumentApi, entity_DT, Object.class);
						log.info("{}", res_DT.toString());
						Object map = mapper.valueToTree(res_DT.getBody());

						JsonNode outputDT = mapper.readTree(mapper.writeValueAsString(((JsonNode) map).get("output")));
						ArrayNode array = mapper.valueToTree(dataUpload);
						jNode = mergeFile(array, outputDT);
					}
				} catch (Exception e) {
					log.info("[==HTTP-LOG-RESPONSE==Exception] : {}",
							Map.of("Exception: ", e.toString(), "payload", request));
					ObjectNode doc = mapper.createObjectNode();
					doc.put("uploadDigiTex", "FAIL");
					return doc;
				}
			}
		} catch (HttpClientErrorException e) {
			log.info("[==HTTP-LOG-RESPONSE==Exception] : {}",
					Map.of("Exception: ", e.toString()));
			ObjectNode doc = mapper.createObjectNode();
			doc.put("uploadDigiTex", "FAIL");
			return doc;

		} catch (Exception e) {
			log.info("[==HTTP-LOG-RESPONSE==Exception] : {}",
					Map.of("Exception: ", e.toString()));
			ObjectNode doc = mapper.createObjectNode();
			doc.put("uploadDigiTex", "FAIL");
			return doc;
		}
		return jNode;
	}

	public JsonNode callApiPartner(String url, JsonNode data, String token, String partnerId) {
		String referenceId = UUID.randomUUID().toString();
		JsonNode result = null;
		try {
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==DATAENTRY-PARTNER-REQUEST==]");
			dataLogReq.put("referenceId", referenceId);
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.put("partnerId", partnerId);
			dataLogReq.set("request_data", data);
			log.info("{}", dataLogReq);
			if(partnerId.equals("1")){
				String dataString = mapper.writeValueAsString(data);

				JsonNode encrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
						Map.of("func", "pgpEncrypt", "body", Map.of("project", "digitex", "data", data)));

				ObjectNode dataLogReq2 = mapper.createObjectNode();
				dataLogReq2.put("type", "[==DATAENTRY-DIGITEX-REQUEST-ENCRYPTED-PGP==]");
				dataLogReq2.put("referenceId", referenceId);
				dataLogReq2.put("request_encrypt", encrypt);
				log.info("{}", dataLogReq2);

				HttpHeaders headers = new HttpHeaders();

				headers.set("authkey", token);

				headers.set("Accept", "application/pgp-encrypted");
				headers.set("Content-Type", "application/pgp-encrypted");
				HttpEntity<String> entity = new HttpEntity<String>(encrypt.path("data").asText(), headers);

				ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);

				ObjectNode dataLogRes = mapper.createObjectNode();
				dataLogRes.put("type", "[==DATAENTRY-DIGITEX-RESPONSE==]");
				dataLogRes.put("referenceId", referenceId);
				dataLogRes.set("status", mapper.convertValue(res.getStatusCode(), JsonNode.class));
				dataLogRes.put("response", res.getBody());
				log.info("{}", dataLogRes);

				JsonNode decrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
						Map.of("func", "pgpDecrypt", "body", Map.of("project", "digitex", "data", res.getBody().toString())));

				ObjectNode dataLogRes2 = mapper.createObjectNode();
				dataLogRes2.put("type", "[==DATAENTRY-DIGITEX-RESPONSE-DECRYPTED-PGP==]");
				dataLogRes2.put("referenceId", referenceId);
				dataLogRes2.set("response_decrypted", decrypt);
				log.info("{}", dataLogRes2);
				result = decrypt.path("data");
			} else if(partnerId.equals("2")){
				String dataString = mapper.writeValueAsString(data);

				JsonNode encrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
						Map.of("func", "pgpEncrypt", "body", Map.of("project", "sgbpo", "data", data)));

				ObjectNode dataLogReq2 = mapper.createObjectNode();
				dataLogReq2.put("type", "[==DATAENTRY-SGBPO-REQUEST-ENCRYPTED-PGP==]");
				dataLogReq2.put("referenceId", referenceId);
				dataLogReq2.put("request_encrypt", encrypt);
				log.info("{}", dataLogReq2);

				HttpHeaders headers = new HttpHeaders();

				headers.setBearerAuth(token);
				headers.set("Content-Type", "text/plain");
				HttpEntity<String> entity = new HttpEntity<String>(encrypt.path("data").asText(), headers);

				ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);

				ObjectNode dataLogRes = mapper.createObjectNode();
				dataLogRes.put("type", "[==DATAENTRY-SGBPO-RESPONSE==]");
				dataLogRes.put("referenceId", referenceId);
				dataLogRes.set("status", mapper.convertValue(res.getStatusCode(), JsonNode.class));
				dataLogRes.put("response", res.getBody());
				log.info("{}", dataLogRes);

				JsonNode decrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
						Map.of("func", "pgpDecrypt", "body", Map.of("project", "sgbpo", "data", res.getBody().toString())));

				ObjectNode dataLogRes2 = mapper.createObjectNode();
				dataLogRes2.put("type", "[==DATAENTRY-SGBPO-RESPONSE-DECRYPTED-PGP==]");
				dataLogRes2.put("referenceId", referenceId);
				dataLogRes2.set("response_decrypted", decrypt);
				log.info("{}", dataLogRes2);
				result = decrypt.path("data");
			}

			return result;

			//KHONG PGP
			/*ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==PARTNER==NOT==PGP]");
			dataLogReq.put("referenceId", referenceId);
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.set("payload", data);
			log.info("{}", dataLogReq);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setBearerAuth(token);
			headers.set("authkey", token);
			HttpEntity<?> entity = new HttpEntity<>(data.textValue(), headers);
			log.info("{}", entity.toString());
			ResponseEntity<?> res = restTemplate.postForEntity(url, entity, Object.class);
			log.info("{}", res.toString());
			JsonNode body = mapper.valueToTree(res.getBody());
			return body;*/
		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==PARTNER==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", data);
			log.info("{}", dataLogRes);

			return mapper.createObjectNode().put("resultCode", 500).put("message", e.getMessage())
					.put("error-code", "api error: ").put("error-description", e.toString());
		}
	}

	public String getTokenSaigonBpo(String url, Map account){
		try {
			HttpHeaders headersAuth = new HttpHeaders();
			headersAuth.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headersAuth.setBasicAuth(account.get("userAuthorization").toString(),  account.get("passwordAuthorization").toString());
			MultiValueMap<String, String> mapToken= new LinkedMultiValueMap<String, String>();
			mapToken.add("username", account.get("userName").toString());
			mapToken.add("password", account.get("passWord").toString());
			mapToken.add("grant_type", "password");
			HttpEntity<MultiValueMap<String, String>> requestToken = new HttpEntity<MultiValueMap<String, String>>(mapToken, headersAuth);
			ResponseEntity<?> responseGetToken = restTemplate.postForEntity(url, requestToken , Object.class );
			JsonNode body = mapper.convertValue(responseGetToken.getBody(), JsonNode.class);
			String tokenPartner = body.path("access_token").asText();
			return tokenPartner;
		}catch (Exception e){
			log.error("Error in ApiService.getTokenSaigonBpo: " + e.getMessage());
			return "";
		}
	}



}