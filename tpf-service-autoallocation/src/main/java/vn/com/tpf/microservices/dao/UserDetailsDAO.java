package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.UserDetail;
import vn.com.tpf.microservices.models.UserDetailView;

public interface UserDetailsDAO extends JpaRepository<UserDetail, Long> {
    UserDetail findByUserName(String userName);
}
