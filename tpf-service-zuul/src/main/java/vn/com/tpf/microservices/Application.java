package vn.com.tpf.microservices;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Value("${spring.rabbitmq.app-id}")
//	private String appId;
//
//	@Bean
//	public Queue queue() {
//		return new Queue(appId, true, false, false);
//	}
//
//	@RabbitListener(queues = "${spring.rabbitmq.app-id}")
//	public Message onMessage() {
//		return null;
//	}

}
