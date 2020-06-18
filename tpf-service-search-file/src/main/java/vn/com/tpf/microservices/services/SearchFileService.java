package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.models.SearchingInfo;

@Service
public class SearchFileService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}

	public JsonNode getListPathFileByKeyword(JsonNode request) {
		ObjectNode data = mapper.createObjectNode();
		try {
			SearchingInfo searchingInfo = getSearchingInfo(request);
			if (searchingInfo == null) {
				/* this case will be captured by catch block */
				throw new Exception("searching information incorrect.");
			}
			String query = String.format("SELECT  FN_SEARCH_FILE_DSA_PAGING ('%s','%s') RESULT FROM DUAL",
					searchingInfo.getSearchKey(),
					searchingInfo.getPageNum());

			String row_string = jdbcTemplate.queryForObject(query,new Object[]{},
					(rs, rowNum) ->
							rs.getString(("RESULT")
							));

			JsonNode rows =  mapper.readTree(row_string);
			return response(200, rows);
		}
		catch (Exception e) {
			data.put("message", e.getMessage());
			return response(500, data);
		}
	}

	private SearchingInfo getSearchingInfo(JsonNode request) {
		try {

			String searchKey = request.path("body").path("appId").asText();
			String pageNum = request.path("body").path("pageNum").asText();

			if (pageNum != null && !pageNum.isEmpty()) {
				try {
					int page = Integer.parseInt(pageNum);
					if (page <= 0) {
						/* setting to default value*/
						pageNum = "1";
					}
				}
				catch (Exception e) {
					/* setting to default value*/
					pageNum = "1";
				}
			}
			/* in this case pageNum not input by user*/
			else {
				/* setting to default value*/
				pageNum = "1";
			}
			return new SearchingInfo(pageNum, searchKey);
		}
		/*handle for case user have no input the search key*/
		catch (Exception e) {
			return null;
		}
	}
}