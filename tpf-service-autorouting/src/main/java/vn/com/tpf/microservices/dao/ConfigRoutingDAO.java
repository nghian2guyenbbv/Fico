package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.ConfigRouting;

public interface ConfigRoutingDAO extends JpaRepository<ConfigRouting, String> {
}
