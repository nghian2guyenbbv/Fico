package vn.com.tpf.microservices;

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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoAuditing
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

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
		return new MongoTemplate(mongoDbFactory);
	}

	@Bean(name = "dbIH")
	@Primary
	@ConfigurationProperties(prefix="spring.ih-datasource")
	public DataSource ihDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplateIH(@Qualifier("dbIH") DataSource ds) {
		return new JdbcTemplate(ds);
	}

	@Bean(name = "dbF1")
	@ConfigurationProperties(prefix="spring.f1-datasource")
	public DataSource f1DataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplateF1")
	public JdbcTemplate jdbcTemplateF1(@Qualifier("dbF1") DataSource ds) {
		return new JdbcTemplate(ds);
	}

	@Bean(name = "namedParameterJdbcTemplateF1")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplateF1(@Qualifier("dbF1") DataSource ds){
		return new NamedParameterJdbcTemplate(ds);
	}

	@Bean(name = "dbF1DE")
	@ConfigurationProperties(prefix="spring.f1-datasource-de")
	public DataSource f1DataSourceDE() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplateF1DE")
	public JdbcTemplate jdbcTemplateF1DE(@Qualifier("dbF1DE") DataSource ds) {
		return new JdbcTemplate(ds);
	}

	@Bean(name = "namedParameterJdbcTemplateF1DE")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplateF1DE(@Qualifier("dbF1DE") DataSource ds){
		return new NamedParameterJdbcTemplate(ds);
	}


}
