package vn.com.tpf.microservices.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@Value("${url.smsbank}")
	private String urlSms;

	@Value("${sms.user}")
	private String smsUser;
	
	@Value("${sms.password}")
	private String smsPassword;
	

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;


	private JsonNode callApiSMS(String url, JsonNode data) {
		
		String body = String.format("\n"
						+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.tpb.vn/entity/vn/notification/notificationsvcs/1\" xmlns:ns1=\"http://www.tpb.vn/common/envelope/commonheader/1\">\n"
						+ "   <soapenv:Header/>\n" 
						+ "   	<soapenv:Body>\n" 
						+ "      <ns:SendNotificationReq>\n"
						+ "         <ns1:Header>\n" 
						+ "            <ns1:Common>\n"
						+ "               <ns1:ServiceVersion>1</ns1:ServiceVersion>\n"
						+ "               <ns1:MessageId>%s</ns1:MessageId>\n"
						+ "               <ns1:TransactionId>%s</ns1:TransactionId>\n"
						+ "               <ns1:MessageTimestamp>"+ ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")) +"</ns1:MessageTimestamp>\n"
						+ "            </ns1:Common>\n" + "            <ns1:Client>\n"
						+ "               <ns1:SourceAppID>EBANKWS</ns1:SourceAppID>\n"
						+ "               <ns1:TargetAppIDs>\n"
						+ "                  <ns1:TargetAppID>?</ns1:TargetAppID>\n"
						+ "               </ns1:TargetAppIDs>\n" 
						+ "               <ns1:UserDetail>\n"
						+ "                  <ns1:UserId>%s</ns1:UserId><ns1:UserRole>?</ns1:UserRole>\n"
						+ "                  <ns1:UserPassword>%s</ns1:UserPassword>\n"
						+ "               </ns1:UserDetail>\n" 
						+ "            </ns1:Client>\n"
						+ "         </ns1:Header>\n" 
						+ "         <BodyReq>\n"
						+ "             <FunctionCode>NOTIFY-SENDSMS-DB-SMSG</FunctionCode>\n"
						+ "        		<Scode>EBank</Scode>\n" 
						+ "        		<BrandName>TPBank</BrandName>\n"
						+ "        		<PhoneNo>%s</PhoneNo>\n"
						+ "       		<Content>%s</Content>\n"
						+ "        		<KeyId>ESB10001</KeyId>\n" 
						+ "         </BodyReq>\n"
						+ "      </ns:SendNotificationReq>\n" 
						+ "   </soapenv:Body>\n" 
						+ "</soapenv:Envelope>\n",
						new Date().getTime(),new Date().getTime(),
						smsUser, smsPassword, data.path("body").path("phone").asText(), data.path("body").path("content").asText());
		try {
			ObjectNode dataLogReq = mapper.createObjectNode();
			dataLogReq.put("type", "[==HTTP-LOG-REQUEST==]");
			dataLogReq.put("method", "POST");
			dataLogReq.put("url", url);
			dataLogReq.put("payload", body);
			log.info("{}", dataLogReq);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "text/xml");
			headers.set("Content-Type", "text/xml");
			HttpEntity<String> entity = new HttpEntity<String>(body, headers);
			ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);

			
			ObjectNode dataLogRes = mapper.createObjectNode();
			dataLogRes.put("type", "[==HTTP-LOG-RESPONSE==]");
			dataLogRes.set("status", mapper.convertValue(res.getStatusCode(), JsonNode.class));
			dataLogRes.put("payload", body);
			dataLogRes.put("result", res.getBody());
			
			log.info("{}", dataLogRes);
			
			if(res.getBody().contains(
					"<out3:ResponseStatus><out3:Status>0</out3:Status><out3:GlobalErrorCode>00</out3:GlobalErrorCode><out3:GlobalErrorDescription>Success</out3:GlobalErrorDescription></out3:ResponseStatus>")) {
				return mapper.createObjectNode().put("resultCode", 0).put("data",res.getBody());
			}else {
				return mapper.createObjectNode().put("resultCode", 500).put("message", res.getBody());
			}
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

	public JsonNode sendSMS(JsonNode data) {
		return callApiSMS(urlSms, data);
	}

	

}