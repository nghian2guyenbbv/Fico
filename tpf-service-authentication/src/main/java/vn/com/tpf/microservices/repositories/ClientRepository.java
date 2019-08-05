package vn.com.tpf.microservices.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.tpf.microservices.models.OauthClientDetails;

@Repository
public interface ClientRepository extends JpaRepository<OauthClientDetails, UUID> {

}