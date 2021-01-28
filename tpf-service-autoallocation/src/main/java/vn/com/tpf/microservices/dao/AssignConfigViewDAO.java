package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.AssignConfigView;

public interface AssignConfigViewDAO extends JpaRepository<AssignConfigView, Long> {
}
