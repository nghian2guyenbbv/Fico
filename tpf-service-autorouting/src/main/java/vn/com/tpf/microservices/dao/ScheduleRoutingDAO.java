package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.tpf.microservices.models.ScheduleRoute;

import java.util.List;
import java.util.Map;

public interface ScheduleRoutingDAO extends JpaRepository<ScheduleRoute, Long> {
    //List<ScheduleRoute> findByIdConfig(String idConfig);
    List<ScheduleRoute> findAllByDayId(String dayId);
}
