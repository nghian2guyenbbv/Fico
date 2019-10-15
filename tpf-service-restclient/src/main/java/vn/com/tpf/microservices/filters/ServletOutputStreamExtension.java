package vn.com.tpf.microservices.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class ServletOutputStreamExtension extends ServletOutputStream {

	private ByteArrayOutputStream capture;

	public ServletOutputStreamExtension(ByteArrayOutputStream capture) {
		this.capture = capture;
	}

	@Override
	public void write(int arg0) throws IOException {
		capture.write(arg0);
	}

	public void flush() throws IOException {
		super.flush();
		capture.flush();
	}

	public void close() throws IOException {
		super.close();
		capture.close();
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener listener) {

	}

}
