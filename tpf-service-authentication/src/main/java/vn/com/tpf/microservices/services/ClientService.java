package vn.com.tpf.microservices.services;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.models.OauthClientDetails;
import vn.com.tpf.microservices.repositories.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private TokenStore tokenStore;

	@PostConstruct
	private void init() {
		try {
			OauthClientDetails author = new OauthClientDetails();
			author.setClientId("tpf-service-authorization");
			author.setClientSecret(passwordEncoder.encode("tpf-service-authorization"));
			author.setSecret("tpf-service-authorization");
			author.setAuthorizedGrantTypes("authorization_code,password,client_credentials,implicit,refresh_token");
			author.setAccessTokenValidity(60 * 60 * 24 * 30);
			author.setRefreshTokenValidity(60 * 60 * 24 * 30 * 6);
			author.setScope("tpf-service-authorization,tpf-service-authentication");
			repository.save(author);

			OauthClientDetails authen = new OauthClientDetails();
			authen.setClientId("tpf-service-authentication");
			authen.setClientSecret(passwordEncoder.encode("tpf-service-authentication"));
			authen.setSecret("tpf-service-authentication");
			repository.save(authen);

			OauthClientDetails restclient = new OauthClientDetails();
			restclient.setClientId("tpf-service-restclient");
			restclient.setClientSecret(passwordEncoder.encode("tpf-service-restclient"));
			restclient.setSecret("tpf-service-restclient");
			repository.save(restclient);

			System.err.println("CLIENT CREATE SUCCES!!!");
		} catch (Exception e) {
			System.err.println("CLIENT ALREADY EXISTS!!!");
		}
	}

	public Map<String, Object> getListClient(JsonNode request) {
		int page = request.path("param").path("page").asInt(1);
		int limit = request.path("param").path("limit").asInt(10);
		String[] sort = request.path("param").path("sort").asText("createdAt,desc").split(",");

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Direction.fromString(sort[1]), sort[0]));
		Page<OauthClientDetails> result = repository.findAll(pageable);

		return Map.of("status", 200, "data", result.getContent(), "total", result.getTotalElements());
	}

	public Map<String, Object> createClient(JsonNode request) throws Exception {
		Assert.notNull(request.get("body"), "no body");

		OauthClientDetails body = mapper.treeToValue(request.get("body"), OauthClientDetails.class);

		Assert.hasText(body.getClientId(), "clientId is required");
		Assert.hasText(body.getClientSecret(), "clientSecret is required");

		OauthClientDetails entity = new OauthClientDetails();
		entity.setClientId(body.getClientId());
		entity.setSecret(body.getClientSecret());
		entity.setClientSecret(passwordEncoder.encode(entity.getSecret()));
		entity.setResourceIds(body.getResourceIds());
		entity.setAuthorizedGrantTypes(body.getAuthorizedGrantTypes());
		entity.setAccessTokenValidity(body.getAccessTokenValidity());
		entity.setRefreshTokenValidity(body.getRefreshTokenValidity());
		entity.setAuthorities(body.getAuthorities());
		entity.setScope(body.getScope());
		entity.setAutoapprove(body.getAutoapprove());
		entity.setWebServerRedirectUri(body.getWebServerRedirectUri());
		repository.save(entity);

		return Map.of("status", 201, "data", entity);
	}

	public Map<String, Object> updateClient(JsonNode request) throws Exception {
		String id = request.path("param").path("id").asText();
		Assert.hasText(id, "param id is required");
		Assert.notNull(request.get("body"), "no body");

		OauthClientDetails body = mapper.treeToValue(request.get("body"), OauthClientDetails.class);
		Optional<OauthClientDetails> exists = repository.findById(UUID.fromString(id));

		if (exists.isEmpty()) {
			return Map.of("status", 404, "data", Map.of("message", "Not Found"));
		}

		OauthClientDetails entity = exists.get();
		entity.setResourceIds(body.getResourceIds());
		entity.setAuthorizedGrantTypes(body.getAuthorizedGrantTypes());
		entity.setAccessTokenValidity(body.getAccessTokenValidity());
		entity.setRefreshTokenValidity(body.getRefreshTokenValidity());
		entity.setAuthorities(body.getAuthorities());
		entity.setScope(body.getScope());
		entity.setAutoapprove(body.getAutoapprove());
		entity.setWebServerRedirectUri(body.getWebServerRedirectUri());
		repository.save(entity);

		tokenStore.findTokensByClientId(entity.getClientId()).forEach(t -> {
			tokenStore.removeAccessToken(t);
			tokenStore.removeRefreshToken(t.getRefreshToken());
		});

		return Map.of("status", 200, "data", entity);
	}

	public Map<String, Object> deleteClient(JsonNode request) {
		String id = request.path("param").path("id").asText();
		Assert.hasText(id, "param id is required");

		Optional<OauthClientDetails> exists = repository.findById(UUID.fromString(id));

		if (exists.isEmpty()) {
			return Map.of("status", 404, "data", Map.of("message", "Not Found"));
		}

		OauthClientDetails entity = exists.get();
		repository.delete(entity);

		tokenStore.findTokensByClientId(entity.getClientId()).forEach(t -> {
			tokenStore.removeAccessToken(t);
			tokenStore.removeRefreshToken(t.getRefreshToken());
		});

		return Map.of("status", 200, "data", entity);
	}

}