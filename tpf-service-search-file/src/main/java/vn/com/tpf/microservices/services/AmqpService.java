package vn.com.tpf.microservices.services;

import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.logback.AmqpAppender;

@Service
public class AmqpService extends AmqpAppender {

	public AmqpService() {
		this.setExchangeType("direct");
		this.setExchangeName("");
	}

}
