package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.ScheduleRoute;

import java.util.List;

public interface ScheduleRoutingDAO extends JpaRepository<ScheduleRoute, String> {
    //List<ScheduleRoute> findByIdConfig(String idConfig);
}
