package vn.com.tpf.microservices;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@Configuration
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

	@Bean(name = "dsFicoPosgres")
	@Primary
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource ficoPosDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("dsFicoPosgres") DataSource dsMaster) {
		return new JdbcTemplate(dsMaster);
	}

	 @Bean(name = "dsFicodb")
	 @ConfigurationProperties(prefix="spring.f1-datasource")
	 public DataSource ficodbDataSource() {
	     return DataSourceBuilder.create().build();
	 }
	 
	 @Bean(name = "jdbcTemplateFico")
	 public JdbcTemplate jdbcTemplateFicodb(@Qualifier("dsFicodb") DataSource dsMaster) {
	       return new JdbcTemplate(dsMaster);
	 }
	 
	 @Bean(name = "dsFicodbde")
	 @ConfigurationProperties(prefix="spring.f1-datasource-de")
	 public DataSource ficodeDataSource() {
	     return DataSourceBuilder.create().build();
	 }
	 
	 @Bean(name = "jdbcTemplateFicoDe")
	 public JdbcTemplate jdbcTemplateFicoDe(@Qualifier("dsFicodbde") DataSource dsMaster) {
	       return new JdbcTemplate(dsMaster);
	 }

	@Bean(name = "dsFicodbih")
	@ConfigurationProperties(prefix="spring.f1-datasource-ih")
	public DataSource ficocenDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplateFicoIh")
	public JdbcTemplate jdbcTemplateFicoIh(@Qualifier("dsFicodbih") DataSource dsMaster) {
		return new JdbcTemplate(dsMaster);
	}
}
