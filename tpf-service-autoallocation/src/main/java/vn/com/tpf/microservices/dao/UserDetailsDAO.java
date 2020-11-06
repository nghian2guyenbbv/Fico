package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import vn.com.tpf.microservices.models.UserDetail;
import vn.com.tpf.microservices.models.UserDetailView;

import javax.persistence.LockModeType;

public interface UserDetailsDAO extends JpaRepository<UserDetail, Long> {
    UserDetail findByUserName(String userName);
}
