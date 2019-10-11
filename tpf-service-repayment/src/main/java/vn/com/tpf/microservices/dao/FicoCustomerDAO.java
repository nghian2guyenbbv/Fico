package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.FicoCustomer;

import java.util.List;


public interface FicoCustomerDAO extends JpaRepository<FicoCustomer, Long> {
    List<FicoCustomer> findByCustomerNameContaining(String customerName);
    List<FicoCustomer> findByIdentificationNumber(String identificationNumber);
    List<FicoCustomer> findByLoanAccountNo(String loanAccountNo);
    FicoCustomer findByLoanId(Long loanId);
}
