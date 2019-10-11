package vn.com.tpf.microservices.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.services.RabbitMQService;

public class PGPFilter implements Filter {

	private ObjectMapper mapper;
	private RabbitMQService rabbitMQService;

	public PGPFilter(ObjectMapper mapper, RabbitMQService rabbitMQService) {
		super();
		this.mapper = mapper;
		this.rabbitMQService = rabbitMQService;
	}

	private String convertBufferedReadertoString(BufferedReader bufferedReader) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}

		return stringBuilder.toString();
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		HttpServletRequestWrapperExtension req = new HttpServletRequestWrapperExtension(request);
		HttpServletResponseWrapperExtension res = new HttpServletResponseWrapperExtension(response);

		req.updateInputStream(convertBufferedReadertoString(servletRequest.getReader()).getBytes());

		if (request.getUserPrincipal() != null && request.getContentType() != null
				&& request.getContentType().equals("application/pgp-encrypted")) {
			JsonNode body = mapper.createObjectNode();
			try {
				JsonNode decrypt = rabbitMQService.sendAndReceive("tpf-service-assets",
						Map.of("func", "pgpDecrypt", "body", Map.of("project", request.getUserPrincipal().getName(), "data",
								convertBufferedReadertoString(servletRequest.getReader()))));
				if (decrypt.path("data").isObject()) {
					body = decrypt;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			req.updateInputStream(body.toString().getBytes());
		}

		chain.doFilter(req, res);

		String respString = res.getContent();

		if (request.getUserPrincipal() != null && request.getContentType() != null
				&& request.getContentType().equals("application/pgp-encrypted")) {
			try {
				JsonNode encrypt = rabbitMQService.sendAndReceive("tpf-service-assets", Map.of("func", "pgpEncrypt", "body",
						Map.of("project", request.getUserPrincipal().getName(), "data", respString)));
				respString = encrypt.path("data").asText();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		PrintWriter printWriter = servletResponse.getWriter();
		printWriter.write(respString);
		printWriter.flush();
		printWriter.close();
	}

}