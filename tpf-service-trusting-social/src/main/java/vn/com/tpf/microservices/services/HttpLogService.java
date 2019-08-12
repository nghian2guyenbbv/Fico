package vn.com.tpf.microservices.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class HttpLogService implements ClientHttpRequestInterceptor {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		ClientHttpResponse response = execution.execute(request, body);

		log.info("[==HTTP-LOG==] : {}",
				Map.of("method", request.getMethod(), "url", request.getURI(), "header", request.getHeaders(),
						"payload", new String(body, "UTF-8"), "status", response.getStatusCode(), "result",
						StreamUtils.copyToString(response.getBody(), Charset.defaultCharset())));

		return response;
	}

}