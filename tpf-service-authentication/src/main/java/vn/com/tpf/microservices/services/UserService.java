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
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.tpf.microservices.models.OauthUserDetails;
import vn.com.tpf.microservices.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private TokenStore tokenStore;

  @PostConstruct
  private void init() {
    try {
      OauthUserDetails user = new OauthUserDetails();
      user.setUsername("root");
      user.setPassword(passwordEncoder.encode("root"));
      user.setEnabled(true);
      user.setAccountNonExpired(true);
      user.setAccountNonLocked(true);
      user.setCredentialsNonExpired(true);
      user.setAuthorities("role_root");
      repository.save(user);

      System.err.println("USER CREATE SUCCES!!!");
    } catch (Exception e) {
      System.err.println("USER ALREADY EXISTS!!!");
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    OauthUserDetails user = repository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException(username);
    }

    return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
        user.isCredentialsNonExpired(), user.isAccountNonLocked(),
        AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities()));
  }

  public Map<String, Object> getListUser(JsonNode request) {
    int page = request.path("param").path("page").asInt(1);
    int limit = request.path("param").path("limit").asInt(10);
    String[] sort = request.path("param").path("sort").asText("createdAt,desc").split(",");

    Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Direction.fromString(sort[1]), sort[0]));
    Page<OauthUserDetails> result = repository.findAll(pageable);

    return Map.of("status", 200, "data", result.getContent(), "total", result.getTotalElements());
  }

  public Map<String, Object> createUser(JsonNode request, JsonNode token) throws Exception {
    Assert.notNull(request.get("body"), "no body");

    OauthUserDetails body = mapper.treeToValue(request.get("body"), OauthUserDetails.class);

    Assert.hasText(body.getUsername(), "username is required");
    Assert.hasText(body.getPassword(), "password is required");

    if (token.path("authorities").toString().matches(".*(\"role_admin\").*")
        && body.getAuthorities().equals("role_root")) {
      return Map.of("status", 403, "data", Map.of("message", "Forbidden"));
    }

    OauthUserDetails entity = new OauthUserDetails();
    entity.setUsername(body.getUsername());
    entity.setPassword(passwordEncoder.encode(body.getPassword()));
    entity.setAuthorities(body.getAuthorities());
    entity.setEnabled(body.isEnabled());
    entity.setAccountNonExpired(body.isAccountNonExpired());
    entity.setAccountNonLocked(body.isAccountNonLocked());
    entity.setCredentialsNonExpired(body.isCredentialsNonExpired());
    repository.save(entity);

    return Map.of("status", 201, "data", entity);
  }

  public Map<String, Object> updateUser(JsonNode request, JsonNode token) throws Exception {
    String id = request.path("param").path("id").asText();
    Assert.hasText(id, "param id is required");
    Assert.notNull(request.get("body"), "no body");

    OauthUserDetails body = mapper.treeToValue(request.get("body"), OauthUserDetails.class);

    if (token.path("authorities").toString().matches(".*(\"role_admin\").*")
        && body.getAuthorities().equals("role_root")) {
      return Map.of("status", 403, "data", Map.of("message", "Forbidden"));
    }

    Optional<OauthUserDetails> exists = repository.findById(UUID.fromString(id));

    if (exists.isEmpty()) {
      return Map.of("status", 404, "data", Map.of("message", "Not Found"));
    }

    OauthUserDetails entity = exists.get();
    if (body.getUsername() != null) {
      entity.setUsername(body.getUsername());
    }
    if (body.getPassword() != null) {
      entity.setPassword(passwordEncoder.encode(body.getPassword()));
    }
    entity.setAuthorities(body.getAuthorities());
    entity.setEnabled(body.isEnabled());
    entity.setAccountNonExpired(body.isAccountNonExpired());
    entity.setAccountNonLocked(body.isAccountNonLocked());
    entity.setCredentialsNonExpired(body.isCredentialsNonExpired());
    repository.save(entity);

    tokenStore.findTokensByClientIdAndUserName(token.path("client_id").asText(), entity.getUsername()).forEach(t -> {
      tokenStore.removeAccessToken(t);
      tokenStore.removeRefreshToken(t.getRefreshToken());
    });

    return Map.of("status", 200, "data", entity);
  }

  public Map<String, Object> deleteUser(JsonNode request, JsonNode token) {
    String id = request.path("param").path("id").asText();
    Assert.hasText(id, "param id is required");

    Optional<OauthUserDetails> exists = repository.findById(UUID.fromString(id));

    if (exists.isEmpty()) {
      return Map.of("status", 404, "data", Map.of("message", "Not Found"));
    }

    OauthUserDetails entity = exists.get();
    repository.delete(entity);

    tokenStore.findTokensByClientIdAndUserName(token.path("client_id").asText(), entity.getUsername()).forEach(t -> {
      tokenStore.removeAccessToken(t);
      tokenStore.removeRefreshToken(t.getRefreshToken());
    });

    return Map.of("status", 200, "data", entity);
  }

  public Map<String, Object> logoutUser(JsonNode token) {
    String clientId = token.path("client_id").asText();
    String username = token.path("user_name").asText();

    tokenStore.findTokensByClientIdAndUserName(clientId, username).forEach(t -> {
      tokenStore.removeAccessToken(t);
      tokenStore.removeRefreshToken(t.getRefreshToken());
    });

    return Map.of("status", 200);
  }

  public Map<String, Object> changePasswordUser(JsonNode request, JsonNode token) {
    OauthUserDetails entity = repository.findByUsername(token.path("user_name").asText());

    if (!passwordEncoder.matches(request.path("body").path("oldPassword").asText(), entity.getPassword())) {
      return Map.of("status", 404, "data", Map.of("message", "Old Password Invalid"));
    }

    entity.setPassword(passwordEncoder.encode(request.path("body").path("newPassword").asText()));
    repository.save(entity);

    return Map.of("status", 200);
  }

}