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
}
