package vn.com.tpf.microservices;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import vn.com.tpf.microservices.services.HttpLogService;


@SpringBootApplication
@EnableDiscoveryClient
public class Application  {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Value("${spring.rabbitmq.app-id}")
	private String appId;

	@Bean
	public Queue queue() {
		return new Queue(appId, true, false, false);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	 @Bean
		public RestTemplate restTemplate1() {
			ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
			RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.setInterceptors(Arrays.asList(new HttpLogService()));
			return restTemplate;
	 }
}

