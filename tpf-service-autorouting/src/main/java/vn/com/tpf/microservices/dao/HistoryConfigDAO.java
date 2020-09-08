package vn.com.tpf.microservices.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import vn.com.tpf.microservices.models.HistoryConfig;

import java.util.List;

public interface HistoryConfigDAO extends JpaRepository<HistoryConfig, Long>,
        PagingAndSortingRepository<HistoryConfig, Long> {

    Page<HistoryConfig> findAllByIdConfigOrderByCreateDateDesc(String idConfig, Pageable pageable);
}
