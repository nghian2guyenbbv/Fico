package vn.com.tpf.microservices.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import vn.com.tpf.microservices.services.UserService;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserService userService;

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		CustomTokenServices tokenService = new CustomTokenServices();
		tokenService.setTokenStore(tokenStore());
		tokenService.setSupportRefreshToken(true);
		return tokenService;
	}
//
//	@Bean
//	public TokenStore tokenStore() {
//		return new JdbcTokenStore(dataSource);
//	}

	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(dataSource);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("isAuthenticated()");
		security.allowFormAuthenticationForClients();

	}

	@Bean
	public TokenStore tokenStore() {
		JdbcTokenStore tokenStore = new JdbcTokenStore(dataSource);
		tokenStore.setInsertAccessTokenSql("insert into oauth_access_token as t (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token)\n" +
				"values (?, ?, ?, ?, ?, ?, ?)\n" +
				"on conflict on constraint oauth_access_token_un  do\n" +
				"update set\n" +
				"    token_id = excluded.token_id,\n" +
				"    token = excluded.token,\n" +
				"    authentication = excluded.authentication,\n" +
				"    refresh_token = excluded.refresh_token\n" +
				"  where t.authentication_id = excluded.authentication_id");
		return tokenStore;
	}

//	@Bean
//	public PlatformTransactionManager transactionManager(){
//		JpaTransactionManager transactionManager = new JpaTransactionManager();
//		transactionManager.setDataSource(dataSource);
//		return transactionManager;
//	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(dataSource);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore());
		endpoints.tokenServices(tokenServices());
		endpoints.authorizationCodeServices(authorizationCodeServices());
		endpoints.userDetailsService(userService);
		endpoints.authenticationManager(authenticationManager);
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
	}

}