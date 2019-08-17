package vn.com.tpf.microservices.services;

import org.springframework.amqp.rabbit.logback.AmqpAppender;

public class AmqpService extends AmqpAppender {

	public AmqpService() {
		super();
		this.setExchangeType("direct");
		this.setExchangeName("");
	}

}
