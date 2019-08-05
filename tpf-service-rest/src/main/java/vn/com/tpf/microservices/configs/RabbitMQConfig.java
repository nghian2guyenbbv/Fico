package vn.com.tpf.microservices.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RabbitMQConfig {

  @Value("${spring.rabbitmq.app-id}")
  private String appId;


  @Bean
  public Queue queue() {
    return new Queue(appId, true, false, false);
  }

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(appId, true, false);
  }

  @Bean
  public Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(appId);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}