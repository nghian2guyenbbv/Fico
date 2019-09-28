package vn.com.tpf.microservices.services;

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
import com.fasterxml.jackson.databind.node.ObjectNode;

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
			author.setScope("tpf-service-authorization,tpf-service-authentication,tpf-service-app");
			repository.save(author);

			OauthClientDetails authen = new OauthClientDetails();
			authen.setClientId("tpf-service-authentication");
			authen.setClientSecret(passwordEncoder.encode("tpf-service-authentication"));
			authen.setSecret("tpf-service-authentication");
			authen.setAuthorizedGrantTypes("client_credentials");
			authen.setAccessTokenValidity(60 * 60 * 24 * 30);
			authen.setRefreshTokenValidity(60 * 60 * 24 * 30 * 6);
			authen.setScope("tpf-service-authentication");
			repository.save(authen);

			OauthClientDetails restclient = new OauthClientDetails();
			restclient.setClientId("tpf-service-restclient");
			restclient.setClientSecret(passwordEncoder.encode("tpf-service-restclient"));
			restclient.setSecret("tpf-service-restclient");
			restclient.setAuthorizedGrantTypes("client_credentials");
			restclient.setAccessTokenValidity(60 * 60 * 24 * 30);
			restclient.setRefreshTokenValidity(60 * 60 * 24 * 30 * 6);
			restclient.setScope("tpf-service-restclient");
			repository.save(restclient);

			OauthClientDetails app = new OauthClientDetails();
			app.setClientId("tpf-service-app");
			app.setClientSecret(passwordEncoder.encode("tpf-service-app"));
			app.setSecret("tpf-service-app");
			app.setAuthorizedGrantTypes("client_credentials");
			app.setAccessTokenValidity(60 * 60 * 24 * 30);
			app.setRefreshTokenValidity(60 * 60 * 24 * 30 * 6);
			app.setScope("tpf-service-app");
			repository.save(app);

			System.err.println("CLIENT CREATE SUCCES!!!");
		} catch (Exception e) {
			System.err.println("CLIENT ALREADY EXISTS!!!");
		}
	}

	private JsonNode response(int status, JsonNode data, long total) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).put("total", total).set("data", data);
		return response;
	}

	public JsonNode getListClient(JsonNode request) {
		int page = request.path("param").path("page").asInt(1);
		int limit = request.path("param").path("limit").asInt(10);
		String[] sort = request.path("param").path("sort").asText("createdAt,desc").split(",");

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Direction.fromString(sort[1]), sort[0]));
		Page<OauthClientDetails> result = repository.findAll(pageable);

		return response(200, mapper.convertValue(result.getContent(), JsonNode.class), result.getTotalElements());
	}

	public JsonNode createClient(JsonNode request) throws Exception {
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

		return response(201, mapper.convertValue(entity, JsonNode.class), 0);
	}

	public JsonNode updateClient(JsonNode request) throws Exception {
		String id = request.path("param").path("id").asText();
		Assert.hasText(id, "param id is required");
		Assert.notNull(request.get("body"), "no body");

		OauthClientDetails body = mapper.treeToValue(request.get("body"), OauthClientDetails.class);
		Optional<OauthClientDetails> exists = repository.findById(UUID.fromString(id));

		if (exists.isEmpty()) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"), 0);
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

		return response(200, mapper.convertValue(entity, JsonNode.class), 0);
	}

	public JsonNode deleteClient(JsonNode request) {
		String id = request.path("param").path("id").asText();
		Assert.hasText(id, "param id is required");

		Optional<OauthClientDetails> exists = repository.findById(UUID.fromString(id));

		if (exists.isEmpty()) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"), 0);
		}

		OauthClientDetails entity = exists.get();
		repository.delete(entity);

		tokenStore.findTokensByClientId(entity.getClientId()).forEach(t -> {
			tokenStore.removeAccessToken(t);
			tokenStore.removeRefreshToken(t.getRefreshToken());
		});

		return response(200, mapper.convertValue(entity, JsonNode.class), 0);
	}

}