package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.UserDetail;

import java.util.List;

public interface UserDetailsDAO extends JpaRepository<UserDetail, Long> {
    UserDetail findByUserName(String userName);

    List<UserDetail> findByActiveFlag(String activeFlag);
}
