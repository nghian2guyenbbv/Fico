package vn.com.tpf.microservices.extensions;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

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
