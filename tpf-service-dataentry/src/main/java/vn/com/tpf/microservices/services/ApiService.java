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

	private String urlFirstCheck;

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

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private RabbitMQService rabbitMQService;

	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Arrays.asList(new HttpLogService()));
	}

	public String firstCheck(JsonNode request) {
		Map<?, ?> data = Map.of("file", request.path("body"));
		try {
			Assert.notNull(request.get("body"), "no body");
//			FirstCheckRequest requestFirstCheck = mapper.treeToValue(request.path("body").path("data"), FirstCheckRequest.class);
			FirstCheckRequest requestFirstCheck = new FirstCheckRequest();
			requestFirstCheck.setProject_name("de");

			requestFirstCheck.setRequest_id("de-"+ UUID.randomUUID().toString().substring(0,11));
			requestFirstCheck.setFull_name(request.path("body").path("data").path("customerName").textValue());
			requestFirstCheck.setCustomer_id(request.path("body").path("data").path("customerId").textValue());
			requestFirstCheck.setDsa_code(request.path("body").path("data").path("dsaCode").textValue());
			requestFirstCheck.setBank_card_number(request.path("body").path("data").path("bankCardNumber").textValue());
			requestFirstCheck.setCurrent_address(request.path("body").path("data").path("currentAddress").textValue());
			requestFirstCheck.setArea_code(request.path("body").path("data").path("areaId").textValue());
			requestFirstCheck.setBirthday("");
			requestFirstCheck.setGender("");
			requestFirstCheck.setPhoneNumber("");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<?> entity = new HttpEntity<>(mapper.writeValueAsString(requestFirstCheck), headers);
			ResponseEntity<?> res = restTemplate.postForEntity(urlFirstcheck, entity, Object.class);
			JsonNode body = mapper.valueToTree(res.getBody());

			FirstCheckResponse firstCheckResponse = mapper.treeToValue(body, FirstCheckResponse.class);
			FirstCheck firstCheck = new FirstCheck();
			firstCheck.setRequest(requestFirstCheck);
			firstCheck.setResponse(firstCheckResponse);
			mongoTemplate.save(firstCheck);

			if (firstCheckResponse.getFirst_check_result().equals("pass")){
				return "pass";
			}
			return "fail";

		} catch (HttpClientErrorException e) {
			log.info("[==HTTP-LOG-RESPONSE==] : {}",
					Map.of("payload", data, "status", e.getStatusCode(), "result", e.getResponseBodyAsString()));
			return e.getMessage();

		} catch (Exception e) {
			log.info("[==HTTP-LOG-RESPONSE==] : {}", Map.of("payload", data, "status", 500, "result", e.getMessage()));
			return e.getCause().toString();
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
//					if (item.getOriginalname().equals("TPF_ID Card.pdf") || item.getOriginalname().equals("TPF_Notarization of ID card.pdf")){
//						doc.put("file-name", "ID-Card_" + item.getOriginalname());
//
//						HttpHeaders headers = new HttpHeaders();
//						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
//						HttpEntity<String> entity = new HttpEntity<>(headers);
//						ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);
//
//						MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
//								"ID-Card_" + item.getOriginalname(), "application/pdf", response.getBody());
//
//
//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);
//
//
//						parts_02.add("ID-Card", multipartFileToSend.getResource());
//					}
//					else if (item.getOriginalname().equals("TPF_Family Book.pdf") || item.getOriginalname().equals("TPF_Notarization of Family Book.pdf")){
//						doc.put("file-name", "Household_" + item.getOriginalname());
//						doc.put("md5", item.getMd5());
//						documents.add(doc);
//
//						HttpHeaders headers = new HttpHeaders();
//						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
//						HttpEntity<String> entity = new HttpEntity<>(headers);
//						ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);
//
//						MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
//								"Household_" + item.getOriginalname(), "application/pdf", response.getBody());
//						parts_02.add("Household", multipartFileToSend.getResource());
//					}else
						if (item.getOriginalname().toUpperCase().equals("TPF_Customer Photograph.pdf".toUpperCase())){
						doc.put("file-name", "Personal-Image_" + item.getOriginalname());
						doc.put("md5", item.getMd5());
						documents.add(doc);

						HttpHeaders headers = new HttpHeaders();
						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
						HttpEntity<String> entity = new HttpEntity<>(headers);
						ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

						MultipartFile multipartFileToSend = new MockMultipartFile("Personal-Image_" + item.getOriginalname(),
								"Personal-Image_" + item.getOriginalname(), "application/pdf", response.getBody());
						parts_02.add("Personal-Image", multipartFileToSend.getResource());
					}else if (item.getOriginalname().toUpperCase().equals("TPF_Application cum Credit Contract (ACCA).pdf".toUpperCase())){
						doc.put("file-name", "ACCA-Form_" + item.getOriginalname());
						doc.put("md5", item.getMd5());
						documents.add(doc);

						HttpHeaders headers = new HttpHeaders();
						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
						HttpEntity<String> entity = new HttpEntity<>(headers);
						ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

						MultipartFile multipartFileToSend = new MockMultipartFile("ACCA-Form_" + item.getOriginalname(),
								"ACCA-Form_" + item.getOriginalname(), "application/pdf", response.getBody());
						parts_02.add("ACCA-form", multipartFileToSend.getResource());
					}
				}

				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().equals("TPF_ID Card.pdf".toUpperCase())){
						if (!checkIdCard) {
							doc.put("file-name", "ID-Card_" + item.getOriginalname());

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
									"ID-Card_" + item.getOriginalname(), "application/pdf", response.getBody());


							MessageDigest md5 = MessageDigest.getInstance("MD5");
							byte[] digest = md5.digest(multipartFileToSend.getBytes());
							String hashString = new BigInteger(1, digest).toString(16);
							doc.put("md5", item.getMd5());
							documents.add(doc);


							parts_02.add("ID-Card", multipartFileToSend.getResource());

							checkIdCard = true;

						}
					}
					else if (item.getOriginalname().toUpperCase().equals("TPF_Family Book.pdf".toUpperCase())){
						if (!checkHousehold) {
							doc.put("file-name", "Household_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
									"Household_" + item.getOriginalname(), "application/pdf", response.getBody());
							parts_02.add("Household", multipartFileToSend.getResource());

							checkHousehold = true;
						}
					}
				}

				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().equals("TPF_Notarization of ID card.pdf".toUpperCase())){
						if (!checkIdCard) {
							doc.put("file-name", "ID-Card_" + item.getOriginalname());

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
									"ID-Card_" + item.getOriginalname(), "application/pdf", response.getBody());


							MessageDigest md5 = MessageDigest.getInstance("MD5");
							byte[] digest = md5.digest(multipartFileToSend.getBytes());
							String hashString = new BigInteger(1, digest).toString(16);
							doc.put("md5", item.getMd5());
							documents.add(doc);


							parts_02.add("ID-Card", multipartFileToSend.getResource());

							checkIdCard = true;
						}
					}
					else if (item.getOriginalname().toUpperCase().equals("TPF_Notarization of Family Book.pdf".toUpperCase())){
						if (!checkHousehold) {
							doc.put("file-name", "Household_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
									"Household_" + item.getOriginalname(), "application/pdf", response.getBody());
							parts_02.add("Household", multipartFileToSend.getResource());

							checkHousehold = true;
						}
					}
				}


				parts_02.add("description", Map.of("files", documents));

				try{
					HttpHeaders headers_DT = new HttpHeaders();
					headers_DT.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
					headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<?> entity_DT = new HttpEntity<>(parts_02, headers_DT);
					ResponseEntity<?> res_DT = restTemplate.postForEntity(urlDigitexDocumentApi, entity_DT, Object.class);

//					Map<String, List> map = mapper.readValue(res_DT.getBody().toString().replaceAll("\"\\{\\[","\\[").replaceAll("\\]\\}\"","\\]"), new TypeReference<Map<String, List>>() {});
//					Map<String, List> map = mapper.readValue(res_DT.getBody().toString(), new TypeReference<Map<String, List>>() {});
					Object map = mapper.valueToTree(res_DT.getBody());

					if (((ObjectNode) map).get("error-code") == null){
						JsonNode outputDT = mapper.readTree(mapper.writeValueAsString(((JsonNode) map).get("output")));
						ArrayNode array = mapper.valueToTree(dataUpload);
						jNode = mergeFile(array, outputDT);
					}else{
						ObjectNode doc = mapper.createObjectNode();
						doc.put("uploadDigiTex", "FAIL");
						((ArrayNode) jNode).add(doc);
					}

				} catch (Exception e) {
					ObjectNode dataLog = mapper.createObjectNode();
					dataLog = mapper.createObjectNode();
					dataLog.put("type", "[==HTTP-LOG==]");
					dataLog.set("result", mapper.convertValue(e.toString(), JsonNode.class));
					log.error("{}", dataLog);
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
				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
//					if (item.getOriginalname().equals("TPF_ID Card.pdf") || item.getOriginalname().equals("TPF_Notarization of ID card.pdf")){
//						doc.put("file-name", "ID-Card_" + item.getOriginalname());
//						doc.put("md5", item.getMd5());
//						String docId = null;
//						for (QLDocument item2 : dataUploadOld) {
//							if (item2.getOriginalname().equals("TPF_ID Card.pdf") ||
//									item2.getOriginalname().equals("TPF_Notarization of ID card.pdf")){
//								docId = item2.getUrlid();
//							}
//						}
//						doc.put("document-id", docId);
//						documents.add(doc);
//
//						HttpHeaders headers = new HttpHeaders();
//						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
//						HttpEntity<String> entity = new HttpEntity<>(headers);
//						ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);
//
//						MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
//								"ID-Card_" + item.getOriginalname(), "application/pdf", response.getBody());
//
//
////						MessageDigest md5 = MessageDigest.getInstance("MD5");
////						byte[] digest = md5.digest(multipartFileToSend.getBytes());
////						String hashString = new BigInteger(1, digest).toString(16);
////						doc.put("md5", item.getMd5());
////						documents.add(doc);
//
//
//						parts_02.add("ID-Card", multipartFileToSend.getResource());
//					}
//					else if (item.getOriginalname().equals("TPF_Family Book.pdf") || item.getOriginalname().equals("TPF_Notarization of Family Book.pdf")){
//						doc.put("file-name", "Household_" + item.getOriginalname());
//						doc.put("md5", item.getMd5());
//						String docId = null;
//						for (QLDocument item2 : dataUploadOld) {
//							if (item2.getOriginalname().equals("TPF_Family Book.pdf") ||
//									item2.getOriginalname().equals("TPF_Notarization of Family Book.pdf")){
//								docId = item2.getUrlid();
//							}
//						}
//						doc.put("document-id", docId);
//						documents.add(doc);
//
//						HttpHeaders headers = new HttpHeaders();
//						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
//						HttpEntity<String> entity = new HttpEntity<>(headers);
//						ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);
//
//						MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
//								"Household_" + item.getOriginalname(), "application/pdf", response.getBody());
//						parts_02.add("Household", multipartFileToSend.getResource());
//					}else
						if (item.getOriginalname().toUpperCase().equals("TPF_Customer Photograph.pdf".toUpperCase())){
						doc.put("file-name", "Personal-Image_" + item.getOriginalname());
						doc.put("md5", item.getMd5());
						String docId = null;
						for (QLDocument item2 : dataUploadOld) {
							if (item2.getOriginalname().toUpperCase().equals("TPF_Customer Photograph.pdf".toUpperCase())){
								docId = item2.getUrlid();
							}
						}
						doc.put("document-id", docId);
						documents.add(doc);

						HttpHeaders headers = new HttpHeaders();
						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
						HttpEntity<String> entity = new HttpEntity<>(headers);
						ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

						MultipartFile multipartFileToSend = new MockMultipartFile("Personal-Image_" + item.getOriginalname(),
								"Personal-Image_" + item.getOriginalname(), "application/pdf", response.getBody());
						parts_02.add("Personal-Image", multipartFileToSend.getResource());
					}else if (item.getOriginalname().toUpperCase().equals("TPF_Application cum Credit Contract (ACCA).pdf".toUpperCase())){
						doc.put("file-name", "ACCA-Form_" + item.getOriginalname());
						doc.put("md5", item.getMd5());
						String docId = null;
						for (QLDocument item2 : dataUploadOld) {
							if (item2.getOriginalname().toUpperCase().equals("TPF_Application cum Credit Contract (ACCA).pdf".toUpperCase())){
								docId = item2.getUrlid();
							}
						}
						doc.put("document-id", docId);
						documents.add(doc);

						HttpHeaders headers = new HttpHeaders();
						headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
						HttpEntity<String> entity = new HttpEntity<>(headers);
						ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

						MultipartFile multipartFileToSend = new MockMultipartFile("ACCA-Form_" + item.getOriginalname(),
								"ACCA-Form_" + item.getOriginalname(), "application/pdf", response.getBody());
						parts_02.add("ACCA-form", multipartFileToSend.getResource());
					}
				}

				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().equals("TPF_ID Card.pdf".toUpperCase())){
						if (!checkIdCard) {
							doc.put("file-name", "ID-Card_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().equals("TPF_ID Card.pdf".toUpperCase()) ||
										item2.getOriginalname().toUpperCase().equals("TPF_Notarization of ID card.pdf".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
									"ID-Card_" + item.getOriginalname(), "application/pdf", response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


							parts_02.add("ID-Card", multipartFileToSend.getResource());

							checkIdCard  = true;
						}
					}
					else if (item.getOriginalname().toUpperCase().equals("TPF_Family Book.pdf".toUpperCase())){
						if (!checkHousehold) {
							doc.put("file-name", "Household_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().equals("TPF_Family Book.pdf".toUpperCase()) ||
										item2.getOriginalname().toUpperCase().equals("TPF_Notarization of Family Book.pdf".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
									"Household_" + item.getOriginalname(), "application/pdf", response.getBody());
							parts_02.add("Household", multipartFileToSend.getResource());

							checkHousehold = true;
						}
					}
				}

				for (QLDocument item : dataUpload) {
					ObjectNode doc = mapper.createObjectNode();
					if (item.getOriginalname().toUpperCase().equals("TPF_Notarization of ID card.pdf".toUpperCase())){
						if (!checkIdCard) {
							doc.put("file-name", "ID-Card_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().equals("TPF_ID Card.pdf".toUpperCase()) ||
										item2.getOriginalname().toUpperCase().equals("TPF_Notarization of ID card.pdf".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("ID-Card_" + item.getOriginalname(),
									"ID-Card_" + item.getOriginalname(), "application/pdf", response.getBody());


//						MessageDigest md5 = MessageDigest.getInstance("MD5");
//						byte[] digest = md5.digest(multipartFileToSend.getBytes());
//						String hashString = new BigInteger(1, digest).toString(16);
//						doc.put("md5", item.getMd5());
//						documents.add(doc);


							parts_02.add("ID-Card", multipartFileToSend.getResource());

							checkIdCard = true;

						}
					}
					else if (item.getOriginalname().toUpperCase().equals("TPF_Notarization of Family Book.pdf".toUpperCase())){
						if (!checkHousehold) {
							doc.put("file-name", "Household_" + item.getOriginalname());
							doc.put("md5", item.getMd5());
							String docId = null;
							for (QLDocument item2 : dataUploadOld) {
								if (item2.getOriginalname().toUpperCase().equals("TPF_Family Book.pdf".toUpperCase()) ||
										item2.getOriginalname().toUpperCase().equals("TPF_Notarization of Family Book.pdf".toUpperCase())) {
									docId = item2.getUrlid();
								}
							}
							doc.put("document-id", docId);
							documents.add(doc);

							HttpHeaders headers = new HttpHeaders();
							headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
							HttpEntity<String> entity = new HttpEntity<>(headers);
							ResponseEntity<byte[]> response = restTemplate.exchange(urlUploadfile + item.getFilename(), HttpMethod.GET, entity, byte[].class);

							MultipartFile multipartFileToSend = new MockMultipartFile("Household_" + item.getOriginalname(),
									"Household_" + item.getOriginalname(), "application/pdf", response.getBody());
							parts_02.add("Household", multipartFileToSend.getResource());

							checkHousehold = true;
						}
					}
				}


				parts_02.add("description", Map.of("files", documents));

				try{
					HttpHeaders headers_DT = new HttpHeaders();
					headers_DT.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
					headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<?> entity_DT = new HttpEntity<>(parts_02, headers_DT);
					ResponseEntity<?> res_DT = restTemplate.postForEntity(urlDigitexResumitDocumentApi, entity_DT, Object.class);

//					Map<String, List> map = mapper.readValue(res_DT.getBody().toString().replaceAll("\"\\{\\[","\\[").replaceAll("\\]\\}\"","\\]"), new TypeReference<Map<String, List>>() {});
//					Map<String, List> map = mapper.readValue(res_DT.getBody().toString(), new TypeReference<Map<String, List>>() {});
					Object map = mapper.valueToTree(res_DT.getBody());

					if (((ObjectNode) map).get("error-code") == null){
						JsonNode outputDT = mapper.readTree(mapper.writeValueAsString(((JsonNode) map).get("output")));
						ArrayNode array = mapper.valueToTree(dataUpload);
						jNode = mergeFile(array, outputDT);
					}else{
						ObjectNode doc = mapper.createObjectNode();
						doc.put("uploadDigiTex", "FAIL");
						((ArrayNode) jNode).add(doc);
					}

				} catch (Exception e) {
					ObjectNode dataLog = mapper.createObjectNode();
					dataLog = mapper.createObjectNode();
					dataLog.put("type", "[==HTTP-LOG==]");
					dataLog.set("result", mapper.convertValue(e.toString(), JsonNode.class));
					log.error("{}", dataLog);
				}
			}
		} catch (HttpClientErrorException e) {
			log.info("[==HTTP-LOG-RESPONSE==] : {}",
					Map.of("payload", data, "status", e.getStatusCode(), "result", e.getResponseBodyAsString()));

		} catch (Exception e) {
			log.info("[==HTTP-LOG-RESPONSE==] : {}", Map.of("payload", data, "status", 500, "result", e.getMessage()));
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
		ObjectMapper mapper = new ObjectMapper();
		JsonNode resultNode = mapper.createArrayNode();
		for (JsonNode item : mainNode) {
			if (updateNode != null) {
				for (JsonNode item2 : updateNode) {
					if (item.findPath("originalname").textValue().toUpperCase().equals("TPF_ID Card.pdf".toUpperCase()) ||
							item.findPath("originalname").textValue().toUpperCase().equals("TPF_Notarization of ID card.pdf".toUpperCase())) {
						if (item2.findPath("document-type").textValue().equals("ID-Card")) {
							ObjectNode doc = mapper.createObjectNode();
							doc.put("originalname", item.findPath("originalname").textValue());
							doc.put("filename", item.findPath("filename").textValue());
							doc.put("md5", item.findPath("md5").textValue());
							doc.put("urlid", item2.findPath("document-id").asText(null));
							((ArrayNode) resultNode).add(doc);
						}
					} else if (item.findPath("originalname").textValue().toUpperCase().equals("TPF_Family Book.pdf".toUpperCase()) ||
							item.findPath("originalname").textValue().toUpperCase().equals("TPF_Notarization of Family Book.pdf".toUpperCase())) {
						if (item2.findPath("document-type").textValue().equals("Household")) {
							ObjectNode doc = mapper.createObjectNode();
							doc.put("originalname", item.findPath("originalname").textValue());
							doc.put("filename", item.findPath("filename").textValue());
							doc.put("md5", item.findPath("md5").textValue());
							doc.put("urlid", item2.findPath("document-id").asText(null));
							((ArrayNode) resultNode).add(doc);
						}
					} else if (item.findPath("originalname").textValue().toUpperCase().equals("TPF_Customer Photograph.pdf".toUpperCase())) {
						if (item2.findPath("document-type").textValue().equals("Personal-Image")) {
							ObjectNode doc = mapper.createObjectNode();
							doc.put("originalname", item.findPath("originalname").textValue());
							doc.put("filename", item.findPath("filename").textValue());
							doc.put("md5", item.findPath("md5").textValue());
							doc.put("urlid", item2.findPath("document-id").asText(null));
							((ArrayNode) resultNode).add(doc);
						}
					} else if (item.findPath("originalname").textValue().toUpperCase().equals("TPF_Application cum Credit Contract (ACCA).pdf".toUpperCase())) {
						if (item2.findPath("document-type").textValue().equals("ACCA-form")) {
							ObjectNode doc = mapper.createObjectNode();
							doc.put("originalname", item.findPath("originalname").textValue());
							doc.put("filename", item.findPath("filename").textValue());
							doc.put("md5", item.findPath("md5").textValue());
							doc.put("urlid", item2.findPath("document-id").asText(null));
							((ArrayNode) resultNode).add(doc);
						}
					} else {
						ObjectNode doc = mapper.createObjectNode();
						doc.put("originalname", item.findPath("originalname").textValue());
						doc.put("filename", item.findPath("filename").textValue());
						doc.put("md5", item.findPath("md5").textValue());
						((ArrayNode) resultNode).add(doc);

						break;
					}
				}
			}else{
				ObjectNode doc = mapper.createObjectNode();
				doc.put("originalname", item.findPath("originalname").textValue());
				doc.put("filename", item.findPath("filename").textValue());
				doc.put("md5", item.findPath("md5").textValue());
				((ArrayNode) resultNode).add(doc);
			}
		}
		return resultNode;
	}


//	public String callApiDigitexx(String url, JsonNode data) {
//		try {
//			ObjectNode dataLogReq = mapper.createObjectNode();
//			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==DIGITEXX==]");
//			dataLogReq.put("method", "POST");
//			dataLogReq.put("url", url);
//			dataLogReq.set("payload", data);
//			log.info("{}", dataLogReq);
//
////			String dataString = mapper.writeValueAsString(data);
////
////			JsonNode encrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
////					Map.of("func", "pgpEncrypt", "body", Map.of("project", "digitex", "data", data)));
////
////			ObjectNode dataLogReq2 = mapper.createObjectNode();
////			dataLogReq2.put("type", "[==HTTP-LOG-REQUEST==DIGITEXX=PGP=]");
////			dataLogReq2.set("payload", encrypt);
////			log.info("{}", dataLogReq2);
////
////			HttpHeaders headers = new HttpHeaders();
////			headers.set("Accept", "application/pgp-encrypted");
////			headers.set("Content-Type", "application/pgp-encrypted");
////			HttpEntity<String> entity = new HttpEntity<String>(encrypt.path("data").asText(), headers);
////			ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);
////
////			ObjectNode dataLogReq3 = mapper.createObjectNode();
////			dataLogReq3.put("type", "[==HTTP-LOG-RESPONSE==DIGITEXX=PGP=]");
////			dataLogReq3.set("payload", mapper.readTree(res.getBody().toString()));
////			log.info("{}", dataLogReq3);
////
////			JsonNode decrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
////					Map.of("func", "pgpDecrypt", "body", Map.of("project", "digitex", "data", res.getBody().toString())));
////
////			ObjectNode dataLogRes = mapper.createObjectNode();
////			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==DIGITEXX==]");
////			dataLogRes.set("status", mapper.convertValue(res.getStatusCode(), JsonNode.class));
////			dataLogRes.set("payload", data);
////			dataLogRes.put("result", decrypt.path("body").path("data").asText());
////
////			log.info("{}", dataLogRes);
////			return decrypt.path("data");
//
//			//------------- test khong pgp -------
//
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//			headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
//			HttpEntity<?> entity = new HttpEntity<>(data.textValue(), headers);
//			ResponseEntity<?> res = restTemplate.postForEntity(url, entity, Object.class);
//			JsonNode body = mapper.valueToTree(res.getBody());
//
//			if (body.path("error-code").asText(null) == null){
//				return null;
//			}else{
//				return body.path("error-code").asText(null) +  ": " + body.path("error-description").asText(null);
//			}
//
//		} catch (Exception e) {
//			ObjectNode dataLogRes = mapper.createObjectNode();
//			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==DIGITEXX==]");
//			dataLogRes.put("status", 500);
//			dataLogRes.put("result", e.toString());
//			dataLogRes.set("payload", data);
//			log.info("{}", dataLogRes);
//
//			return "Call DigiTexx 500!";
//		}
//	}

	public JsonNode callApiDigitexx(String url, JsonNode data) {
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
			headers.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
			HttpEntity<?> entity = new HttpEntity<>(data.textValue(), headers);
			ResponseEntity<?> res = restTemplate.postForEntity(url, entity, Object.class);
			JsonNode body = mapper.valueToTree(res.getBody());

			return body.path("data");
		} catch (Exception e) {
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==DIGITEXX==]");
			dataLogRes.put("status", 500);
			dataLogRes.put("result", e.toString());
			dataLogRes.set("payload", data);
			log.info("{}", dataLogRes);

			return mapper.createObjectNode().put("resultCode", 500).put("message", e.getMessage());
		}
	}

}