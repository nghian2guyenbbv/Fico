package vn.com.tpf.microservices.services;

import org.springframework.amqp.rabbit.logback.AmqpAppender;
import org.springframework.stereotype.Service;

@Service
public class AmqpService extends AmqpAppender {

	public AmqpService() {
		this.setExchangeType("direct");
		this.setExchangeName("");
	}

}
