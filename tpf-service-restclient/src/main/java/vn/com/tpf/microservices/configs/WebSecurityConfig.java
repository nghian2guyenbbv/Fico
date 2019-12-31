package vn.com.tpf.microservices.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.filters.ApiFilter;
import vn.com.tpf.microservices.filters.PGPFilter;
import vn.com.tpf.microservices.services.RabbitMQService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addExposedHeader("x-pagination-total");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	@Bean
	public FilterRegistrationBean<PGPFilter> pgpFilter() {
		FilterRegistrationBean<PGPFilter> bean = new FilterRegistrationBean<>(new PGPFilter(mapper, rabbitMQService));
		bean.setOrder(2);
		return bean;
	}
	
//	@Profile("pro")
	@Bean
	public FilterRegistrationBean<ApiFilter> pgpFilter2() {
		FilterRegistrationBean<ApiFilter> bean = new FilterRegistrationBean<>(new ApiFilter(mapper));
		bean.setOrder(3);
		return bean;
	}

}