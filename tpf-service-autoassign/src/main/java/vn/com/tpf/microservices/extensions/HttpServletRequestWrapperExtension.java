package vn.com.tpf.microservices.extensions;

import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;

public class HttpServletRequestWrapperExtension extends HttpServletRequestWrapper {

	private ServletInputStreamExtension streamInput;

	public HttpServletRequestWrapperExtension(HttpServletRequest request) throws IOException {
		super(request);
		streamInput = new ServletInputStreamExtension();
	}

	public void updateInputStream(byte[] value) {
		streamInput.setStream(new ByteArrayInputStream(value));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return streamInput;
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		if (null != name && name.equalsIgnoreCase("Content-Type")) {
			return new Enumeration<String>() {
				private boolean hasGetted = false;

				@Override
				public boolean hasMoreElements() {
					return !hasGetted;
				}

				@Override
				public String nextElement() {
					hasGetted = true;
					return MediaType.APPLICATION_JSON_UTF8_VALUE;
				}
			};
		}
		return super.getHeaders(name);
	}

}
