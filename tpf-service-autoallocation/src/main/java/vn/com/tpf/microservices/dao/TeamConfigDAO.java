package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.tpf.microservices.models.HistoryAllocation;
import vn.com.tpf.microservices.models.TeamConfig;

import java.util.List;

public interface TeamConfigDAO extends JpaRepository<TeamConfig, Long> {
    @Query("SELECT tc.teamName FROM TeamConfig tc")
    List<String> getListTeamName();
}
