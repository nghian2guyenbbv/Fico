package vn.com.tpf.microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.tpf.microservices.models.FicoCustomer;
import vn.com.tpf.microservices.models.FicoCustomerLiveBank;

import java.util.List;


public interface FicoCustomerLiveBankDAO extends JpaRepository<FicoCustomerLiveBank, Long> {
    List<FicoCustomerLiveBank> findByCustomerNameContaining(String customerName);
    List<FicoCustomerLiveBank> findByIdentificationNumber(String identificationNumber);
    List<FicoCustomerLiveBank> findByLoanAccountNo(String loanAccountNo);
    FicoCustomerLiveBank findByLoanId(Long loanId);
}
