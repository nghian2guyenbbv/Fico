package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.tpf.microservices.models.ScheduleRoute;

import java.util.List;

public interface ScheduleRoutingDAO extends JpaRepository<ScheduleRoute, String> {
    List<ScheduleRoute> findAllByDayId(String dayId);

    @Query("SELECT s FROM ScheduleRoute s WHERE s.configRouting.idConfig = ?1")
    List<ScheduleRoute> findByIdConfig(String idConfig);

}
