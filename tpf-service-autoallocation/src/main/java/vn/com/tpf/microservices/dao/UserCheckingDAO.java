package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.UserChecking;

public interface UserCheckingDAO extends JpaRepository<UserChecking, Long> {
}
