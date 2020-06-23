package vn.com.tpf.microservices.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public  class  Utils {
	
	@Autowired
	private ObjectMapper mapper;

	public  JsonNode getJsonNodeResponse(int resultCode,JsonNode body, JsonNode data) {
		ObjectNode res = mapper.createObjectNode();
		res.put("result_code", resultCode);
		res.put("request_id", body.path("request_id").asText());
		res.put("reference_id", body.path("reference_id").asText());
		res.put("date_time",ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		if (!Objects.isNull(data))
			if(resultCode == 0)
				res.set("data", data);
			else 
				res.put("message", data.path("message").asText());
		return  res;
	}
	
//	public JsonNode getJsonNodeResponseWithLog( JsonNode payload, JsonNode response) throws Exception {
//		ObjectNode dataLog = mapper.createObjectNode();
//		dataLog.put("type", "[==LOCAL-LOG==]");
//		dataLog.set("result",response);
//		try {
//			
//			if ( payload.hasNonNull("data") && payload.path("data").hasNonNull("documents"))
//			{
//				ArrayNode arrayDoc = mapper.convertValue(payload.path("data").path("documents"), ArrayNode.class);
//				for(JsonNode doc : arrayDoc)
//					((ObjectNode)doc).put("documentBase64",  String.format("%s...%s",doc.path("documentBase64").asText().substring(0,10),doc.path("documentBase64").asText().substring(doc.path("documentBase64").asText().length()-10,doc.path("documentBase64").asText().length()-1)));				
//			}
//
//		} catch (Exception e) {
//			dataLog.put("message",e.getMessage());
//		}
//		dataLog.set("payload",payload);
//		log.info("{}", dataLog);
//
//		return dataLog.path("result") ;
//	}
}
