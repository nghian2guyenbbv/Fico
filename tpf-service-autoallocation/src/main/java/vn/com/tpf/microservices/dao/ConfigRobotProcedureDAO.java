package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.ConfigRobotProcedure;

public interface ConfigRobotProcedureDAO extends JpaRepository<ConfigRobotProcedure, String> {
}
