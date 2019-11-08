package vn.com.tpf.microservices.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.extensions.HttpServletRequestWrapperExtension;
import vn.com.tpf.microservices.extensions.HttpServletResponseWrapperExtension;

public class ApiFilter implements Filter {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private ObjectMapper mapper;

	public ApiFilter(ObjectMapper mapper) {
		super();
		this.mapper = mapper;

	}

	private String convertBufferedReadertoString(BufferedReader bufferedReader) throws IOException {
		int intValueOfChar;
		String targetString = "";
		while ((intValueOfChar = bufferedReader.read()) != -1) {
			targetString += (char) intValueOfChar;
		}
		return targetString.replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n");
	}

	private String ZonedDateTimeNow() {
//	      ZoneId zoneId = ZoneId.of("Asia/Bangkok"); //Etc/GMT-7  Asia/Bangkok
		return ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}

	private Duration DurationDateTime(String date_time) {

		try {
			ZonedDateTime result = ZonedDateTime.parse(date_time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			return Duration.between(result,
					ZonedDateTime.parse(ZonedDateTimeNow(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));

		} catch (Exception e) {
			log.error("{}", e.getMessage());
			return null;
		}

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		ObjectNode resp = mapper.createObjectNode();
		if (request.getUserPrincipal() != null && request.getContentType() != null
				&& request.getContentType().equals("application/json")) {
			HttpServletRequestWrapperExtension req = new HttpServletRequestWrapperExtension(request);
			HttpServletResponseWrapperExtension res = new HttpServletResponseWrapperExtension(response);
			String data = convertBufferedReadertoString(request.getReader());
			if (data.isEmpty()) {
				chain.doFilter(req, res);
			} else {
				JsonNode body = mapper.readTree(data);
//			Duration duration = DurationDateTime(body.path("date_time").asText());
//			if (duration.getSeconds() < 150) {
				try {
					log.info("{}", body.toString());
					req.updateInputStream(data.getBytes());
					chain.doFilter(req, res);
					resp = (ObjectNode) mapper.readTree(res.getContent());
				} catch (Exception e) {
					e.printStackTrace();
					log.error("{}", e.getMessage());
				}
//			} else {
//				resp.put("result_code", 498);
//				resp.put("message", String.format("date_time over 150sec. +-%ssec", duration.getSeconds()));
//			}

				resp.put("date_time", ZonedDateTimeNow());

				servletResponse.setContentType("application/json");
				PrintWriter printWriter = servletResponse.getWriter();
				printWriter.write(mapper.writeValueAsString(resp));
				printWriter.flush();
				printWriter.close();
			}
		} else

		{

			chain.doFilter(request, response);

		}
	}

}