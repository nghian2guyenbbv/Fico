package vn.com.tpf.microservices.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import vn.com.tpf.microservices.services.RabbitMQService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Mobile4csController {
    @Autowired
    private RabbitMQService rabbitMQService;

    @PostMapping("/mobile4cs/recheckRegister")
    @PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-mobile4cs')")
    public ResponseEntity<?> recheckRegister(@RequestHeader("Authorization") String token,@RequestBody ObjectNode body) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("func", "recheckRegister");
        request.put("token", token);
        request.put("body", body);
        ObjectNode response = (ObjectNode) rabbitMQService.sendAndReceive("tpf-service-mobile4cs", request);
        return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
    }

    @PostMapping("/mobile4cs/quickInquiry")
    @PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-mobile4cs')")
    public ResponseEntity<?> quickInquiry(@RequestHeader("Authorization") String token,@RequestBody ObjectNode body) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("func", "quickInquiry");
        request.put("token", token);
        request.put("body", body);
        ObjectNode response = (ObjectNode) rabbitMQService.sendAndReceive("tpf-service-mobile4cs", request);
        return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
    }

    @PostMapping("/mobile4cs/detailInquiry")
    @PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-mobile4cs')")
    public ResponseEntity<?> detailInquiry(@RequestHeader("Authorization") String token,@RequestBody ObjectNode body) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("func", "detailInquiry");
        request.put("token", token);
        request.put("body", body);
        ObjectNode response = (ObjectNode) rabbitMQService.sendAndReceive("tpf-service-mobile4cs", request);
        return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
    }

    @PostMapping("/mobile4cs/getProfile")
    @PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-mobile4cs')")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token,@RequestBody ObjectNode body) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("func", "getProfile");
        request.put("token", token);
        request.put("body", body);
        ObjectNode response = (ObjectNode) rabbitMQService.sendAndReceive("tpf-service-mobile4cs", request);
        return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
    }

    @PostMapping("/mobile4cs/sendSms")
    @PreAuthorize("#oauth2.hasAnyScope('tpf-service-root','tpf-service-mobile4cs')")
    public ResponseEntity<?> sendSms(@RequestHeader("Authorization") String token,@RequestBody ObjectNode body) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("func", "sendSms");
        request.put("token", token);
        request.put("body", body);
        ObjectNode response = (ObjectNode) rabbitMQService.sendAndReceive("tpf-service-mobile4cs", request);
        return ResponseEntity.status(response.path("status").asInt(500)).body(response.path("data"));
    }
}
