package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.tpf.microservices.models.ETLDataPush;

public interface ETLDataPushDAO extends JpaRepository<ETLDataPush, Long> {
    @Query(value = "SELECT hibernate_sequence.nextval FROM dual", nativeQuery =
            true)
    Long getIdFromSequence();

}
