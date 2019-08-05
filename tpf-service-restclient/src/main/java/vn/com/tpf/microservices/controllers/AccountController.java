package vn.com.tpf.microservices.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import vn.com.tpf.microservices.services.RabbitMQService;

@RestController
public class AccountController {

  @Autowired
  private RabbitMQService rabbitMQService;

  @GetMapping("/v1/account")
  @PreAuthorize("#oauth2.hasAnyScope('tpf-service-authorization') and hasAnyAuthority('role_root','role_admin')")
  public ResponseEntity<?> reads(@RequestHeader("Authorization") String token, @RequestParam Map<String, String> param)
      throws Exception {
    Map<String, Object> request = new HashMap<>();
    request.put("func", "getListAccount");
    request.put("token", token);
    request.put("param", param);

    JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
    return ResponseEntity.status(response.get("status").asInt())
        .header("x-pagination-total", response.path("total").asText()).body(response.get("data"));
  }

  @PostMapping("/v1/account")
  @PreAuthorize("#oauth2.hasAnyScope('tpf-service-authorization') and hasAnyAuthority('role_root','role_admin')")
  public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody JsonNode body)
      throws Exception {
    Map<String, Object> request = new HashMap<>();
    request.put("func", "createAccount");
    request.put("token", token);
    request.put("body", body);

    JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
    return ResponseEntity.status(response.get("status").asInt()).body(response.get("data"));
  }

  @PutMapping("/v1/account/{id}")
  @PreAuthorize("#oauth2.hasAnyScope('tpf-service-authorization') and hasAnyAuthority('role_root','role_admin')")
  public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @PathVariable String id,
      @RequestBody JsonNode body) throws Exception {
    Map<String, Object> request = new HashMap<>();
    request.put("func", "updateAccount");
    request.put("token", token);
    request.put("param", Map.of("id", id));
    request.put("body", body);

    JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
    return ResponseEntity.status(response.get("status").asInt()).body(response.get("data"));
  }

  @DeleteMapping("/v1/account/{id}")
  @PreAuthorize("#oauth2.hasAnyScope('tpf-service-authorization') and hasAnyAuthority('role_root')")
  public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable String id)
      throws Exception {
    Map<String, Object> request = new HashMap<>();
    request.put("func", "deleteAccount");
    request.put("token", token);
    request.put("param", Map.of("id", id));

    JsonNode response = rabbitMQService.sendAndReceive("tpf-service-authorization", request);
    return ResponseEntity.status(response.get("status").asInt()).body(response.get("data"));
  }

}