package vn.com.tpf.microservices.filters;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

import lombok.Getter;
import lombok.Setter;

public class ServletInputStreamExtension extends ServletInputStream {

	@Getter
	@Setter
	private InputStream stream;

	@Override
	public int read() throws IOException {
		return stream.read();
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setReadListener(ReadListener listener) {

	}

}
