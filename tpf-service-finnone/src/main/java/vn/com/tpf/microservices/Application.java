package vn.com.tpf.microservices;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties.Jdbc;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Value("${spring.rabbitmq.app-id}")
	private String appId;

	@Bean
	public Queue queue() {
		return new Queue(appId, true, false, false);
	}
	
//	@Bean(name = "")
//	public JdbcTemplate jdbcTemplate() {
//		   var ds = new SingleConnectionDataSource();
//	        ds.setDriver(new Driver());
//	        ds.setUrl("jdbc:mysql://localhost:3306/testdb");
//	        ds.setUsername("user7");
//	        ds.setPassword("s$cret");
//
//	
//
//	        return new JdbcTemplate(new SingleConnectionDataSource("", false));
//	}

}
