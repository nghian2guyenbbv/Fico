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
	
	 @Bean(name = "dsFicodb")
	 @Primary
	 @ConfigurationProperties(prefix="spring.ficodb-datasource")
	 public DataSource ficodbDataSource() {
	     return DataSourceBuilder.create().build();
	 }
	 
	 @Bean(name = "jdbcTemplate")
	 public JdbcTemplate jdbcTemplateFicodb(@Qualifier("dsFicodb") DataSource dsMaster) {
	       return new JdbcTemplate(dsMaster);
	 }
	
	 
	 @Bean(name = "dsFicocen")
	 @ConfigurationProperties(prefix="spring.ficocen-datasource")
	 public DataSource ficocenDataSource() {
	     return DataSourceBuilder.create().build();
	 }
	 
	 @Bean(name = "jdbcTemplateFicocen")
	 public JdbcTemplate jdbcTemplateFicocen(@Qualifier("dsFicocen") DataSource dsMaster) {
	       return new JdbcTemplate(dsMaster);
	 }
	

}
