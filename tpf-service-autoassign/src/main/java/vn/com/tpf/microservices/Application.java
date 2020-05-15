package vn.com.tpf.microservices;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.services.AutoAssignService;


@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class Application {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
//		ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
//		AutoAssignService service = applicationContext.getBean(AutoAssignService.class);

//		do{
//			service.createdConfigureAuto();
//			Thread.sleep(3600000);
//		}while(1==1);
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

}
