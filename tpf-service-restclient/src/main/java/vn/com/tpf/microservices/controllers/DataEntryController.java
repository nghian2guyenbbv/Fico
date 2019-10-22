package vn.com.tpf.microservices.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import vn.com.tpf.microservices.services.RabbitMQService;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

@RestController
public class DataEntryController {

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private ObjectMapper mapper;

	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate = new RestTemplate(factory);
	}

	@PostMapping("/v1/dataentry/addproduct")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "addProduct");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@RequestMapping("/v1/dataentry/getproductbyname")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> getProductByName(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getProductByName");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@RequestMapping("/v1/dataentry/getall")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getAll");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@RequestMapping("/v1/dataentry/getappid")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> getByAppId(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getByAppId");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@RequestMapping("/v1/dataentry/getaddress")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> getAddress(@RequestHeader("Authorization") String token)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getAddress");
		request.put("token", token);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@RequestMapping("/v1/dataentry/getbranch")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> getBranch(@RequestHeader("Authorization") String token)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getBranch");
		request.put("token", token);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/dataentry/firstcheck")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> firstCheck(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "firstCheck");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}


	@PostMapping("/dataentry/sendapp")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> sendapp(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "sendApp");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/dataentry/updateapp")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> updateapp(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "updateApp");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/dataentry/commentapp")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> comment(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "commentApp");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/dataentry/updatestatus")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app ')")
	public ResponseEntity<?> cancel(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "cancelApp");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/dataentry/quicklead")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> quicklead(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "quickLead");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@PostMapping("/v1/dataentry/uploadfile")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> uploadfile(@RequestHeader("Authorization") String token,
										@RequestPart("files")  MultipartFile[] files,
										@RequestPart(value = "appId", required = false)  String appId)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		String urlFico = "http://192.168.0.205:3001/v1/file";
//		String urlFico = "http://tpf-service-file:3001/v1/file";
		String urlDigiTex = "https://effektif-connector-qa-global.digi-texx.vn/ConnectorService.svc/json/Interact/ec1a42bf-90df-4dfa-9998-0a82bfd9084b/documentAPI";
		String urlDigiTexResubmit = "https://effektif-connector-qa-global.digi-texx.vn/ConnectorService.svc/json/Interact/ec1a42bf-90df-4dfa-9998-0a82bfd9084b/resubmitDocumentAPI";
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		JsonNode body = null;

		request.put("func", "uploadFile");
		request.put("token", token);
		request.put("appId", appId);

		try {
			MultiValueMap<String, Object> parts =
					new LinkedMultiValueMap<String, Object>();
			for (MultipartFile item:
					files) {
				parts.add("file", item.getResource());
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<?> entity = new HttpEntity<>(parts, headers);
			ResponseEntity<?> res = restTemplate.postForEntity(urlFico, entity, List.class);

			if (res.getStatusCodeValue() == 200){
				JsonNode outputDT = null;
				body = mapper.valueToTree(res.getBody());

				MultiValueMap<String, Object> parts_02 =
						new LinkedMultiValueMap<String, Object>();

				if (appId == null){
					ArrayNode documents = mapper.createArrayNode();
					if(files != null) {
						for (MultipartFile item :files) {
							ObjectNode doc = mapper.createObjectNode();
							MessageDigest md5 = MessageDigest.getInstance("MD5");
							byte[] digest = md5.digest(item.getBytes());
							String hashString = new BigInteger(1, digest).toString(16);

							doc.put("file-name", item.getOriginalFilename());
							doc.put("md5", hashString);

							documents.add(doc);
//							parts.add("ACCA_From", documents);
						}
						parts_02.add("description", Map.of("files", documents));
						parts_02.add("ACCA_From", files[0].getResource());
					}
					HttpHeaders headers_DT = new HttpHeaders();
					headers_DT.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
					headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<?> entity_DT = new HttpEntity<>(parts_02, headers_DT);
					ResponseEntity<?> res_DT = restTemplate.postForEntity(urlDigiTex, entity_DT, String.class);

//					outputDT = mapper.readTree(res_DT.getBody().toString());

				} else{
					ArrayNode documents = mapper.createArrayNode();
					if(files != null) {
						for (MultipartFile item :files) {
							ObjectNode doc = mapper.createObjectNode();
							MessageDigest md5 = MessageDigest.getInstance("MD5");
							byte[] digest = md5.digest(item.getBytes());
							String hashString = new BigInteger(1, digest).toString(16);

							doc.put("file-name", item.getOriginalFilename());
							doc.put("md5", hashString);

							documents.add(doc);
						}
						parts.add("files", documents);
					}
					HttpHeaders headers_DT = new HttpHeaders();
					headers_DT.set("authkey", "699f6095-7a8b-4741-9aa5-e976004cacbb");
					headers_DT.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<?> entity_DT = new HttpEntity<>(parts, headers_DT);
					ResponseEntity<?> res_DT = restTemplate.postForEntity(urlDigiTexResubmit, entity_DT, String.class);

					outputDT = mapper.readTree(res_DT.getBody().toString());
				}

//				JsonNode jNode = mergeFile(body, mapper.valueToTree(outputDT));
				request.put("body", body);

//				return ResponseEntity.status(200)
//						.header("x-pagination-total", "0").body(jNode);
			}
			

//			int i=0;
//			do {
//				Thread.sleep(30000);
//				try{
//
//				} catch (Exception e) {
//				}
//				i = i +1;
//			}while(i<6);


		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(500)
					.header("x-pagination-total", "0").body(e.toString());
		} catch (Exception e) {
			return ResponseEntity.status(500)
					.header("x-pagination-total", "0").body(e.toString());
		}

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);
		return ResponseEntity.status(response.path("status").asInt(500))
				.header("x-pagination-total", response.path("total").asText("0")).body(response.path("data"));
	}

	@RequestMapping("/v1/dataentry/gettatreport")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> getTATReport(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		mapper = new ObjectMapper();
		request.put("func", "getTATReport");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=TATReport_"+new Date().getTime()+".xlsx");

		Base64.Decoder dec = Base64.getDecoder();
		byte[] decbytes = dec.decode(response.path("data").asText());

		ByteArrayInputStream in = new ByteArrayInputStream(decbytes);

		return ResponseEntity
				.ok()
				.headers(headers)
				.body(new InputStreamResource(in));
	}

	@RequestMapping("/v1/dataentry/getstatusreport")
	@PreAuthorize("#oauth2.hasAnyScope('tpf-service-dataentry','tpf-service-app')")
	public ResponseEntity<?> getStatusReport(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
			throws Exception {
		Map<String, Object> request = new HashMap<>();
		request.put("func", "getStatusReport");
		request.put("token", token);
		request.put("body", body);

		JsonNode response = rabbitMQService.sendAndReceive("tpf-service-dataentry", request);

		Base64.Decoder dec = Base64.getDecoder();
		byte[] decbytes = dec.decode(response.path("data").asText());
		ByteArrayInputStream in = new ByteArrayInputStream(decbytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=StatusReport_"+new Date().getTime()+".xlsx");

		return ResponseEntity
				.ok()
				.headers(headers)
				.body(new InputStreamResource(in));
	}

	public Optional<String> getExtensionByStringHandling(String filename) {
		return Optional.ofNullable(filename)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".")));
	}

	public JsonNode mergeFile(JsonNode mainNode, JsonNode updateNode) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode resultNode = mapper.createArrayNode();
		String originalName = updateNode.findPath("originalname").textValue();
		for (JsonNode item : mainNode) {
			for (JsonNode item2 : updateNode) {
				if (item.findPath("originalname").textValue().equals(item2.findPath("originalname").textValue())){
					ObjectNode doc = mapper.createObjectNode();
					doc.put("originalname", item.findPath("originalname").textValue());
					doc.put("filename", item.findPath("filename").textValue());
					doc.put("urlid", item2.findPath("urlId").textValue());
					((ArrayNode) resultNode).add(doc);
				}
			}
		}
		return resultNode;
	}
}

