package vn.com.tpf.microservices.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.tpf.microservices.models.OauthUserDetails;

@Repository
public interface UserRepository extends JpaRepository<OauthUserDetails, UUID> {

	public OauthUserDetails findByUsername(String username);

}