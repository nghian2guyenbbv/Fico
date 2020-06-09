package vn.com.tpf.microservices;

import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoAuditing
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
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
	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
		return new MongoTemplate(mongoDbFactory);
	}

//	@Bean(name = "dspsFicodb")
//	@Primary
//	@ConfigurationProperties(prefix="spring.datasource")
//	public DataSource ficodbDataSource() {
//		return DataSourceBuilder.create().build();
//	}
//
//	@Bean(name = "jdbcTemplate")
//	public JdbcTemplate jdbcTemplateFicodb(@Qualifier("dspsFicodb") DataSource dsMaster) {
//		return new JdbcTemplate(dsMaster);
//	}
//
//	@Bean(name = "dsF1")
//	@ConfigurationProperties(prefix="spring.f1-datasource")
//	public DataSource ficocenDataSource() {
//		return DataSourceBuilder.create().build();
//	}
//
//	@Bean(name = "jdbcTemplateF1")
//	public JdbcTemplate jdbcTemplateF1(@Qualifier("dsF1") DataSource dsMaster) {
//		return new JdbcTemplate(dsMaster);
//	}

}
