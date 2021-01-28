package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import vn.com.tpf.microservices.models.ETLDataPush;

import java.util.List;

public interface ETLDataPushDAO extends JpaRepository<ETLDataPush, Long> {

    List<ETLDataPush> findByAppNumberAndSuorceEtl(@Param("appNumber") String appNumber, @Param("suorceEtl") String suorceEtl);

}
